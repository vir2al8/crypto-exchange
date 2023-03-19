import com.crypto.api.apiObjectMapper
import com.crypto.api.v1.models.*
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {

    @Test
    fun serialize() {
        val request = OrderCreateRequest(
            requestId = "123",
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.BAD_WALLET_ID
            ),
            order = OrderCreateObject(
                walletId = "1",
                amount = BigDecimal.valueOf(10),
                type = OrderType.MARKET,
                operation = OrderOperation.BUYING
            )
        )

        val json = apiObjectMapper.writeValueAsString(request)

        assertContains(json, Regex("\"walletId\":\\s*\"1\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badWalletId\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val request = OrderCreateRequest(
            requestId = "123",
            debug = OrderDebug(
                mode = OrderRequestDebugMode.STUB,
                stub = OrderRequestDebugStubs.BAD_WALLET_ID
            ),
            order = OrderCreateObject(
                walletId = "1",
                amount = BigDecimal.valueOf(10),
                type = OrderType.MARKET,
                operation = OrderOperation.BUYING
            )
        )
        val json = apiObjectMapper.writeValueAsString(request)

        val obj = apiObjectMapper.readValue(json, IRequest::class.java) as OrderCreateRequest

        assertEquals(request, obj)
    }
}