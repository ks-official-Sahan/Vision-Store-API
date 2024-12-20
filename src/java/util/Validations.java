package util;

public class Validations {

    public static boolean isEmailValid(String email) {

        return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }

    public static boolean isPasswordValid(String password) {

        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@#$%^&+=]).{8,}$");

    }

    public static boolean isInteger(String value) {

        try {
            int intValue = Integer.valueOf(value);

            return true;

        } catch (Exception e) {

            return false;
        }

    }

    public static boolean VALIDATE_MOBILE(String mobile) {
        return mobile.matches("^07[01245678]{1}[0-9]{7}$");
    }

}
