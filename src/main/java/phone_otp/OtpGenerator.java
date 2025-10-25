package phone_otp;

import java.security.SecureRandom;

public class OtpGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DEFAULT_OTP_LENGTH = 6;

    public String generate(Integer otpLength) {
        int length = (otpLength != null && otpLength > 0) ? otpLength : DEFAULT_OTP_LENGTH;
        int bound = (int) Math.pow(10, length);
        int otp = RANDOM.nextInt(bound);
        return String.format("%0" + length + "d", otp);
    }
}
