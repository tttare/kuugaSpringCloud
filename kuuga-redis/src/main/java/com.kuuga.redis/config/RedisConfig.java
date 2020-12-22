package com.kuuga.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author qiuyongkang
 * @Description: RedisConfig
 * @Title: RedisConfig
 * @date 2020/12/21 11:21
 */
@Configuration
public class RedisConfig {

    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        /**
         * redis的序列化方式:RedisTemplate中需要声明4种serializer,默认为JdkSerializationRedisSerializer
         * keySerializer :对于普通K-V操作时，key采取的序列化策略
         * valueSerializer:value采取的序列化策略
         * hashKeySerializer: 在hash数据结构中，hash-key的序列化策略
         * hashValueSerializer:hash-value的序列化策略
         * */
        /**
         * StringRedisSerializer:redis存储的数据与java中一致,字符串不带引号
         * JdkSerializationRedisSerializer: 使用JDK提供的序列化功能。 优点是反序列化时不需要提供类型信息(class)，但缺点是需要实现
         *          Serializable接口，还有序列化后的结果非常庞大，是JSON格式的5倍左右，这样就会消耗redis服务器的大量内存
         *          二进制值,在redis中是不可读的
         * Jackson2JsonRedisSerializer： 使用Jackson库将对象序列化为JSON字符串。优点是速度快，序列化后的字符串短小精悍，
         *          不需要实现Serializable接口。但缺点也非常致命，那就是此类的构造函数中有一个类型参数，
         *          必须提供要序列化对象的类型信息(.class对象)。 通过查看源代码，发现其只在反序列化过程中用到了类型信息。
         *          作为keySerializer时,会给字符串或Interger值加上 引号
         *
         * */
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        //jackson序列化的值字符串形式
        /**
         *         [
         *             "com.kuuga.api.system.model.User",
         *             {"userId":null,
         *             "userName":"tttare"}
         *         ]
         */
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }



}


