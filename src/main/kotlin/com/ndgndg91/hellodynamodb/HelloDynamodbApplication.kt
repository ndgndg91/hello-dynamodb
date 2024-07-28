package com.ndgndg91.hellodynamodb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HelloDynamodbApplication

fun main(args: Array<String>) {
    runApplication<HelloDynamodbApplication>(*args)
}
