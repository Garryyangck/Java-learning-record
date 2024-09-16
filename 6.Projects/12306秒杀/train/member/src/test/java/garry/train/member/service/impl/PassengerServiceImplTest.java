package garry.train.member.service.impl;

import garry.train.common.util.CommonUtil;
import garry.train.member.config.MemberApplicationTest;
import garry.train.member.form.PassengerSaveForm;
import garry.train.member.service.PassengerService;
import jakarta.annotation.Resource;
import org.junit.Test;

import java.util.Random;

/**
 * @author Garry
 * 2024-09-16 17:57
 */

public class PassengerServiceImplTest extends MemberApplicationTest {
    @Resource
    private PassengerService passengerService;

    @Test
    public void save() {
        for (int i = 0; i < 20; i++) {
            PassengerSaveForm form = new PassengerSaveForm();
            form.setMemberId(1833041335083470848L);
            form.setName("占位" + CommonUtil.generateUUID(6));
            form.setIdCard(CommonUtil.generateUUID(18));
            form.setType(String.valueOf(new Random().nextInt(3) + 1));
            passengerService.save(form);
        }
    }

    @Test
    public void queryList() {
    }
}