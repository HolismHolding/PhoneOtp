package phone_otp;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.models.*;
import org.jboss.logging.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class OtpSender {

    private static final Logger LOG = Logger.getLogger(OtpSender.class);
    public static final String CONFIG_URL_TEMPLATE = "otpUrlTemplate";

    public boolean send(AuthenticationFlowContext context, String phone, String otp) {
        try {
            RealmModel realm = context.getRealm();
            AuthenticatorConfigModel cfg = context.getAuthenticatorConfig();
            String urlTemplate = null;

            if (cfg != null && cfg.getConfig() != null) {
                Object value = cfg.getConfig().get(CONFIG_URL_TEMPLATE);
                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    if (!list.isEmpty()) urlTemplate = String.valueOf(list.get(0));
                } else {
                    urlTemplate = String.valueOf(value);
                }
            }

            if (urlTemplate == null || urlTemplate.isEmpty()) {
                LOG.error("OTP URL template not configured");
                context.getSession().setAttribute("otpErrorKey", "otpConfigMissing");
                return false;
            }

            String urlString = urlTemplate
                    .replace("{phone}", phone)
                    .replace("{otp}", otp)
                    .replace("{realm}", realm.getName());

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
            while ((line = reader.readLine()) != null) response.append(line);
            reader.close();
            conn.disconnect();

            LOG.info("OTP server response (" + status + "): " + response);

            if (status == 200) return true;

            context.getSession().setAttribute("otpErrorKey", "otpSendFailed");
            return false;

        } catch (Exception e) {
            LOG.error("Failed to send OTP: " + e.getMessage(), e);
            context.getSession().setAttribute("otpErrorKey", "otpSendFailed");
            return false;
        }
    }
}
