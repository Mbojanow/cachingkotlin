package com.cachingexamples.cachingworkshops

import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserEndpoints(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): List<Person> {
        return userService.getAllUsers()
    }

    @GetMapping("/{id}")
    fun findUserById(@PathVariable id: Long): Person {
        return userService.findUserById(id) ?: throw RuntimeException("BOOM")
    }

    @PostMapping
    fun createUser(@RequestBody person: Person): Person {
        return userService.savePerson(person)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody person: Person): Person {
        return userService.updatePerson(id, person)
    }
}

@Service
@CacheConfig(cacheNames = ["a"])
class UserService(private val personRepository: PersonRepository) {

    @Cacheable(cacheNames = ["users"], /*cacheManager = "longRunningCacheManager"*/)
    fun getAllUsers(): List<Person> {
        println("In getAllUsers")
        return personRepository.findAll()
            .map { it.toDto() }
    }

    @Cacheable(cacheNames = ["user"], condition = "#a0 > 1"/*unless = "#result != null", cacheManager = "shortButBigRunningCacheManager"*/)
    fun findUserById(id: Long): Person? {
        println("In findUserById")
        return personRepository.findById(id).orElse(null)
            .toDto()
    }

    @CacheEvict(cacheNames = ["users"], allEntries = true)
    fun savePerson(person: Person): Person {
        return personRepository.save(person.toEntity())
            .toDto()
    }

    @Caching(
        evict = [
            CacheEvict(cacheNames = ["user"], key = "#a0"),
            CacheEvict(cacheNames = ["users"], allEntries = true)]
    )
    //@CachePut(cacheNames = ["user"], key = "#id")
    fun updatePerson(id: Long, person: Person): Person {
        val currentPerson = personRepository.findById(id).orElseThrow()
        val updatedPerson = currentPerson.copy(
            firstName = person.firstName,
            lastName = person.lastName,
            age = person.age
        )
        return personRepository.save(updatedPerson)
            .toDto()
    }

    private fun PersonEntity.toDto() =
        Person(id, firstName, lastName, age)

    private fun Person.toEntity() = PersonEntity(
        id, firstName, lastName, age
    )


}