package garry.train.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import garry.train.member.form.MemberRegisterForm;
import garry.train.member.mapper.MemberMapper;
import garry.train.member.pojo.Member;
import garry.train.member.pojo.MemberExample;
import garry.train.member.service.MemberService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Garry
 * 2024-09-05 16:41
 */
@Service
public class MemberServiceImpl implements MemberService {
    @Resource
    private MemberMapper memberMapper;

    @Override
    public int count() {
        return (int) memberMapper.countByExample(null);
    }

    @Override
    public long register(MemberRegisterForm form) {
        // 检验mobile是否重复
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria() // 创建条件
                .andMobileEqualTo(form.getMobile()); // and意为“且”，即并且mobile="mobile"
        List<Member> members = memberMapper.selectByExample(memberExample);

        if (CollUtil.isNotEmpty(members)) {
            throw new RuntimeException("手机号已注册");
        }

        Member member = new Member();
        member.setId(System.currentTimeMillis());
        member.setMobile(form.getMobile());

        memberMapper.insert(member);
        return member.getId();
    }
}
