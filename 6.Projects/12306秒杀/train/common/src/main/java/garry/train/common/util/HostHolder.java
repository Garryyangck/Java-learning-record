package garry.train.common.util;

/**
 * @author Garry
 * 2024-09-13 20:29
 */

import garry.train.common.enums.ResponseEnum;
import garry.train.common.exception.BusinessException;
import garry.train.common.vo.MemberLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息，用于代替session对象
 * 该对象可以给调用它的每一个不同的线程，
 * 单独分配一个ThreadLocalMap用于set User。
 */
@Slf4j
@Component
public class HostHolder {
    private final ThreadLocal<MemberLoginVo> members = new ThreadLocal<>();

    public void setMember(MemberLoginVo vo) {
        members.set(vo);
    }

    public MemberLoginVo getMember() {
        return members.get();
    }

    public void remove() {
        members.remove();
    }

    public Long getMemberId() {
        try {
            return members.get().getId();
        } catch (Exception e) {
            log.error(ResponseEnum.MEMBER_THREAD_LOCAL_ERROR.getMsg());
            throw new BusinessException(ResponseEnum.MEMBER_THREAD_LOCAL_ERROR);
        }
    }
}
