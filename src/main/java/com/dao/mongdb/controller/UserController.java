package com.dao.mongdb.controller;

import com.dao.mongdb.dao.User;
import com.dao.mongdb.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 直接使用控制层吧
 *
 * @author 阿导
 * @version 1.0
 * @fileName com.dao.mongdb.controller.UserController.java
 * @CopyRright (c) 2018-万物皆导
 * @created 2018-04-17 19:23:00
 */
@RestController
public class UserController {

    @Autowired
    UserDao userDao;

    @RequestMapping("/find")
    public List<User> findUser(){

        return userDao.find(new Query(),"dao");
    }
}
