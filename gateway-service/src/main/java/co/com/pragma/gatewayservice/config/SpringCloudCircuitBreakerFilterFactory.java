package co.com.pragma.gatewayservice.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerResilience4JFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.DispatcherHandler;

public abstract class SpringCloudCircuitBreakerFilterFactory extends AbstractGatewayFilterFactory<SpringCloudCircuitBreakerFilterFactory> {

    public static final String NAME = "PhotoCircuitBreaker";

    @Bean
    @ConditionalOnBean(ReactiveResilience4JCircuitBreakerFactory.class)
    @ConditionalOnEnabledFilter
    public SpringCloudCircuitBreakerResilience4JFilterFactory springCloudCircuitBreakerResilience4JFilterFactory(
            ReactiveResilience4JCircuitBreakerFactory reactiveCircuitBreakerFactory,
            ObjectProvider<DispatcherHandler> dispatcherHandler) {

        return new SpringCloudCircuitBreakerResilience4JFilterFactory(reactiveCircuitBreakerFactory, dispatcherHandler);
    }
}
