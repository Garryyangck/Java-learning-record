package garry.mall.service.impl;

import garry.mall.dao.UserMapper;
import garry.mall.enums.ResponseEnum;
import garry.mall.enums.RoleEnum;
import garry.mall.pojo.User;
import garry.mall.service.IUserService;
import garry.mall.vo.ResponseVo;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author Garry
 * ---------2024/3/6 19:24
 **/
@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public ResponseVo<User> register(User user) {
        //用户名不能重复
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if (countByUsername > 0) return ResponseVo.error(ResponseEnum.USERNAME_EXIST);

        //邮箱不能重复
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if (countByEmail > 0) return ResponseVo.error(ResponseEnum.EMAIL_EXIST);

        //注册用户为普通用户
        user.setRole(RoleEnum.CUSTOMER.getCode());

        //密码用MD5摘要算法(Spring自带)
        user.setPassword(DigestUtils.md5DigestAsHex(
                user.getPassword().getBytes(StandardCharsets.UTF_8)));

        //写入数据库
        int count = userMapper.insertSelective(user);
        //服务端错误
        if (count == 0) return ResponseVo.error(ResponseEnum.ERROR);

        return ResponseVo.success();
    }

    @Override
    public ResponseVo<User> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) return ResponseVo.error(ResponseEnum.USER_NOT_FOUND);

        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!user.getPassword().equals(md5Password)) return ResponseVo.error(ResponseEnum.PASSWORD_ERROR);

        user.setPassword(null);
        return ResponseVo.success(user);
    }
}
