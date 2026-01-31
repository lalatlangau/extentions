// use an integer for version numbers
version = 1

cloudstream {
    language = "id"
    description = "Dutamovie - Nonton Film & Series Online"
    authors = listOf("Dutamovie")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 1
    tvTypes = listOf(
        "TvSeries",
        "Movie",
        "AsianDrama"
    )

    iconUrl = "https://hidekielectronics.com/favicon.ico"
}