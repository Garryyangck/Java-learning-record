package garry.train.business.mapper;

import garry.train.business.pojo.SkToken;
import garry.train.business.pojo.SkTokenExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SkTokenMapper {
    long countByExample(SkTokenExample example);

    int deleteByExample(SkTokenExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SkToken record);

    int insertSelective(SkToken record);

    List<SkToken> selectByExample(SkTokenExample example);

    SkToken selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SkToken record, @Param("example") SkTokenExample example);

    int updateByExample(@Param("record") SkToken record, @Param("example") SkTokenExample example);

    int updateByPrimaryKeySelective(SkToken record);

    int updateByPrimaryKey(SkToken record);
}