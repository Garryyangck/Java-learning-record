package garry.springmvc.converter;


import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/3 19:55
 **/
public class MyDateConverter implements Converter<String, Date> {
    public Date convert(String s) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(s);
        } catch (ParseException e) {
            System.out.println("===========日期转换失败===========");
            throw new RuntimeException("日期转换失败");
        }

    }
}
