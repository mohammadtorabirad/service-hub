package ir.co.kishsys.sanhabinquiry.utils;

public class SecurityUtils {

    public static String authorisation(String userName, String password) {
        String credential = userName + ":" + password;
        byte[] encodedUsernamePassword = org.apache.commons.codec.binary.Base64.encodeBase64(credential.getBytes());
        return "Basic ".concat(new String(encodedUsernamePassword));
    }
}
