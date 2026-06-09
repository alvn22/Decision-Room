package id.ac.pnm.decisionroom.model.room

data class ChatMessage(
    var senderId: String = "",
    var senderName: String = "",
    var message: String = "",
    var timestamp: Long = System.currentTimeMillis()
)