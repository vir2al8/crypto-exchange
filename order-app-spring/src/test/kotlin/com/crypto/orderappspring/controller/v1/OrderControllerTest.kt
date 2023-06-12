package com.crypto.orderappspring.controller.v1

import com.crypto.api.v1.models.*
import com.crypto.common.CommonContext
import com.crypto.logic.OrderProcessor
import com.crypto.mappers.toTransportCreate
import com.crypto.mappers.toTransportDelete
import com.crypto.mappers.toTransportRead
import com.crypto.mappers.toTransportSearch
import com.crypto.orderappspring.service.OrderBlockingProcessor
import com.crypto.stubs.OrderStub
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.print.print
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(OrderController::class)
internal class OrderControllerTest {
    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockkBean(relaxUnitFun = true)
    private lateinit var processor: OrderBlockingProcessor

    @Test
    fun createOrder() = testStubOrder(
        "/api/v1/order/create",
        OrderCreateRequest(),
        CommonContext().toTransportCreate()
    )

    @Test
    fun readOrder() = testStubOrder(
        "/api/v1/order/read",
        OrderReadRequest(),
        CommonContext().toTransportRead()
    )

    @Test
    fun deleteOrder() = testStubOrder(
        "/api/v1/order/delete",
        OrderDeleteRequest(),
        CommonContext().toTransportDelete()
    )

    @Test
    fun searchOrder() = testStubOrder(
        "/api/v1/order/search",
        OrderSearchRequest(),
        CommonContext().toTransportSearch()
    )

    private fun <Req : Any, Res : Any> testStubOrder(url: String, requestObj: Req, responseObj: Res) {
        val request = mapper.writeValueAsString(requestObj)
        val response = mapper.writeValueAsString(responseObj)

        mvc.perform(
            MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andDo { print() }
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(response))
        coVerify { processor.exec(any()) }
    }
}