package Data.Utils;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.regex.Pattern;

public class StringUtils {
    public static String stripAccents(String input) {
        input = Normalizer.normalize(input, Normalizer.Form.NFD);
        input = input.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        return input;
    }

    public static String toFilename(String name, String extension) {
        String temp = stripAccents(name.trim());
        if (!temp.endsWith('.' + extension))
            temp += '.' + extension;

        return temp;
    }

    public static boolean containsIgnoreCase(String string, String substring) {
        String s1 = string.toLowerCase(), s2 = substring.toLowerCase();

        return s1.contains(s2);
    }

    public static boolean validEmail(String email) {
        return Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+[.][a-z]+$").matcher(email).matches();
    }

    public static boolean isBlank(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static boolean areBlank(String[] strings) {
        for (String s: strings)
            if (!s.trim().isEmpty())
                return false;

        return strings.length == 0;
    }

    public static String getLongest(String [] strings) {
        String temp = "";
        for (String s: strings)
            if (s.length() > temp.length())
                temp = s;

        return temp;
    }

    public static String formatCurrency(double value) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(value);
    }
}
