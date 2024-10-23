package garry.train.business.service;

import garry.train.business.pojo.Station;
import garry.train.common.vo.PageVo;
import garry.train.business.form.StationQueryForm;
import garry.train.business.form.StationSaveForm;
import garry.train.business.vo.StationQueryVo;

import java.util.List;

/**
 * @author Garry
 * 2024-09-19 20:52
 */
public interface StationService {
    /**
     * 插入新车站，或修改已有的车站
     * 如果 form.id = null，则为插入；
     * 如果 form.id != null，则为修改
     */
    void save(StationSaveForm form);

    /**
     * 根据 memberId 查询所有的车站
     * 如果 form.memberId = null，则为管理员查询
     */
    PageVo<StationQueryVo> queryList(StationQueryForm form);

    /**
     * 根据 id(主键) 删除车站
     * 同时删除作为始发站和终点站的对应 Train
     */
    void delete(Long id);

    /**
     * 返回所有车站的名称，用于 train 插入时选择
     */
    List<StationQueryVo> queryAll();

    /**
     * 增删改操作后更新 redis 缓存
     */
    List<StationQueryVo> queryAllRefreshCache();

    /**
     * 根据 Name 查询 Station
     * 主要用于校验唯一键约束
     */
    List<Station> queryByName(String name);
}
