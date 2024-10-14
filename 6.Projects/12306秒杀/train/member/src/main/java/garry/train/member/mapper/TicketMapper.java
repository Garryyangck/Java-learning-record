package garry.train.member.mapper;

import garry.train.member.pojo.Ticket;
import garry.train.member.pojo.TicketExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TicketMapper {
    long countByExample(TicketExample example);

    int deleteByExample(TicketExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Ticket record);

    int insertSelective(Ticket record);

    List<Ticket> selectByExample(TicketExample example);

    Ticket selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Ticket record, @Param("example") TicketExample example);

    int updateByExample(@Param("record") Ticket record, @Param("example") TicketExample example);

    int updateByPrimaryKeySelective(Ticket record);

    int updateByPrimaryKey(Ticket record);
}