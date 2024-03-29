package com.study.test_container

import com.study.test_container.entity.Customer
import com.study.test_container.entity.CustomerRepository
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class CustomerControllerTest(
) {

    @LocalServerPort
    private val port: Int? = null

    @Autowired
    var customerRepository: CustomerRepository? = null

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost:$port"
        customerRepository!!.deleteAll()
    }

    @Test
    fun shouldGetAllCustomers() {
        val customers = listOf(
            Customer(
                name = "a",
                email = "a@mail.com"
            ),
            Customer(
                name = "b",
                email = "b@mail.com"
            )
        )

        customerRepository!!.saveAll(customers)

        RestAssured.given()
            .contentType(ContentType.JSON)
            .`when`()
            .get("/api/customers")
            .then()
            .statusCode(200)
            .body(".", hasSize<Int>(2))
    }

    companion object {

        @Container
        private var postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:15-alpine")

        @JvmStatic
        @DynamicPropertySource
        @Suppress("unused")
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.apply {
                add("spring.datasource.url") { postgres.jdbcUrl }
                add("spring.datasource.username") { postgres.username }
                add("spring.datasource.password") { postgres.password }
            }
        }
    }
}