package phone_otp;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.*;
import org.keycloak.provider.ProviderConfigProperty;
import org.jboss.logging.Logger;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class Auth implements Authenticator {

    private static final Logger LOG = Logger.getLogger(Auth.class);
    public static final String CONFIG_URL_TEMPLATE = "otpUrlTemplate";

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        String phone = (String) context.getSession().getAttribute("phone");
        String errorKey = (String) context.getSession().getAttribute("phoneErrorKey");

        context.getSession().removeAttribute("phoneErrorKey");

        LoginFormsProvider form = context.form();
        form.setAttribute("phone", phone);
        form.setAttribute("phoneErrorKey", errorKey);

        Response challengeResponse = form.createForm("phone.ftl");
        context.challenge(challengeResponse);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formParams = context.getHttpRequest().getDecodedFormParameters();

        if (formParams.containsKey("otp")) {
            verifyOtp(context, formParams.getFirst("otp"));
            return;
        }

        String phone = formParams.getFirst("phone");
        if (phone == null || phone.isEmpty()) {
            context.getSession().setAttribute("phoneErrorKey", "emptyPhoneText");
            authenticate(context);
            return;
        }

        LOG.info("Phone number entered: " + phone);

        KeycloakSession session = context.getSession();
        RealmModel realm = context.getRealm();
        UserProvider userProvider = session.users();

        UserModel user = userProvider.getUserByUsername(realm, phone);
        if (user == null) {
            user = userProvider.addUser(realm, phone);
            user.setEnabled(true);
            LOG.info("Created new user for phone: " + phone);
        }

        String otp = generateOtp();
        session.setAttribute("otp", otp);
        session.setAttribute("phone", phone);

        if (sendOtp(context, phone, otp)) {
            LOG.info("OTP sent successfully to: " + phone);
            LoginFormsProvider form = context.form();
            form.setAttribute("phone", phone);
            Response otpForm = form.createForm("otp.ftl");
            context.challenge(otpForm);
        } else {
            String error = (String) session.getAttribute("otpErrorKey");
            LOG.warn("Failed to send OTP to: " + phone + " — " + error);
            context.getSession().setAttribute("phoneErrorKey", error != null ? error : "otpSendFailed");
            authenticate(context);
        }
    }

    private void verifyOtp(AuthenticationFlowContext context, String enteredOtp) {
        String expectedOtp = (String) context.getSession().getAttribute("otp");
        String phone = (String) context.getSession().getAttribute("phone");

        if (expectedOtp != null && expectedOtp.equals(enteredOtp)) {
            RealmModel realm = context.getRealm();
            UserProvider userProvider = context.getSession().users();
            UserModel user = userProvider.getUserByUsername(realm, phone);

            context.setUser(user);
            LOG.info("OTP verified successfully for phone: " + phone);
            context.success();
        } else {
            LOG.warn("Invalid OTP entered for phone: " + phone);
            LoginFormsProvider form = context.form();
            form.setAttribute("phone", phone);
            form.setAttribute("otpErrorKey", "invalidOtpText");
            Response otpForm = form.createForm("otp.ftl");
            context.challenge(otpForm);
        }
    }

    private boolean sendOtp(AuthenticationFlowContext context, String phone, String otp) {
        try {
            RealmModel realm = context.getRealm();
            AuthenticatorConfigModel authenticatorModel = context.getAuthenticatorConfig();
            String urlTemplate = null;

            if (authenticatorModel != null && authenticatorModel.getConfig() != null) {
                Object value = authenticatorModel.getConfig().get(CONFIG_URL_TEMPLATE);
                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    if (!list.isEmpty()) urlTemplate = String.valueOf(list.get(0));
                } else {
                    urlTemplate = String.valueOf(value);
                }
            }

            if (urlTemplate == null || urlTemplate.isEmpty()) {
                LOG.error("OTP URL template not configured in Admin Panel");
                context.getSession().setAttribute("otpErrorKey", "otpConfigMissing");
                return false;
            }

            String urlString = urlTemplate
                    .replace("{phone}", phone)
                    .replace("{otp}", otp)
                    .replace("{realm}", realm.getName());

            LOG.info("Sending OTP request to URL: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(new byte[0]);
            }

            int status = conn.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    status >= 200 && status < 300 ? conn.getInputStream() : conn.getErrorStream()
            ));

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            conn.disconnect();

            LOG.info("OTP server response (" + status + "): " + response);

            if (status == 200) {
                return true;
            } else {
                context.getSession().setAttribute("otpErrorKey", "otpSendFailed");
                return false;
            }

        } catch (Exception e) {
            LOG.error("Failed to send OTP request: " + e.getMessage(), e);
            context.getSession().setAttribute("otpErrorKey", "otpSendFailed");
            return false;
        }
    }

    private String generateOtp() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {}

    @Override
    public void close() {}

    public static List<ProviderConfigProperty> getConfigProperties() {
        List<ProviderConfigProperty> configProperties = new ArrayList<>();

        ProviderConfigProperty urlProperty = new ProviderConfigProperty();
        urlProperty.setName(CONFIG_URL_TEMPLATE);
        urlProperty.setLabel("OTP URL Template");
        urlProperty.setType(ProviderConfigProperty.STRING_TYPE);
        urlProperty.setHelpText("Example: https://some-host.tld/sms/send?phone={phone}&otp={otp}&realm={realm}");
        configProperties.add(urlProperty);

        return configProperties;
    }
}
