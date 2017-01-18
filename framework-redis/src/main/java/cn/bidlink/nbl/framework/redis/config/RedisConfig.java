package cn.bidlink.nbl.framework.redis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:applicationContext-redis.xml")
public class RedisConfig {

}