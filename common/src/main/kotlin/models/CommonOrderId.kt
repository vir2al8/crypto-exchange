package models

@JvmInline
value class CommonOrderId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = CommonOrderId("")
    }
}
