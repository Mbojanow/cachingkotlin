package com.cachingexamples.cachingworkshops

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.Duration

@Configuration
@EnableCaching
//@ConditionalOnProperty(name = ["spring.cache.type"], havingValue = "true", matchIfMissing = false)
//@Profile("caching")
class CachingConfiguration {

//    @Bean
//    fun longRunningCacheManager(): CacheManager {
//        val cacheManager = CaffeineCacheManager()
//        cacheManager.setCaffeine(
//            Caffeine.newBuilder()
//                .maximumSize(100)
//                .expireAfterAccess(Duration.ofHours(5))
//        )
//        return cacheManager
//    }
//
//
//    @Bean
//    @Primary
//    fun shortButBigRunningCacheManager(): CacheManager {
//        val cacheManager = CaffeineCacheManager()
//        cacheManager.setCaffeine(
//            Caffeine.newBuilder()
//                .maximumSize(1000000)
//                .expireAfterAccess(Duration.ofSeconds(3))
//        )
//        return cacheManager
//    }
}

@Component
class CustomBeanPostProcessor: BeanPostProcessor {
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        if (beanName.lowercase().contains("cache")) {
            println("BEAN: $beanName")
        }
        return bean
    }
}