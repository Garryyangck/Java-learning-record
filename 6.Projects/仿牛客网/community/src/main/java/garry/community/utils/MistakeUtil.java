package garry.community.utils;

/**
 * @author Garry
 * ---------2024/3/17 11:19
 **/

import garry.community.enums.MistakeEnum;

import java.util.Map;

/**
 * 处理给前端返回的信息的方法类
 */
public class MistakeUtil {

    /**
     * 为传入的responseMap添加新的response(即<String, Object>键值对)
     *
     * @param responseMap
     * @param mistakeEnum
     */
    public static void addMistake(Map<String, Object> responseMap, MistakeEnum mistakeEnum) {
        responseMap.put(mistakeEnum.getType(), mistakeEnum.getDesc());
    }
}
