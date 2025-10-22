package phone_otp;

public class OtpGenerator {
    public String generate() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }
}
