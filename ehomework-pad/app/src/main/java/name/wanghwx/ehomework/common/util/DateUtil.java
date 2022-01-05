package name.wanghwx.ehomework.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil{

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm",Locale.CHINA);

    public static String format(Date date){
        return date==null?"":simpleDateFormat.format(date);
    }

}