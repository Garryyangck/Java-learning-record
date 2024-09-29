package garry.train.business.service;

import garry.train.business.pojo.Train;
import garry.train.business.vo.TrainQueryAllVo;
import garry.train.common.vo.PageVo;
import garry.train.business.form.TrainQueryForm;
import garry.train.business.form.TrainSaveForm;
import garry.train.business.vo.TrainQueryVo;

import java.util.List;

/**
 * @author Garry
 * 2024-09-22 13:19
 */
public interface TrainService {
    /**
     * 插入新车次，或修改已有的车次
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(TrainSaveForm form);

    /**
     * 根据 memberId 查询所有的车次
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<TrainQueryVo> queryList(TrainQueryForm form);

    /**
     * 根据 id(主键) 删除车次
     * 同时删除所有隶属于该车次的 TrainStation 和 TrainCarriage
     */
    void delete(Long id);

    /**
     * 返回所有车次编号、始发站、终点站、车次类型，用于前端的 train_station 填入车次编号时做下拉框
     */
    List<TrainQueryAllVo> queryAll();

    /**
     * 用于 service 之间调用
     */
    List<Train> selectAll();

    /**
     * 根据 code 查 train，主要被 service 层调用
     * 还可用于唯一键 code 的校验
     */
    List<Train> queryByCode(String code);

    /**
     * 根据 start 查 train
     * 用于 Station 删除时，同时删除作为始发站和终点站的对应 Train
     */
    List<Train> queryByStart(String start);

    /**
     * 根据 end 查 train
     * 用于 Station 删除时，同时删除作为始发站和终点站的对应 Train
     */
    List<Train> queryByEnd(String end);
}
