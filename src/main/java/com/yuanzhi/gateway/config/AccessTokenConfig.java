package com.yuanzhi.gateway.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: AccessTokenConfig
 * @author: zhangding
 * @date: 2020/2/23  8:48 下午
 */
@Data
@Configuration
@RefreshScope
@ConditionalOnExpression("!'${secret}'.isEmpty()")
@ConfigurationProperties(prefix = "secret")
public class AccessTokenConfig {

    private String key;
}
