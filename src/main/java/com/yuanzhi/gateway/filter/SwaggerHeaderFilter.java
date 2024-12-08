package com.yuanzhi.gateway.filter;

import com.yuanzhi.gateway.config.SwaggerProvider;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class SwaggerHeaderFilter extends AbstractGatewayFilterFactory {

    private static final String HEADER_NAME = "X-Forwarded-Prefix";

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            if (!StringUtils.endsWithIgnoreCase(path, SwaggerProvider.API_URI)&&!StringUtils.endsWithIgnoreCase(path, SwaggerProvider.KNIFE_API_URI)) {
                return chain.filter(exchange);
            }
            if(StringUtils.endsWithIgnoreCase(path, SwaggerProvider.API_URI)) {
                String basePath = "/";

                ServerHttpRequest newRequest = request.mutate().header(HEADER_NAME, basePath).path(SwaggerProvider.API_URI).build();
                ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
                return chain.filter(newExchange);
            }else{
                String basePath = "/";

                ServerHttpRequest newRequest = request.mutate().header(HEADER_NAME, basePath).path(SwaggerProvider.KNIFE_API_URI).build();
                ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
                return chain.filter(newExchange);
            }
        };
      /*  return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            if (!StringUtils.endsWithIgnoreCase(path,SwaggerProvider.API_URI )) {
                return chain.filter(exchange);
            }
            String basePath = path.substring(0, path.lastIndexOf(SwaggerProvider.API_URI));
            ServerHttpRequest newRequest = request.mutate().header(HEADER_NAME, basePath).build();
            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newExchange);
        };*/
    }

}