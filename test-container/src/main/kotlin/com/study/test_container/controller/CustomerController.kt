package com.study.test_container.controller

import com.study.test_container.entity.Customer
import com.study.test_container.entity.CustomerRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class CustomerController(
    private val repo: CustomerRepository
) {

    @GetMapping("/api/customers")
    fun getAll(): List<Customer> = repo.findAll()
}