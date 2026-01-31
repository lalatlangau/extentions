package com.dutamovie

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Element

class DutamovieProvider : MainAPI() {
    override var mainUrl = "https://hidekielectronics.com"
    override var name = "Dutamovie"
    override var lang = "id"
    override val hasMainPage = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.AsianDrama
    )

    override val mainPage = mainPageOf(
        "$mainUrl/page/" to "Latest Movies",
        "$mainUrl/genre/action/page/" to "Action",
        "$mainUrl/genre/adventure/page/" to "Adventure",
        "$mainUrl/genre/comedy/page/" to "Comedy",
        "$mainUrl/genre/drama/page/" to "Drama",
        "$mainUrl/genre/horror/page/" to "Horror",
        "$mainUrl/genre/thriller/page/" to "Thriller",
        "$mainUrl/genre/romance/page/" to "Romance",
        "$mainUrl/genre/scifi/page/" to "Sci-Fi"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(request.data + page).document
        val home = document.select("article.item-infinite").mapNotNull {
            it.toSearchResult()
        }
        return newHomePageResponse(request.name, home)
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val title = this.selectFirst("h2.entry-title a")?.text()?.trim() ?: return null
        val href = fixUrl(this.selectFirst("h2.entry-title a")?.attr("href") ?: return null)
        val posterUrl = fixUrlNull(this.selectFirst("img")?.attr("src"))
        val quality = this.selectFirst("span.quality")?.text()
        
        return newMovieSearchResponse(title, href, TvType.Movie) {
            this.posterUrl = posterUrl
            this.quality = getQualityFromString(quality)
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/?s=$query").document
        return document.select("article.item-infinite").mapNotNull {
            it.toSearchResult()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document
        
        val title = document.selectFirst("h2.entry-title a")?.text()?.trim() ?: ""
        val poster = document.selectFirst("img")?.attr("src")
        val tags = document.select("div.genre a").map { it.text() }
        val year = document.selectFirst("span.year")?.text()?.toIntOrNull()
        val description = document.selectFirst("div.entry-content p")?.text()?.trim()
        val rating = document.selectFirst("span.rating")?.text()?.toRatingInt()
        
        val recommendations = document.select("article.item-infinite").mapNotNull {
            it.toSearchResult()
        }

        // Detect if it's a TV Series
        val tvType = if (document.select("div.seasons, .episode-list").isNotEmpty()) 
            TvType.TvSeries 
        else 
            TvType.Movie
        
        return if (tvType == TvType.TvSeries) {
            val episodes = document.select("div.episode-list a, .episodes a").map { ep ->
                val href = fixUrl(ep.attr("href"))
                val name = ep.text()
                Episode(href, name)
            }
            
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.year = year
                this.plot = description
                this.tags = tags
                this.rating = rating
                this.recommendations = recommendations
            }
        } else {
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.year = year
                this.plot = description
                this.tags = tags
                this.rating = rating
                this.recommendations = recommendations
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document
        
        // Extract from iframes
        document.select("div.player-embed iframe").forEach { iframe ->
            val iframeUrl = fixUrl(iframe.attr("src"))
            loadExtractor(iframeUrl, subtitleCallback, callback)
        }
        
        // Extract direct download links
        document.select("div.download-links a").forEach { link ->
            val url = link.attr("href")
            val quality = link.text()
            
            callback.invoke(
                ExtractorLink(
                    this.name,
                    this.name,
                    url,
                    referer = mainUrl,
                    quality = getQualityFromName(quality),
                )
            )
        }
        
        return true
    }
}