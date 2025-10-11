package phone_otp;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.forms.login.LoginFormsProvider;
import org.jboss.logging.Logger;
import jakarta.ws.rs.core.Response;

public class Auth implements Authenticator {

    private static final Logger LOG = Logger.getLogger(Auth.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        String phone = (String) context.getSession().getAttribute("phone");
        String errorMessage = (String) context.getSession().getAttribute("phoneError");

        LoginFormsProvider form = context.form();
        form.setAttribute("phone", phone);
        form.setAttribute("errorMessage", errorMessage);

        Response challengeResponse = form.createForm("templates/phone.ftl");
        context.challenge(challengeResponse);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        String phone = context.getHttpRequest().getDecodedFormParameters().getFirst("phone");

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
        } else {
            LOG.info("Found existing user for phone: " + phone);
        }

        if (sendOtp(phone)) {
            LOG.info("OTP sent successfully to: " + phone);
        } else {
            LOG.warn("Failed to send OTP to: " + phone);
        }

        context.setUser(user);
        context.success();
    }

    private boolean sendOtp(String phone) {
        LOG.info("Pretending to send OTP to " + phone);
        return true;
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
