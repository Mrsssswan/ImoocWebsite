package mrsssswan.mall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateTimeUtil {

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date strToDate(String dataTimeFormat,String formatStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dataTimeFormat);
        return dateTime.toDate();
    }
    public static String datarToStr(String formatStr,Date date){
            if(date == null){
                return StringUtils.EMPTY;
            }
        DateTime dateTime = new DateTime(date);
            return dateTime.toString(formatStr);
    }

    public static Date strToDate(String dataTimeFormat){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dataTimeFormat);
        return dateTime.toDate();
    }
    public static String datarToStr(Date date){
        if(date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
}
