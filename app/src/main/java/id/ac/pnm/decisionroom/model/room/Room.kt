package id.ac.pnm.decisionroom.model.room

data class Room(
    var roomId: String = "",
    var title: String = "",
    var hostId: String = "",
    var status: String = "waiting",
    var anonymousVoting: Boolean = false,
    var participants: Map<String, Participant> = emptyMap(),
    var options: List<VoteOption> = emptyList(),
    var votes: Map<String, Int> = emptyMap()
)
