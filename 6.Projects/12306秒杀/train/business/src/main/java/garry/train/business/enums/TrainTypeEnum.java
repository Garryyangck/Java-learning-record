package garry.train.business.enums;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

/**
 * @author Garry
 * 2024-09-22 13:27
 */
public enum TrainTypeEnum {

    G("G", "高铁", new BigDecimal("1.2")),

    D("D", "动车", new BigDecimal("1")),

    K("K", "快速", new BigDecimal("0.8"))
    ;

    private final String code;

    private final String desc;

    /**
     * 票价比例，例：1.1，则票价 = 1.1 * 每公里单价（SeatTypeEnum.price） * 公里（station.km）
     */
    private final BigDecimal priceRate;

    TrainTypeEnum(String code, String desc, BigDecimal priceRate) {
        this.code = code;
        this.desc = desc;
        this.priceRate = priceRate;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public BigDecimal getPriceRate() {
        return priceRate;
    }

    public static List<HashMap<String, String>> getEnumList() {
        List<HashMap<String, String>> list = new ArrayList<>();
        for (TrainTypeEnum anEnum : EnumSet.allOf(TrainTypeEnum.class)) {
            HashMap<String, String> map = new HashMap<>();
            map.put("code", anEnum.code);
            map.put("desc", anEnum.desc);
            list.add(map);
        }
        return list;
    }
}
