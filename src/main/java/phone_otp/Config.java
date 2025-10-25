package phone_otp;

import org.keycloak.provider.ProviderConfigProperty;
import java.util.ArrayList;
import java.util.List;

public class Config {
    public static List<ProviderConfigProperty> getConfigProperties() {
        List<ProviderConfigProperty> configProperties = new ArrayList<>();

        ProviderConfigProperty urlProperty = new ProviderConfigProperty();
        urlProperty.setName(OtpSender.CONFIG_URL_TEMPLATE);
        urlProperty.setLabel("OTP URL Template");
        urlProperty.setType(ProviderConfigProperty.STRING_TYPE);
        urlProperty.setHelpText("Example: https://some-host.tld/accounts/sendOtp?phone={phone}&otp={otp}&realm={realm}");
        configProperties.add(urlProperty);

        ProviderConfigProperty fakeSendingOtpProperty = new ProviderConfigProperty();
        fakeSendingOtpProperty.setName("fakeSendingOtp");
        fakeSendingOtpProperty.setLabel("Fake Sending OTP");
        fakeSendingOtpProperty.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        fakeSendingOtpProperty.setHelpText("Enable this to bypass sending the OTP for testing purposes.");
        configProperties.add(fakeSendingOtpProperty);

        ProviderConfigProperty logoUrlProperty = new ProviderConfigProperty();
        logoUrlProperty.setName("logoUrl");
        logoUrlProperty.setLabel("Logo URL");
        logoUrlProperty.setType(ProviderConfigProperty.STRING_TYPE);
        logoUrlProperty.setHelpText("URL of the logo to display in pages.");
        configProperties.add(logoUrlProperty);

        ProviderConfigProperty otpLengthProperty = new ProviderConfigProperty();
        otpLengthProperty.setName("otpLength");
        otpLengthProperty.setLabel("OTP Length");
        otpLengthProperty.setType(ProviderConfigProperty.STRING_TYPE);
        otpLengthProperty.setHelpText("Number of digits in the OTP code (e.g., 4, 6).");
        configProperties.add(otpLengthProperty);

        ProviderConfigProperty secondsToEnableResendingProperty = new ProviderConfigProperty();
        secondsToEnableResendingProperty.setName("secondsToEnableResending");
        secondsToEnableResendingProperty.setLabel("Seconds to Enable Resending");
        secondsToEnableResendingProperty.setType(ProviderConfigProperty.STRING_TYPE);
        secondsToEnableResendingProperty.setHelpText("Number of seconds before the 'resend OTP' option is enabled again.");
        configProperties.add(secondsToEnableResendingProperty);

        ProviderConfigProperty enablePhoneCallProperty = new ProviderConfigProperty();
        enablePhoneCallProperty.setName("enablePhoneCall");
        enablePhoneCallProperty.setLabel("Enable Phone Call");
        enablePhoneCallProperty.setType(ProviderConfigProperty.BOOLEAN_TYPE);
        enablePhoneCallProperty.setHelpText("Enable this to deliver OTP via a phone call instead of SMS.");
        configProperties.add(enablePhoneCallProperty);

        return configProperties;
    }
}
