import com.crypto.api.v1.models.*
import com.crypto.common.CommonContext
import com.crypto.common.models.*
import com.crypto.common.stubs.CommonStub
import com.crypto.mappers.fromTransport
import com.crypto.mappers.toTransport
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant

class MapperTest {
    @Test
    fun fromTransport() {
        val request = OrderCreateRequest(
            requestId = "123",
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.SUCCESS
            ),
            order = OrderCreateObject(
                walletId = "1011",
                amount = BigDecimal.TEN,
                type = OrderType.MARKET,
                operation = OrderOperation.BUYING
            )
        )

        val context = CommonContext()
        context.fromTransport(request)

        assertEquals(CommonStub.SUCCESS, context.stubCase)
        assertEquals(CommonWorkMode.STUB, context.workMode)
        assertEquals(CommonWalletId("1011"), context.orderRequest.walletId)
        assertEquals(BigDecimal.TEN, context.orderRequest.amount)
        assertEquals(CommonOrderType.MARKET, context.orderRequest.type)
        assertEquals(CommonOrderOperation.BUYING, context.orderRequest.operation)
    }

    @Test
    fun toTransport() {
        val context = CommonContext(
            requestId = CommonRequestId("123"),
            command = CommonCommand.CREATE,
            state = CommonState.RUNNING,
            errors = mutableListOf(
                CommonError(
                    code = "error",
                    group = "request",
                    field = "walletId",
                    message = "Wallet is blocked"
                )
            ),
            orderResponse = CommonOrder(
                id = CommonOrderId("1000"),
                walletId = CommonWalletId("1011"),
                amount = BigDecimal.TEN,
                type = CommonOrderType.MARKET,
                operation = CommonOrderOperation.BUYING,
                status = CommonOrderStatus.OPEN,
                createdAt = Instant.parse("2023-03-03T08:05:57Z")
            )
        )

        val response = context.toTransport() as OrderCreateResponse

        assertEquals("123", response.requestId)
        assertEquals("1000", response.order?.id)
        assertEquals("1011", response.order?.walletId)
        assertEquals(BigDecimal.TEN, response.order?.amount)
        assertEquals(OrderType.MARKET, response.order?.type)
        assertEquals(OrderOperation.BUYING, response.order?.operation)
        assertEquals(OrderStatus.OPEN, response.order?.status)
        assertEquals("2023-03-03T08:05:57Z", response.order?.createdAt)
        assertEquals(1, response.errors?.size)
        assertEquals("error", response.errors?.firstOrNull()?.code)
        assertEquals("request", response.errors?.firstOrNull()?.group)
        assertEquals("walletId", response.errors?.firstOrNull()?.field)
        assertEquals("Wallet is blocked", response.errors?.firstOrNull()?.message)
    }
}