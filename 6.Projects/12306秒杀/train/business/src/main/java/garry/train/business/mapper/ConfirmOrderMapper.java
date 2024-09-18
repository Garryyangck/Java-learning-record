package garry.train.business.mapper;

import garry.train.business.pojo.ConfirmOrder;
import garry.train.business.pojo.ConfirmOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ConfirmOrderMapper {
    long countByExample(ConfirmOrderExample example);

    int deleteByExample(ConfirmOrderExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ConfirmOrder record);

    int insertSelective(ConfirmOrder record);

    List<ConfirmOrder> selectByExample(ConfirmOrderExample example);

    ConfirmOrder selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ConfirmOrder record, @Param("example") ConfirmOrderExample example);

    int updateByExample(@Param("record") ConfirmOrder record, @Param("example") ConfirmOrderExample example);

    int updateByPrimaryKeySelective(ConfirmOrder record);

    int updateByPrimaryKey(ConfirmOrder record);
}