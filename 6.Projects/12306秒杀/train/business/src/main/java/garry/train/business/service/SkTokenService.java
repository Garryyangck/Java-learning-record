package garry.train.business.service;

import garry.train.business.form.SkTokenQueryForm;
import garry.train.business.form.SkTokenSaveForm;
import garry.train.business.pojo.SkToken;
import garry.train.business.pojo.Train;
import garry.train.business.vo.SkTokenQueryVo;
import garry.train.common.vo.PageVo;

import java.util.Date;
import java.util.List;

/**
 * @author Garry
 * 2024-11-05 18:16
 */
public interface SkTokenService {
    /**
     * 插入新秒杀令牌，或修改已有的秒杀令牌
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(SkTokenSaveForm form);

    /**
     * 根据 memberId 查询所有的秒杀令牌
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<SkTokenQueryVo> queryList(SkTokenQueryForm form);

    /**
     * 根据 id(主键) 删除秒杀令牌
     */
    void delete(Long id);

    /**
     * 生成 date 日，train 下的所有 sk-token
     */
    void genDaily(Date date, Train train);

    /**
     * 一次性查出 date, trainCode 下的 sk-token
     */
    List<SkToken> queryByDateAndTrainCode(Date date, String trainCode);

    /**
     * 扣减令牌，并防止机器人刷票
     */
    boolean validSkToken(Date date, String trainCode, Long memberId);
}
