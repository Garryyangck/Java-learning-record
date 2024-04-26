package garry.community.utils;

import garry.community.CommunityApplicationTests;
import garry.community.consts.CommunityConst;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author Garry
 * ---------2024/3/17 10:24
 **/
@Slf4j
public class CommunityUtilTest extends CommunityApplicationTests {

    @Test
    public void generateUUID() {
        String str = CommunityUtil.generateUUID();
        log.info("str = {}", str);
    }

    @Test
    public void md5Encryption() {
        String password = "garry";
        String withoutSalt = CommunityUtil.md5Encryption(password);
        log.info("withoutSalt = {}", withoutSalt);

        String salt = CommunityUtil.generateUUID()
                .substring(0, CommunityConst.SALT_LENGTH);
        log.info("salt = {}", salt);

        String withSalt = CommunityUtil.md5Encryption(password + salt);
        log.info("withSalt = {}", withSalt);
    }
}