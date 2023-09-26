package com.cachingexamples.cachingworkshops

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.cache.interceptor.SimpleKey
import org.springframework.cache.interceptor.SimpleKeyGenerator
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.reflect.Method
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId

data class Pricing(
    val calculationTime: Instant,
    val price: BigDecimal
)

@RestController
@RequestMapping("/api/pricing")
class PricingEndpoints(private val pricingService: PricingService) {

    @PostMapping
    fun calculatePrice(@RequestBody pricing: Pricing): BigDecimal {
        return pricingService.calculate(pricing)
    }
}

@Service
class PricingService {

    @Cacheable(cacheNames = ["pricing"], keyGenerator = "fullHourKeyGenerator")
    fun calculate(pricing: Pricing): BigDecimal {
        println("IN calculate")
        return pricing.price
    }

    @CacheEvict(cacheNames = ["pricing"], keyGenerator = "fullHourKeyGenerator")
    fun invalidate(instant: Instant) {

    }
}

@Component
class FullHourKeyGenerator: KeyGenerator {
    override fun generate(target: Any, method: Method, vararg params: Any?): Any =
        when {
            params.size == 1 && params[0] is Pricing -> SimpleKey(toFullHourInstant((params[0] as Pricing).calculationTime))
            params.size == 1 && params[0] is Instant -> SimpleKey(toFullHourInstant((params[0] as Instant)))
            else -> SimpleKeyGenerator().generate(target, method, params)
        }

        private fun toFullHourInstant(input: Instant) =
            input.atZone(ZoneId.systemDefault())
                .withNano(0)
                .withSecond(0)
                .withMinute(0)
                .toInstant()
}