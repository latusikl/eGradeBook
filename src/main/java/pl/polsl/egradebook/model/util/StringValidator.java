package pl.polsl.egradebook.model.util;

public class StringValidator {
    public static boolean validateDate(String date) {
        if (date == null)
            return false;
        return date.compareTo("2050-01-01") < 0 && date.compareTo("1999-12-12") > 0;
    }
}
