package com.study.test_container

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Main
fun main(array: Array<String>) {
    runApplication<Main>(*array)
}