package com.github.open.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ElapsedFilter implements GlobalFilter, Ordered {

    private static final String ELAPSED_TIME_BEGIN = "elapsedTimeBegin";

    /**
     * 实现filter()方法记录处理时间
     *
     * @param exchange 用于获取与当前请求、响应相关的数据，以及设置过滤器间传递的上下文数据
     * @param chain    Gateway过滤器链对象
     * @return Mono对应一个异步任务，因为Gateway是基于Netty Server异步处理的,Mono对就代表异步处理完毕的情况。
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        exchange.getAttributes().put(ELAPSED_TIME_BEGIN, System.currentTimeMillis());

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute(ELAPSED_TIME_BEGIN);
                    if (startTime != null) {
                        log.info(exchange.getRequest().getRemoteAddress() //远程访问的用户地址
                                + " | " + exchange.getRequest().getPath()  //Gateway URI
                                + " | cost " + (System.currentTimeMillis() - startTime) + "ms"); //处理时间
                    }
                })
        );
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}