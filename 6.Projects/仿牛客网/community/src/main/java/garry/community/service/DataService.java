package garry.community.service;

import java.util.Date;

/**
 * @author Garry
 * ---------2024/3/29 21:03
 **/
public interface DataService {
    /**
     * 将ip存入UV中
     *
     * @param ip
     */
    void recordUV(String ip);

    /**
     * 统计从fromDate到toDate的UV
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    long calculateUV(Date fromDate, Date toDate);

    /**
     * 将指定用户存到DAU中
     *
     * @param userId
     */
    void recordDAU(Integer userId);

    /**
     * 统计从fromDate到toDate的DAU
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    long calculateDAU(Date fromDate, Date toDate);
}
