package phone_otp;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.*;
import org.jboss.logging.Logger;

import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

public class Auth implements Authenticator {

    private static final Logger LOG = Logger.getLogger(Auth.class);
    private final OtpSender otpSender = new OtpSender();
    private final OtpVerifier otpVerifier = new OtpVerifier();
    private final OtpGenerator otpGenerator = new OtpGenerator();

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        String phone = (String) context.getSession().getAttribute("phone");
        String errorKey = (String) context.getSession().getAttribute("phoneErrorKey");
        context.getSession().removeAttribute("phoneErrorKey");

        LoginFormsProvider form = context.form();
        form.setAttribute("phone", phone);
        form.setAttribute("phoneErrorKey", errorKey);
        String logoUrl = Config.getConfig(context, "logoUrl", String.class);
        if (logoUrl != null) {
            form.setAttribute("logoUrl", logoUrl);
        }

        context.challenge(form.createForm("phone.ftl"));
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formParams = context.getHttpRequest().getDecodedFormParameters();

        if (formParams.containsKey("otp")) {
            otpVerifier.verify(context, formParams.getFirst("otp"));
            return;
        }

        String phone = formParams.getFirst("phone");
        if (phone == null || phone.isEmpty()) {
            context.getSession().setAttribute("phoneErrorKey", "emptyPhoneText");
            authenticate(context);
            return;
        }

        KeycloakSession session = context.getSession();
        RealmModel realm = context.getRealm();
        UserProvider users = session.users();
        UserModel user = users.getUserByUsername(realm, phone);

        if (user == null) {
            user = users.addUser(realm, phone);
            user.setEnabled(true);
        }

        Integer otpLength = Config.getConfig(context, "otpLength", Integer.class);
        String otp = otpGenerator.generate(otpLength);
        context.getAuthenticationSession().setAuthNote("otp", otp);
        context.getAuthenticationSession().setAuthNote("phone", phone);

        if (otpSender.send(context, phone, otp)) {
            Integer secondsToEnableResending = Config.getConfig(context, "secondsToEnableResending", Integer.class);
            Boolean enablePhoneCall = Config.getConfig(context, "enablePhoneCall", Boolean.class);
            Boolean separateOtpInputs = Config.getConfig(context, "separateOtpInputs", Boolean.class);
            Boolean autoSubmitOtp = Config.getConfig(context, "autoSubmitOtp", Boolean.class);
            String logoUrl = Config.getConfig(context, "logoUrl", String.class);

            LoginFormsProvider form = context.form();
            form.setAttribute("phone", phone);
            form.setAttribute("otpLength", otp.length());
            form.setAttribute("secondsToEnableResending", secondsToEnableResending);
            form.setAttribute("enablePhoneCall", enablePhoneCall);
            form.setAttribute("separateOtpInputs", separateOtpInputs);
            form.setAttribute("autoSubmitOtp", autoSubmitOtp);
            if (logoUrl != null) {
                form.setAttribute("logoUrl", logoUrl);
            }
            context.challenge(form.createForm("otp.ftl"));
        } else {
            String error = (String) session.getAttribute("otpErrorKey");
            context.getSession().setAttribute("phoneErrorKey", error != null ? error : "otpSendFailed");
            authenticate(context);
        }

    }

    @Override public boolean requiresUser() { return false; }
    @Override public boolean configuredFor(KeycloakSession s, RealmModel r, UserModel u) { return true; }
    @Override public void setRequiredActions(KeycloakSession s, RealmModel r, UserModel u) {}
    @Override public void close() {}
}
