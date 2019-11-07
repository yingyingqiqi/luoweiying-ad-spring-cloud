package cn.luoweiying.index.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
//?
@Slf4j
public class CommonUtils {
    //在容器map中，没有key对应的value,则，在factory.get中取得一个新值，保存在map，并返回value。
    public static <K, V> V getOrCreate(K key, Map<K, V> map, Supplier<V> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }

    public static String stringConcat(String...args) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String arg : args) {
            stringBuffer.append(arg);
            stringBuffer.append("-");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    //    Tue Jan 01 08:00:00 CST 2019
    public static Date parseStringToDate(String datestring) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EE MMM dd HH:mm:ss zzz yyy", Locale.US);
        try {
           return DateUtils.addHours(simpleDateFormat.parse(datestring),-8);
        } catch (ParseException e) {
            log.error("parseStringToDate error:{}", datestring);
            return null;
        }
    }
}
