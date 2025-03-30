package eu.kanade.tachiyomi.extension.en.unsounded

import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.network.asObservableSuccess
import eu.kanade.tachiyomi.source.model.FilterList
import eu.kanade.tachiyomi.source.model.MangasPage
import eu.kanade.tachiyomi.source.model.Page
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import eu.kanade.tachiyomi.source.online.ParsedHttpSource
import eu.kanade.tachiyomi.util.asJsoup
import okhttp3.Response
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import rx.Observable

@Suppress("unused")
class Unsounded : ParsedHttpSource() {
    override val name = "Unsounded"

    override val lang = "en"

    override val supportsLatest = false

    override val baseUrl = "$DOMAIN/Unsounded/comic+index/"

    override fun popularMangaSelector() = "#chapter_box"

    override fun popularMangaRequest(page: Int) = GET(baseUrl, headers)

    override fun popularMangaFromElement(element: Element) = SManga.create().apply {
        author = "Ashley Cope"
        artist = "Ashley Cope"
        title = element.selectFirst("h2")!!.text()
        description = element.selectFirst(".sub")?.text()
        setUrlWithoutDomain(element.selectFirst("a")!!.absUrl("href"))
        thumbnail_url = DOMAIN + url.replace(chapterRegex, "/pageart/$1.jpg")
        status = if (element.`is`("#chapter_box:first-of-type")) SManga.ONGOING else SManga.COMPLETED
    }

    override fun fetchSearchManga(page: Int, query: String, filters: FilterList): Observable<MangasPage> =
        fetchPopularManga(1).map { mp ->
            MangasPage(mp.mangas.filter { it.title.contains(query, ignoreCase = true) }, false)
        }

    override fun fetchMangaDetails(manga: SManga): Observable<SManga> = Observable.just(manga)

    override fun chapterListRequest(manga: SManga) = GET(baseUrl, headers)

    override fun fetchChapterList(manga: SManga): Observable<List<SChapter>> =
        client.newCall(chapterListRequest(manga)).asObservableSuccess().map {
            it.asJsoup().select("#chapter_box:contains(${manga.title}) > a")
                .map(::chapterFromElement).sortedByDescending(SChapter::chapter_number)
        }

    override fun chapterFromElement(element: Element) = SChapter.create().apply {
        setUrlWithoutDomain(element.absUrl("href"))
        name = element.ownText()
        chapter_number = name.toFloat()
        date_upload = 0L
    }

    override fun fetchPageList(chapter: SChapter): Observable<List<Page>> =
        Observable.just(listOf(Page(1, getChapterUrl(chapter))))

    override fun imageUrlParse(document: Document): String =
        document.selectFirst("#comic > a > img")!!.absUrl("src")

    override fun getMangaUrl(manga: SManga) = DOMAIN + manga.url

    override fun getChapterUrl(chapter: SChapter) = DOMAIN + chapter.url.replace(pageRegex, "/$1.html")

    override fun popularMangaNextPageSelector(): String? = null

    override fun latestUpdatesSelector() = ""

    override fun latestUpdatesNextPageSelector(): String? = null

    override fun latestUpdatesRequest(page: Int) =
        throw UnsupportedOperationException()

    override fun latestUpdatesFromElement(element: Element) =
        throw UnsupportedOperationException()

    override fun searchMangaSelector() = ""

    override fun searchMangaNextPageSelector(): String? = null

    override fun searchMangaRequest(page: Int, query: String, filters: FilterList) =
        throw UnsupportedOperationException()

    override fun searchMangaFromElement(element: Element) =
        throw UnsupportedOperationException()

    override fun searchMangaParse(response: Response) =
        throw UnsupportedOperationException()

    override fun chapterListSelector() = ""

    override fun mangaDetailsParse(document: Document) =
        throw UnsupportedOperationException()

    override fun chapterListParse(response: Response) =
        throw UnsupportedOperationException()

    override fun pageListParse(document: Document) =
        throw UnsupportedOperationException()

    private companion object {
        private const val DOMAIN = "https://casualvillain.com"

        private val chapterRegex = Regex("""/(ch\d+_\d+)\.html$""")

        private val pageRegex = Regex("""/pageart/(ch\d+_\d+)\.jpg$""")
    }
}
