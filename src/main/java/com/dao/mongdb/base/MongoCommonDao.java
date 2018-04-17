package com.dao.mongdb.base;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;


/**
 * @author 阿导
 * @version BUILD1001
 * @fileName com.dao.base.mongoutil.Page.java
 * @CopyRright (c) 2017-bxm：万物皆导
 * @created 2018-01-05 16:03:00
 * @modifier 阿导
 * @updated 2018-01-05 16:03:00
 * @description
 */
public abstract class MongoCommonDao<T> {

    /**
     * 默认分页信息
     */
    private static final int DEFAULT_SKIP = 0;
    private static final int DEFAULT_LIMIT = 100;

    /**
     * spring-mongodb　集成操作类
     * <p>
     * 作为一个工具类，防止注入失败导致无法解决后续问题，所以由子类自动注入
     */
    protected MongoTemplate mongoTemplate;

    /**
     * 通过条件查询实体(集合)， 只能指定条件，集合名默认为实体类
     *
     * @param query
     * @return java.util.List<T>
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public List<T> find(Query query) {
        return mongoTemplate.find(query, this.getEntityClass());
    }

    /**
     * 通过条件查询实体(集合)，可以指定条件和集合名
     *
     * @param query
     * @param collectionName
     * @return java.util.List<T>
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public List<T> find(Query query, String collectionName) {
        return mongoTemplate.find(query, this.getEntityClass(), collectionName);
    }

    /**
     * 通过条件查询实体(集合)，可以指定条件和集合名以及返回集合实体类
     *
     * @param query
     * @param clazz
     * @param collectionName
     * @return java.util.List<T>
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public List<?> find(Query query, Class<?> clazz, String collectionName) {
        return mongoTemplate.find(query, clazz, collectionName);
    }

    /**
     * 通过一定的条件查询一个实体，只能指定条件，集合名默认为实体类
     *
     * @param query
     * @return T
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public T findOne(Query query) {
        return mongoTemplate.findOne(query, this.getEntityClass());
    }

    /**
     * 可以指定条件和集合名以及返回集合实体类
     *
     * @param query
     * @param collectionName
     * @return T
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public T findOne(Query query, String collectionName) {
        return mongoTemplate.findOne(query, this.getEntityClass(), collectionName);
    }

    /**
     * 可以指定条件和集合名以及返回集合实体类
     *
     * @param query
     * @param clazz
     * @param collectionName
     * @return java.lang.Object
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public Object findOne(Query query, Class<?> clazz, String collectionName) {
        return mongoTemplate.findOne(query, clazz, collectionName);
    }

    /**
     * 通过条件查询更新数据，
     *
     * @param query
     * @param update
     * @return void
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public void update(Query query, Update update) {
        mongoTemplate.findAndModify(query, update, this.getEntityClass());
    }

    /**
     * 通过条件查询更新数据,可以指定集合名
     *
     * @param query
     * @param update
     * @param collectionName
     * @return void
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public void update(Query query, Update update, String collectionName) {
        mongoTemplate.findAndModify(query, update, this.getEntityClass(), collectionName);
    }

    /**
     * 通过条件查询更新数据，可以指定集合名和类名（主要获取其属性名称）
     *
     * @param query
     * @param update
     * @param clazz
     * @param collectionName
     * @return void
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public void update(Query query, Update update, Class<?> clazz, String collectionName) {
        mongoTemplate.findAndModify(query, update, clazz, collectionName);
    }

    /**
     * 保存一个对象到mongodb
     *
     * @param entity
     * @return T
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public T save(T entity) {
        mongoTemplate.insert(entity);
        return entity;
    }

    /**
     * 保存一个对象到mongodb(可以指定存储到的集合名称)
     *
     * @param entity
     * @param collectionName
     * @return T
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public T save(T entity, String collectionName) {
        mongoTemplate.insert(entity, collectionName);
        return entity;
    }

    /**
     * 批量新增
     *
     * @param t
     * @param collectionName
     * @return void
     * @author 阿导
     * @time 2018/1/9
     * @CopyRight 杭州微财网络科技有限公司
     */
    public void saveBatch(List<T> t, String collectionName) {
        mongoTemplate.insert(t, collectionName);
    }

    /**
     * 按条件删除
     *
     * @param query
     * @param collectionName
     * @return void
     * @author 阿导
     * @time 2018/1/9
     * @CopyRight 杭州微财网络科技有限公司
     */
    public void remove(Query query, String collectionName) {
        if (mongoTemplate.exists(query, collectionName)) {
            mongoTemplate.remove(query, collectionName);
        }
    }

