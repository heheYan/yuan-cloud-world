package com.yuan.cloud.system.service;

import com.yuan.cloud.system.entity.User;
import com.yuan.cloud.system.query.UserQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

/**
 * Description
 *
 * @author Mr.Y
 * Created on 2025-08-06 20:03
 */
@SpringBootTest
class UserServiceTest {

    @Autowired
    IUserService service;

    @Test
    void testSave() {
        User user = new User();
        user.setUsername("yuan3");
        user.setPwd("123456");
        user.setNickName("yuan");
        user.setMobile("12345678901");
        service.save(user);
    }

    @Test
    void testFind() {
        UserQuery query = new UserQuery();
        query.setNickName("ay");
        Page<User> page = service.page(0, 10, query);
        System.out.println(page.getContent());
        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
    }

}