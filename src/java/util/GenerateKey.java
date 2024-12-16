package util;

import java.util.Locale;

public class GenerateKey {

    public static String genetate(String firstName, String lastName) {
        String name = firstName + lastName;
        String namekey = name.substring(2, 5);
        String upperCase = name.substring(0, 2).toUpperCase(Locale.ITALY);

        return String.valueOf((int)(Math.random()*1000000000))+ namekey + upperCase ;
    }

    public static String genetate(String name) {
        String namekey = name.substring(2, 5);
        String upperCase = name.substring(0, 2).toUpperCase(Locale.ITALY);

        return String.valueOf((int)(Math.random()*1000000000))+ namekey + upperCase ;
    }
}
