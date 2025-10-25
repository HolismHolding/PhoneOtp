package phone_otp;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.*;
import org.jboss.logging.Logger;

import jakarta.ws.rs.core.Response;

public class OtpVerifier {

    private static final Logger LOG = Logger.getLogger(OtpVerifier.class);

    public void verify(AuthenticationFlowContext context, String enteredOtp) {
        String expectedOtp = context.getAuthenticationSession().getAuthNote("otp");
        String phone = context.getAuthenticationSession().getAuthNote("phone");

        LOG.infof("Phone: %s, Expected OTP: %s, Entered OTP: %s", phone, expectedOtp, enteredOtp);

        if (expectedOtp != null && expectedOtp.equals(enteredOtp)) {
            RealmModel realm = context.getRealm();
            UserModel user = context.getSession().users().getUserByUsername(realm, phone);
            context.setUser(user);
            context.success();
        } else {
            LoginFormsProvider form = context.form();
            form.setAttribute("phone", phone);
            form.setAttribute("otpErrorKey", "invalidOtpText");
            Response otpForm = form.createForm("otp.ftl");
            context.challenge(otpForm);
        }
    }
}
