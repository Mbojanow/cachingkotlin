package com.cachingexamples.cachingworkshops

import java.io.Serializable

data class Person(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val age: Int?
): Serializable