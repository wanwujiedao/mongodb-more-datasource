package com.dao.mongdb.config;

import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.net.UnknownHostException;

/**
 * 去掉默认存储的_class,节约内存
 *
 * @author 阿导
 * @version BUILD1001
 * @fileName com.bxm.datapark.service.mongo.mongo.Test.java
 * @CopyRright (c) 2017-bxm：万物皆导
 * @created 2018-01-05 11:39:00
 * @modifier 阿导
 * @updated 2018-01-05 11:39:00
 *
 */
@Configuration
public class MongoConfig {

    /**
     * 自动注入第一个 mongondb
     */
    @Value("${spring.data.mongodb.one.uri}")
    private String MONGO_URI;

    /**
     * 自动注入第二个 mongondb
     */
    @Value("${spring.data.mongodb.two.uri}")
    private String MONGO_URI2;


    // ===================== 连接到 mongodb 服务器 =================================


    @Bean
    @Primary
    public MongoMappingContext mongoMappingContext() {
        MongoMappingContext mappingContext = new MongoMappingContext();
        return mappingContext;
    }

    /**
     * 使用自定义的typeMapper去除写入mongodb时的“_class”字段
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Primary
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(this.dbFactory());
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, this.mongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

    @Bean
    @Primary
    public MongoDbFactory dbFactory() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_URI));
    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(dbFactory(), this.mappingMongoConverter());
    }

    // ==================== 连接到 mongodb2 服务器 ======================================
    @Bean
    public MongoMappingContext mongoMappingContext2() {
        MongoMappingContext mappingContext = new MongoMappingContext();
        return mappingContext;
    }

    /**
     * 使用自定义的typeMapper去除写入mongodb时的“_class”字段
     *
     * @return
     * @throws Exception
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter2() throws Exception {
        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(this.dbFactory2());
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, this.mongoMappingContext2());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

    @Bean
    public MongoDbFactory dbFactory2() throws UnknownHostException {
        return new SimpleMongoDbFactory(new MongoClientURI(MONGO_URI2));
    }

    @Bean
    public MongoTemplate mongoTemplate2() throws Exception {
        return new MongoTemplate(this.dbFactory2(), this.mappingMongoConverter2());
    }

}
