package phone_otp;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.KeycloakSession;
import org.jboss.logging.Logger; // Import JBoss Logger

public class Auth implements Authenticator {

    private static final Logger LOG = Logger.getLogger(Auth.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        // This is the required core method. It must call one of:
        // context.success(), context.challenge(), or context.failure().
        LOG.info("Phone OTP Authenticator (minimal) running. Calling success().");
        context.success();
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        // Required, but empty for a non-form-based authenticator that immediately succeeds.
    }

    @Override
    public boolean requiresUser() {
        // Assuming this runs *after* user identification (e.g., after username/password).
        return true; 
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        // This is the simplest return for a global authenticator.
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        // Required, but empty for this minimal implementation.
    }

    @Override
    public void close() {
        // Required by the Provider interface, but implementation is usually empty.
    }
}
