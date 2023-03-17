import com.crypto.api.apiObjectMapper
import com.crypto.api.v1.models.*
import org.junit.Test
import java.math.BigDecimal
import java.time.Instant
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {

    @Test
    fun serialize() {
        val response = OrderCreateResponse(
            requestId = "123",
            result = ResponseResult.SUCCESS,
            order = OrderResponseObject(
                walletId = "1",
                amount = BigDecimal.valueOf(10),
                type = OrderType.MARKET,
                operation = OrderOperation.BUYING,
                id = "order-id",
                status = OrderStatus.OPEN,
                createdAt = Instant.now().toString(),
                updatedAt = null
            )
        )

        val json = apiObjectMapper.writeValueAsString(response)

        assertContains(json, Regex("\"walletId\":\\s*\"1\""))
        assertContains(json, Regex("\"amount\":\\s*10"))
        assertContains(json, Regex("\"type\":\\s*\"market\""))
        assertContains(json, Regex("\"status\":\\s*\"open\""))
        assertContains(json, Regex("\"id\":\\s*\"order-id\""))
    }

    @Test
    fun deserialize() {
        val response = OrderCreateResponse(
            requestId = "123",
            result = ResponseResult.SUCCESS,
            order = OrderResponseObject(
                walletId = "1",
                amount = BigDecimal.valueOf(10),
                type = OrderType.MARKET,
                operation = OrderOperation.BUYING,
                id = "order-id",
                status = OrderStatus.OPEN,
                createdAt = Instant.now().toString(),
                updatedAt = null
            )
        )
        val json = apiObjectMapper.writeValueAsString(response)

        val obj = apiObjectMapper.readValue(json, IResponse::class.java) as OrderCreateResponse

        assertEquals(response, obj)
    }
}