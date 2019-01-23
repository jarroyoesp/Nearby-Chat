package es.jarroyo.nearbychat.model

class Message(val message: String, val type: Int) {
    companion object {
        val TYPE_SEND = 0
        val TYPE_RECEIVED = 1
    }
}