# mongodb 多数据源配置

***

- 配置文件

> **application.properties**

```markdown
        
    # mongdb1
    spring.data.mongodb.one.uri=mongodb://localhost:27017/dao
    
    
    
    # mongdb2
    spring.data.mongodb.two.uri=mongodb://localhost:27017/Xu?replicaSet=mgset-5031831
    
    #mongodb://[username:password@]host1[:port1][,host2[:port2],…[,hostN[:portN]]][/[database][?options]]


```

- 配置代码，设置动态数据源

> **MongoConfig.java**

```markdown
    
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



```

- 抽象类做原型，复用数据访问层

> **MongoCommonDao.java**

```markdown
    
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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
         * @CopyRight 万物皆导
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



```

- 反射工具类

**ReflectionUtils.java**

```markdown
    
    package com.dao.mongdb.base;
    
    import org.apache.commons.lang3.StringUtils;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.util.Assert;
    
    import java.lang.reflect.*;
    
    /** 
     * 反射工具类. 
     *  
     * 提供访问私有变量,获取泛型类型Class, 提取集合中元素的属性, 转换字符串到对象等Util函数. 
     *  
     */  
    public class ReflectionUtils {  
      
        private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);
      
        /** 
         * 调用Getter方法. 
         */  
        public static Object invokeGetterMethod(Object obj, String propertyName) {  
            String getterMethodName = "get" + StringUtils.capitalize(propertyName);
            return invokeMethod(obj, getterMethodName, new Class[] {}, new Object[] {});  
        }  
      
        /** 
         * 调用Setter方法.使用value的Class来查找Setter方法. 
         */  
        public static void invokeSetterMethod(Object obj, String propertyName, Object value) {  
            invokeSetterMethod(obj, propertyName, value, null);  
        }  
      
        /** 
         * 调用Setter方法. 
         *  
         * @param propertyType 用于查找Setter方法,为空时使用value的Class替代. 
         */  
        public static void invokeSetterMethod(Object obj, String propertyName, Object value, Class<?> propertyType) {  
            Class<?> type = propertyType != null ? propertyType : value.getClass();  
            String setterMethodName = "set" + StringUtils.capitalize(propertyName);  
            invokeMethod(obj, setterMethodName, new Class[] { type }, new Object[] { value });  
        }  
      
        /** 
         * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数. 
         */  
        public static Object getFieldValue(final Object obj, final String fieldName) {  
            Field field = getAccessibleField(obj, fieldName);  
      
            if (field == null) {  
                throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");  
            }  
      
            Object result = null;  
            try {  
                result = field.get(obj);  
            } catch (IllegalAccessException e) {  
                logger.error("不可能抛出的异常{}", e.getMessage());  
            }  
            return result;  
        }  
      
        /** 
         * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数. 
         */  
        public static void setFieldValue(final Object obj, final String fieldName, final Object value) {  
            Field field = getAccessibleField(obj, fieldName);  
      
            if (field == null) {  
                throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");  
            }  
      
            try {  
                field.set(obj, value);  
            } catch (IllegalAccessException e) {  
                logger.error("不可能抛出的异常:{}", e.getMessage());  
            }  
        }  
      
        /** 
         * 循环向上转型, 获取对象的DeclaredField,   并强制设置为可访问. 
         *  
         * 如向上转型到Object仍无法找到, 返回null. 
         */  
        public static Field getAccessibleField(final Object obj, final String fieldName) {  
            Assert.notNull(obj, "object不能为空");
            Assert.hasText(fieldName, "fieldName");
            for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {  
                try {  
                    Field field = superClass.getDeclaredField(fieldName);  
                    field.setAccessible(true);  
                    return field;  
                } catch (NoSuchFieldException e) {//NOSONAR  
                    // Field不在当前类定义,继续向上转型  
                }  
            }  
            return null;  
        }  
      
        /** 
         * 直接调用对象方法, 无视private/protected修饰符. 
         * 用于一次性调用的情况. 
         */  
        public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,  
                final Object[] args) {  
            Method method = getAccessibleMethod(obj, methodName, parameterTypes);  
            if (method == null) {  
                throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");  
            }  
      
            try {  
                return method.invoke(obj, args);  
            } catch (Exception e) {  
                throw convertReflectionExceptionToUnchecked(e);  
            }  
        }  
      
        /** 
         * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 
         * 如向上转型到Object仍无法找到, 返回null. 
         *  
         * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args) 
         */  
        public static Method getAccessibleMethod(final Object obj, final String methodName,  
                final Class<?>... parameterTypes) {  
            Assert.notNull(obj, "object不能为空");
      
            for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {  
                try {  
                    Method method = superClass.getDeclaredMethod(methodName, parameterTypes);  
      
                    method.setAccessible(true);  
      
                    return method;  
      
                } catch (NoSuchMethodException e) {//NOSONAR  
                    // Method不在当前类定义,继续向上转型  
                }  
            }  
            return null;  
        }  
      
        /** 
         * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 
         * 如无法找到, 返回Object.class. 
         * eg. 
         * public UserDao extends HibernateDao<User> 
         * 
         * @param clazz The class to introspect 
         * @return the first generic declaration, or Object.class if cannot be determined 
         */  
        @SuppressWarnings({ "unchecked", "rawtypes" })  
        public static <T> Class<T> getSuperClassGenricType(final Class clazz) {  
            return getSuperClassGenricType(clazz, 0);  
        }  
      
        /** 
         * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 
         * 如无法找到, 返回Object.class. 
         *  
         * 如public UserDao extends HibernateDao<User,Long> 
         * 
         * @param clazz clazz The class to introspect 
         * @param index the Index of the generic ddeclaration,start from 0. 
         * @return the index generic declaration, or Object.class if cannot be determined 
         */  
        @SuppressWarnings("rawtypes")  
        public static Class getSuperClassGenricType(final Class clazz, final int index) {  
      
            Type genType = clazz.getGenericSuperclass();  
      
            if (!(genType instanceof ParameterizedType)) {  
                logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");  
                return Object.class;  
            }  
      
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
      
            if (index >= params.length || index < 0) {  
                logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "  
                        + params.length);  
                return Object.class;  
            }  
            if (!(params[index] instanceof Class)) {  
                logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");  
                return Object.class;  
            }  
      
            return (Class) params[index];  
        }  
      
        /** 
         * 将反射时的checked exception转换为unchecked exception. 
         */  
        public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {  
            if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException  
                    || e instanceof NoSuchMethodException) {  
                return new IllegalArgumentException("Reflection Exception.", e);  
            } else if (e instanceof InvocationTargetException) {  
                return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());  
            } else if (e instanceof RuntimeException) {  
                return (RuntimeException) e;  
            }  
            return new RuntimeException("Unexpected Checked Exception.", e);  
        }  
    }  




```

