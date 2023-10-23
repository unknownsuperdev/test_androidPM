package ru.tripster.entities.response.chat

data class ChangesResponse(
    val confirmed: ConfirmedResponse?,
    val date_time: DateTimeResponse?,
    val full_price: FullPriceResponse?,
    val persons_count: PersonsCountResponse?,
    val status: StatusResponse?,
    val tickets: TicketsResponse?
) {
    companion object {
        fun empty(): ChangesResponse =
            ChangesResponse(
                confirmed = ConfirmedResponse.empty(),
                date_time = DateTimeResponse.empty(),
                full_price = FullPriceResponse.empty(),
                persons_count = PersonsCountResponse.empty(),
                status = StatusResponse.empty(),
                tickets = TicketsResponse.empty()
            )
    }
}