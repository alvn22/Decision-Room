package id.ac.pnm.decisionroom.model.room

data class Room(
    val roomId: String = "",
    val title: String = "",
    val hostId: String = "",
    val status: String = "waiting",
    val anonymousVoting: Boolean = false,
    val participants: Map<String, Participant> = emptyMap(),
    val options: Map<String, VoteOption> = emptyMap()
)
