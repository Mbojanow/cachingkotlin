package com.cachingexamples.cachingworkshops

import org.springframework.boot.CommandLineRunner
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component

@Component
class DbInitializr(private val personRepository: PersonRepository,
    private val cacheManager: CacheManager): CommandLineRunner {
    override fun run(vararg args: String?) {
        personRepository.saveAll(
            listOf(
                PersonEntity("Andrzej", "Nowak", 3),
                PersonEntity("Marian", "Pohling", 19)
            )
        )
    }
}