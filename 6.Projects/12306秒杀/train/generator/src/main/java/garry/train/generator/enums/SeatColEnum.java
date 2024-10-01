package garry.train.generator.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * @author Garry
 * 2024-09-22 21:17
 */
public enum SeatColEnum {

    YDZ_A("A", "A", "1"),

    YDZ_C("C", "C", "1"),

    YDZ_D("D", "D", "1"),

    YDZ_F("F", "F", "1"),

    EDZ_A("A", "A", "2"),

    EDZ_B("B", "B", "2"),

    EDZ_C("C", "C", "2"),

    EDZ_D("D", "D", "2"),

    EDZ_F("F", "F", "2");

    private final String code;

    private final String desc;

    /**
     * 对应 SeatTypeEnum.code
     */
    private final String type;

    SeatColEnum(String code, String desc, String type) {
        this.code = code;
        this.desc = desc;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    /**
     * 根据车箱的座位类型，筛选出所有的列，比如车箱类型是一等座，则筛选出 columnList={ACDF}
     */
    public static List<SeatColEnum> getColsByType(String seatType) {
        List<SeatColEnum> colList = new ArrayList<>();
        EnumSet<SeatColEnum> seatColEnums = EnumSet.allOf(SeatColEnum.class);
        for (SeatColEnum anEnum : seatColEnums) {
            if (seatType.equals(anEnum.getType())) {
                colList.add(anEnum);
            }
        }
        return colList;
    }
}
