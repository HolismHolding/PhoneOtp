package phone_otp;

import org.keycloak.provider.ProviderConfigProperty;
import java.util.ArrayList;
import java.util.List;

public class OtpConfig {
    public static List<ProviderConfigProperty> getConfigProperties() {
        List<ProviderConfigProperty> configProperties = new ArrayList<>();

        ProviderConfigProperty urlProperty = new ProviderConfigProperty();
        urlProperty.setName(OtpSender.CONFIG_URL_TEMPLATE);
        urlProperty.setLabel("OTP URL Template");
        urlProperty.setType(ProviderConfigProperty.STRING_TYPE);
        urlProperty.setHelpText("Example: https://some-host.tld/accounts/sendOtp?phone={phone}&otp={otp}&realm={realm}");
        configProperties.add(urlProperty);

        return configProperties;
    }
}