- 分页信息

**Page.java**

```markdown

    package com.dao.mongdb.base;
    
    import java.io.Serializable;
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
    public class Page<T> implements Serializable {
    
        private static final long serialVersionUID = 1571744502699069043L;
        /**
         * 当前页面大小
         */
        private Integer pageSize;
        /**
         * 当前页码
         */
        private Integer pageNum;
        /**
         * 总条数
         */
        private Long total;
        /**
         * 总页数
         */
        private Integer pages;
    
        /**
         * 是否有下一页
         */
        private Boolean hasNexPage;
    
        /**
         * 是否有上一页
         */
        private Boolean hasPrePage;
    
        /**
         * 是否为第一页
         */
        private Boolean isFirstPage;
    
        /**
         * 是否为最后一页
         */
        private Boolean isLastPage;
    
        /**
         * 当前页大小
         */
        private Integer size;
    
        /**
         * 结果集
         */
        private List<T> list;
    
        public Integer getPageSize() {
            return pageSize;
        }
    
        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }
    
        public Integer getPageNum() {
            return pageNum;
        }
    
        public void setPageNum(Integer pageNum) {
            this.pageNum = pageNum;
        }
    
        public Long getTotal() {
            return total;
        }
    
        public void setTotal(Long total) {
            this.total = total;
        }
    
        public Integer getPages() {
            return pages;
        }
    
        public void setPages(Integer pages) {
            this.pages = pages;
        }
    
        public Boolean getHasNexPage() {
            return hasNexPage;
        }
    
        public void setHasNexPage(Boolean hasNexPage) {
            this.hasNexPage = hasNexPage;
        }
    
        public Boolean getHasPrePage() {
            return hasPrePage;
        }
    
        public void setHasPrePage(Boolean hasPrePage) {
            this.hasPrePage = hasPrePage;
        }
    
        public Boolean getFirstPage() {
            return isFirstPage;
        }
    
        public void setFirstPage(Boolean firstPage) {
            isFirstPage = firstPage;
        }
    
        public Boolean getLastPage() {
            return isLastPage;
        }
    
        public void setLastPage(Boolean lastPage) {
            isLastPage = lastPage;
        }
    
        public Integer getSize() {
            return size;
        }
    
        public void setSize(Integer size) {
            this.size = size;
        }
    
        public List<T> getList() {
            return list;
        }
    
        public void setList(List<T> list) {
            this.list = list;
        }
    
        public Page() {
        }
    
        public Page(List<T> list, Integer pageNum, Integer pageSize, Long total) {
           this(list,pageNum,pageSize,total,false);
        }
    
        public Page(List<T> list, Integer pageNum, Integer pageSize, Long total, Boolean hasCount) {
            if(list==null&&total==null){
                this.list = null;
                this.pageNum = pageNum;
                this.pageSize = pageSize;
                this.total = 0l;
                this.size=0;
                this.pages=0;
            }else {
                this.list = list;
                this.pageNum = pageNum;
                this.pageSize = pageSize;
                this.total = total;
                if(hasCount){
                    this.size = list.size()!=0?list.size()-1:0;
                }else{
                    this.size = list.size();
                }
                this.pages = Integer.valueOf(String.valueOf(Math.ceil(total / (double) pageSize)).split("\\.")[0]);
            }
            if (pageNum == 1) {
                this.isFirstPage = true;
            } else {
                this.isFirstPage = false;
            }
            if (pageNum == this.pages) {
                this.isLastPage = true;
            } else {
                this.isLastPage = false;
            }
    
            if (pages > pageNum) {
                this.hasNexPage = true;
            } else {
                this.hasNexPage = false;
            }
            if (pageNum > 1) {
                this.hasPrePage = true;
            } else {
                this.hasPrePage = false;
            }
        }
    
    }



```


- 继承抽象类

**UserDao.java**

```markdown
    
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



```


- 实体类

> **User.java**

```markdown
    
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



```


- 控制层

> **UserController.java**

```markdown
    
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



```

- 启动测试

> **http://localhost:9876/find**

```markdown
    
    [
        {
            "name": "小旭",
            "sex": "男",
            "age": 26,
            "score": null
        },
        {
            "name": "小聪",
            "sex": "女",
            "age": 23,
            "score": null
        },
        {
            "name": "小超",
            "sex": "中",
            "age": 27,
            "score": null
        },
        {
            "name": null,
            "sex": null,
            "age": null,
            "score": null
        }
    ]
    
    
    若一个数据集没这些字段会返回一个结果，只是结果全为 null
    
```
