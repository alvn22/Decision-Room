package id.ac.pnm.decisionroom.model.room

data class Participant(
    val name: String = "",
    val host: Boolean = false,
    val ready: Boolean = false
)