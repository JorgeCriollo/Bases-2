package Data.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {
    private static final String STANDARD_FORMAT = "dd/MM/yyyy";

    public static String formatDate(Calendar date) {
        return formatDate(null, date);
    }

    public static String formatDate(String format, Calendar date) {
        String f;

        if (StringUtils.isBlank((f = format)))
            f = STANDARD_FORMAT;

        return new SimpleDateFormat(f, Locale.US).format(date.getTime());
    }
}