    /**
     * 删除数据集
     *
     * @author 阿导
     * @time 2018/1/9
     * @CopyRight 杭州微财网络科技有限公司
     * @param collectionName
     * @return void
     */
    public void drop(String collectionName) {
        mongoTemplate.dropCollection(collectionName);
    }


    /**
     * 通过ID获取记录
     *
     * @param id
     * @return T
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public T findById(String id) {
        return mongoTemplate.findById(id, this.getEntityClass());
    }

    /**
     * 通过ID获取记录,并且指定了集合名(表的意思)
     *
     * @param id
     * @param collectionName
     * @return T
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public T findById(String id, String collectionName) {
        return mongoTemplate.findById(id, this.getEntityClass(), collectionName);
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param query
     * @return com.dao.base.mongoutil.Page<T>
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public Page<T> findPage(Integer pageNum, Integer pageSize, Query query) {
        //获取分页总数
        Long total = this.count(query);

        //分页信息处理
        if (pageNum == null || pageSize == null) {
            query.skip(DEFAULT_SKIP);
        } else {
            query.skip((pageNum - 1) * pageSize);
        }
        if (pageSize == null) {
            query.limit(DEFAULT_LIMIT);
        } else {
            query.limit(pageSize);
        }
        //将查询的数据设置进集合
        return new Page<T>(this.find(query), pageNum, pageSize, total);
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param query
     * @param collectionName
     * @return com.dao.base.mongoutil.Page<T>
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */
    public Page<T> findPage(Integer pageNum, Integer pageSize, Query query,String collectionName) {
        //获取分页总数
        Long total = this.count(query,collectionName);

        //分页信息处理
        if (pageNum == null || pageSize == null) {
            query.skip(DEFAULT_SKIP);
        } else {
            query.skip((pageNum - 1) * pageSize);
        }
        if (pageSize == null) {
            query.limit(DEFAULT_LIMIT);
        } else {
            query.limit(pageSize);
        }
        //将查询的数据设置进集合
        return new Page<T>(this.find(query,collectionName), pageNum, pageSize, total);
    }
    /**
     * @param query
     * @return long
     * @description
     * @author 阿导
     * @time 2018-01-05
     * @CopyRight 杭州微财网络科技有限公司
     */

    public long count(Query query) {
        return mongoTemplate.count(query, this.getEntityClass());
    }
    public long count(Query query,String collectionName) {
        return mongoTemplate.count(query, this.getEntityClass(),collectionName);
    }

    /**
     * 分组统计
     *
     * @param collectionName 数据集名称
     * @param match          条件注入
     * @param group          分组信息
     * @return java.util.List<T>
     * @author 阿导
     * @time 2018/1/6
     * @CopyRight 杭州微财网络科技有限公司
     */
    public List<T> sum(String collectionName, MatchOperation match, GroupOperation group) {
        // Aggregation.match(Criteria.where("oid").is(orderId))
        // Aggregation.group("userName").sum("flowSize").as("tatoalFlowSize").sum("amount").as("totalAmount")
        Aggregation aggregation = Aggregation.newAggregation(match, group);
        AggregationResults<T> aggRes = mongoTemplate.aggregate(aggregation,
                collectionName, this.getEntityClass());
        return aggRes.getMappedResults();
    }

//    public Page<T> pageSum(String collectionName, MatchOperation match, GroupOperation group, SortOperation sort, Integer pageNum, Integer pageSize){
//        Aggregation aggregation = Aggregation.newAggregation(match,group,sort, Aggregation.skip(pageNum>1?(pageNum-1)*pageSize:0),Aggregation.limit(pageSize));
//        AggregationResults<T> aggRes = mongoTemplate.aggregate(aggregation,collectionName, this.getEntityClass());
//        //将查询的数据设置进集合
//        return new Page<T>(aggRes.getMappedResults()  , pageNum,pageSize,);
//    }


    /**
     * 获取需要操作的实体类class
     *
     * @return
     */
    private Class<T> getEntityClass() {
        return ReflectionUtils.getSuperClassGenricType(getClass());
    }

    /**
     * 注入mongodbTemplate
     *
     * @param mongoTemplate
     */
    protected abstract void setMongoTemplate(MongoTemplate mongoTemplate);

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }
}
