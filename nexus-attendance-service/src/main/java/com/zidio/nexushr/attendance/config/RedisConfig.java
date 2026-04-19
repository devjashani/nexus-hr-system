package com.zidio.nexushr.attendance.config;

//RedisConfig.java

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

    // ✅ REQUIRED: Connection Factory
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6380);
    }

    // ✅ Redis Template
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory()); // VERY IMPORTANT
        return template;
    }
}

//@Configuration
//public class RedisConfig {
//
// @Bean
// public JedisConnectionFactory redisConnectionFactory() {
//     JedisConnectionFactory factory = new JedisConnectionFactory();
//     factory.setHostName("localhost");
//     factory.setPort(6379);
//     factory.setPassword("");
//     return factory;
// }
//
// @Bean
// public RedisTemplate<String, Object> redisTemplate() {
//     RedisTemplate<String, Object> template = new RedisTemplate<>();
//     template.setConnectionFactory(redisConnectionFactory());
//     
//     ObjectMapper objectMapper = new ObjectMapper();
//     objectMapper.registerModule(new JavaTimeModule());
//     
//     GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//     
//     template.setKeySerializer(new StringRedisSerializer());
//     template.setHashKeySerializer(new StringRedisSerializer());
//     template.setValueSerializer(serializer);
//     template.setHashValueSerializer(serializer);
//     
//     template.afterPropertiesSet();
//     return template;
// }
// 
// // Pub/Sub Topics
// @Bean
// public ChannelTopic attendanceTopic() {
//     return new ChannelTopic("attendance:events");
// }
// 
// @Bean
// public ChannelTopic leaveTopic() {
//     return new ChannelTopic("leave:events");
// }
// 
// @Bean
// public ChannelTopic notificationTopic() {
//     return new ChannelTopic("notification:events");
// }
//}
