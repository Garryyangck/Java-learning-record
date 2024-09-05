package garry.train.member.service.impl;

import garry.train.member.mapper.MemberMapper;
import garry.train.member.service.MemberService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author Garry
 * 2024-09-05 16:41
 */
@Service
public class MemberServiceImpl implements MemberService {
    @Resource
    private MemberMapper memberMapper;

    @Override
    public int getMemberCount() {
        return memberMapper.count();
    }
}
