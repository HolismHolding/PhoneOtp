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

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        String phone = (String) context.getSession().getAttribute("phone");
        String errorMessage = (String) context.getSession().getAttribute("phoneError");

        context.getSession().removeAttribute("phoneError");

        LoginFormsProvider form = context.form();
        form.setAttribute("phone", phone);
        form.setAttribute("errorMessage", errorMessage);

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
            context.getSession().setAttribute("phoneError", "Phone number cannot be empty.");
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

        if (sendOtp(phone, otp)) {
            LOG.info("OTP sent successfully to: " + phone);
            LoginFormsProvider form = context.form();
            form.setAttribute("phone", phone);
            Response otpForm = form.createForm("otp.ftl");
            context.challenge(otpForm);
        } else {
            LOG.warn("Failed to send OTP to: " + phone);
            context.getSession().setAttribute("phoneError", "Failed to send OTP. Please try again.");
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
            form.setAttribute("errorMessage", "Invalid OTP. Please try again.");
            Response otpForm = form.createForm("otp.ftl");
            context.challenge(otpForm);
        }
    }

    private boolean sendOtp(String phone, String otp) {
        LOG.info("Pretending to send OTP " + otp + " to " + phone);
        // Return false here to simulate a failure if you want to test
        return true;
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
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
    }

    @Override
    public void close() {
    }
}
