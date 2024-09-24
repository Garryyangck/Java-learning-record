package garry.train.business.service;

import garry.train.business.form.TrainCarriageQueryForm;
import garry.train.business.form.TrainCarriageSaveForm;
import garry.train.business.pojo.TrainCarriage;
import garry.train.business.vo.TrainCarriageQueryVo;
import garry.train.common.vo.PageVo;

import java.util.List;

/**
 * @author Garry
 * 2024-09-22 20:48
 */
public interface TrainCarriageService {
    /**
     * 插入新火车车厢，或修改已有的火车车厢
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(TrainCarriageSaveForm form);

    /**
     * 根据 memberId 查询所有的火车车厢
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<TrainCarriageQueryVo> queryList(TrainCarriageQueryForm form);

    /**
     * 根据 id(主键) 删除火车车厢
     */
    void delete(Long id);

    /**
     * 根据 trainCode 查 TrainCarriage，主要被 service 层调用
     */
    List<TrainCarriage> selectByTrainCode(String trainCode);

    /**
     * 通过 trainCode 和 index 查询
     * 可用于对唯一键 train_code_index_unique 的校验
     */
    List<TrainCarriage> queryByTrainCodeAndIndex(String trainCode, Integer index);
}
