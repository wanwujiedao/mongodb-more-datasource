package com.dao.mongdb.dao;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author 阿导
 * @version 1.0
 * @fileName com.dao.mongdb.dao.User.java
 * @CopyRright (c) 2018-万物皆导
 * @created 2018-04-17 19:05:00
 */
public class User {
    @Field("name")
    private String name;
    @Field("sex")
    private String sex;
    @Field("age")
    private Integer age;
    @Field("score")
    private Double score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
