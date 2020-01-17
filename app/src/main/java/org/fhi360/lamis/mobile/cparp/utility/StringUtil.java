package org.fhi360.lamis.mobile.cparp.utility;

/**
 * Created by aalozie on 3/16/2017.
 */

public class StringUtil {

    public static String captalize(String string )
    {
        String firstLetter = string.substring(0,1).toUpperCase();
        String restLetters = string.substring(1).toLowerCase();
        return firstLetter + restLetters;
    }

    public static boolean found(String searchFor, String stringPattern) {
        boolean found = false;
        String[] strings = stringPattern.split("#");
        for(String string : strings) {
            if (string.equalsIgnoreCase(searchFor.toLowerCase())) {
                found = true;
            }
        }
        return found;
    }

}
