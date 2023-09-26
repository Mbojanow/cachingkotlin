package com.cachingexamples.cachingworkshops

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class PersonEntity(
    @Id
    @GeneratedValue
    val id: Long?,

    val firstName: String,
    val lastName: String,
    val age: Int?
) {
    constructor(): this(null, "", "", null)

    constructor(firstName: String, lastName: String, age: Int?): this(null, firstName, lastName, age)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PersonEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "PersonEntity(id=$id, firstName='$firstName', lastName='$lastName', age=$age)"
    }

}