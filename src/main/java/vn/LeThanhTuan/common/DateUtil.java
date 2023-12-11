package vn.LeThanhTuan.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final String PATTERN_DATE = "yyyy-MM-dd";
    public static Date formatToDate(String dateString) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN_DATE);
        return simpleDateFormat.parse(dateString);
    }

    public static String formatToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN_DATE);
        return simpleDateFormat.format(date);
    }
}
