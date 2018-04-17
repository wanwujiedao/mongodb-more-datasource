package com.dao.mongdb.dao;

import com.dao.mongdb.base.MongoCommonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 阿导
 * @version 1.0
 * @fileName com.dao.mongdb.dao.UserDao.java
 * @CopyRright (c) 2018-万物皆导
 * @created 2018-04-17 19:08:00
 */
@Component
public class UserDao extends MongoCommonDao<User> {

    /**
     * 注入mongodbTemplate
     *
     * @param mongoTemplate
     */
    @Autowired
    @Qualifier("mongoTemplate")
    @Override
    protected void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate=mongoTemplate;
    }
}
