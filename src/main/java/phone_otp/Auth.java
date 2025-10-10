package phone_otp;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.KeycloakSession;
import org.jboss.logging.Logger;
import org.keycloak.theme.ThemeProvider;

import java.util.HashMap;
import java.util.Map;

public class Auth implements Authenticator {

    private static final Logger LOG = Logger.getLogger(Auth.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        LOG.info("Phone OTP Authenticator running. Rendering phone form.");

        String phone = (String) context.getSession().getAttribute("phone");
        String errorMessage = (String) context.getSession().getAttribute("phoneError");

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("phone", phone);
        attributes.put("errorMessage", errorMessage);

        context.getSession().getProvider(ThemeProvider.class)
            .getTheme(ThemeProvider.Type.LOGIN)
            .render("templates/phone.ftl", context.getSession(), context.getRequest(), context.getResponse(), attributes);
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

        context.getSession().setAttribute("phone", phone);
        context.success();
    }

    @Override
    public boolean requiresUser() {
        return true;
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
