package com.yuanzhi.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.yuanzhi.gateway.config.AccessTokenConfig;
import com.yuanzhi.gateway.config.FilterIgnorePropertiesConfig;
import com.yuanzhi.gateway.constant.CacheKeys;
import com.yuanzhi.gateway.utils.JwtUtil;
import com.yuanzhi.gateway.utils.MD5Utils;

import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName: RequestGlobalFilter
 * @author: zhangding
 * 全局拦截器，作用所有的微服务
 * @date: 2020/2/19  7:16 下午
 */
@Slf4j
@Component
@AllArgsConstructor
public class RequestGlobalFilter implements GlobalFilter, Ordered {

    private final FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

    private final AccessTokenConfig accessTokenConfig;

    private final JwtUtil jwtUtil;

    private static final String SECRET_KEY = "2024_CS5721";

    // without Token url
    private final static String IGNORE_URL = "/user/withoutToken";
    private final static String SWAGGER_URL = "swagger";
    private final static String API_DOCS_URL = "api-docs";
    private final static String WEBJARS_URL = "webjars";

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";


    @Override
    @SneakyThrows
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        List<String> urls = filterIgnorePropertiesConfig.getUrls().stream().filter(path::startsWith).collect(Collectors.toList());
        if (urls.size() > 0) {
            return chain.filter(exchange);
        }

        String access_token = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);

        String userId = null;
        if (StrUtil.isNotBlank(access_token)) {
            userId = jwtUtil.getUserIdFromToken(access_token);
        }
        List<String> clients = filterIgnorePropertiesConfig.getClients().stream().filter(path::startsWith).collect(Collectors.toList());

        if (clients.size() == 0) {
            if (StrUtil.isBlank(userId)) {
                ServerHttpResponse response = exchange.getResponse();
                JSONObject message = new JSONObject();
                message.put("code", 401);
                message.put("msg", "Please login first。");
                byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(bits);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                return response.writeWith(Mono.just(buffer));
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1000;
    }

}
