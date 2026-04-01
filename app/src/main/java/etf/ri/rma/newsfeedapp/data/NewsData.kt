package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.model.NewsItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

object NewsData {
    // Datum format "yyyy-MM-dd"
    private val df = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Inicijalna lista od 10 vijesti iz različitih kategorija:
    private val initialNews: MutableList<NewsItem> = mutableListOf(
        NewsItem(
            uuid = UUID.randomUUID().toString(),
            title = "Naučnici otkrivaju novi mineral na Marsu",
            snippet = "Pomoću rovera Perseverance, tim NASA-inih stručnjaka uuidentificirao je nepoznati mineral...",
            source = "Science Daily",
            publishedDate = LocalDate.now().minusDays(5).format(df),
            imageUrl = "https://images.unsplash.com/photo-1746730348427-78952b87e1cf?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDJ8NnNNVmpUTFNrZVF8fGVufDB8fHx8fA%3D%3D",
            category = "Science",
            isFeatured = true
        ),
        NewsItem(
            uuid = UUID.randomUUID().toString(),
            title = "Fudbalska reprezentacija BiH pobijedila Norvešku",
            snippet = "U prijateljskoj utakmici, selekcija BiH ostvarila je uvjerljivu pobjedu 3:1...",
            source = "Sportske Novosti",
            publishedDate = LocalDate.now().minusDays(3).format(df),
            imageUrl = "https://images.unsplash.com/photo-1737472446386-73efbbafa4a9?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDN8NnNNVmpUTFNrZVF8fGVufDB8fHx8fA%3D%3D",
            category = "Sports",
            isFeatured = true
        ),
        NewsItem(
            uuid = UUID.randomUUID().toString(),
            title = "Dionice Apple dosegle novi rekord",
            snippet = "Zahvaljujući dobrom kvartalnom izvještaju, Apple dionice probile su granicu od 200 USD...",
            source = "Bloomberg",
            publishedDate = LocalDate.now().minusDays(2).format(df),
            imageUrl = "https://images.unsplash.com/photo-1748357657816-cc98c9c1d552?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDN8dG93SlpGc2twR2d8fGVufDB8fHx8fA%3D%3D",
            category = "Business"
            ,
            isFeatured = true
        ),
        NewsItem(
            uuid = UUID.randomUUID().toString(),
            title = "Rezultati lokalnih izbora u Sarajevu",
            snippet = "Koalicija X pobijedila je s 45% glasova, dok je stranka Y osvojila 30%...",
            source = "Avaz",
            publishedDate = LocalDate.now().minusDays(10).format(df),
            imageUrl = "https://images.unsplash.com/photo-1748032886813-3b7f6dc863b4?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDd8dG93SlpGc2twR2d8fGVufDB8fHx8fA%3D%3D",
            category = "Politics",
            isFeatured = true
        ),
        NewsItem(
            uuid = UUID.randomUUID().toString(),
            title = "Novi restoran otvoren u centru Sarajeva",
            snippet = "Gastronomski specijaliteti iz Azije, Europe i Balkana u jednom ambijentu...",
            source = "Dnevni Avaz",
            publishedDate = LocalDate.now().minusDays(7).format(df),
            imageUrl = "https://images.unsplash.com/photo-1746192774685-92a49a9463e5?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDJ8aG1lbnZRaFVteE18fGVufDB8fHx8fA%3D%3D",
            category = "Food"
            ,
            isFeatured = true
        ),
        NewsItem(
            uuid = UUID.randomUUID().toString(),
            title = "Stručnjaci upozoravaju na klimatske promjene",
            snippet = "Globalno zagrijavanje dostiže kritičnu točku, kažu znanstvenici...",
            source = "National Geographic",
            publishedDate = LocalDate.now().minusDays(1).format(df),
            imageUrl = "https://images.unsplash.com/photo-1746023790179-7b3bed576e67?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDd8aG1lbnZRaFVteE18fGVufDB8fHx8fA%3D%3D",
            category = "Health"
            ,
            isFeatured = true
        ),
        NewsItem(
            uuid = UUID.randomUUID().toString(),
            title = "Premijerno prikazana nova Apple TV+ serija",
            snippet = "Fanovi diljem svijeta hvale režiju i glumačku postavu...",
            source = "Variety",
            publishedDate = LocalDate.now().minusDays(4).format(df),
            imageUrl = "https://plus.unsplash.com/premium_photo-1743572054857-985f5ca90ec2?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDEyfGhtZW52UWhVbXhNfHxlbnwwfHx8fHw%3D",
            category = "Entertainment"
            ,
            isFeatured = true
        ),
        NewsItem(
            uuid = UUID.randomUUID().toString(),
            title = "Putovanje vlakom kroz Alpe – savjeti za avanturiste",
            snippet = "Scenski krajolici, povijesne postaje i najbolja mjesta za usputni obrok...",
            source = "Lonely Planet",
            publishedDate = LocalDate.now().minusDays(8).format(df),
            imageUrl = "https://images.unsplash.com/photo-1748020285050-c57ace0754ea?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDh8eEh4WVRNSExnT2N8fGVufDB8fHx8fA%3D%3D",
            category = "Travel"
            ,
            isFeatured = true
        ),
        NewsItem(
            uuid = UUID.randomUUID().toString(),
            title = "Najnovija tehnologija – savitljivi ekrani na pomolu",
            snippet = "Ekrani koji se mogu saviti i uvući u samu strukturu uređaja postaju stvarnost...",
            source = "TechCrunch",
            publishedDate = LocalDate.now().minusDays(6).format(df),
            imageUrl = "https://images.unsplash.com/photo-1748069037195-e80f05d9209b?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDExfHhIeFlUTUhMZ09jfHxlbnwwfHx8fHw%3D",
            category = "Tech",
            isFeatured = true
        ),
        NewsItem(
            uuid = UUID.randomUUID().toString(),
            title = "Lokalni radio stanica slavi 30 godina postojanja",
            snippet = "Brojne emisije, intervjui i koncerti uživo obilježavaju tri desetljeća na eteru...",
            source = "Radio Slobodna Evropa",
            publishedDate = LocalDate.now().minusDays(9).format(df),
            imageUrl = "https://images.unsplash.com/photo-1748096089006-d2c0777a9c82?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDE4fHhIeFlUTUhMZ09jfHxlbnwwfHx8fHw%3D",
            category = "General",
            isFeatured = true
        )
    )

    // Interna instanca DAO-a koja će se koristiti za dodavanje fetched vijesti
    private val newsDAO: NewsDAO = NewsDAO

    /**
     * Prvi poziv getAllNews(): vraća unaprijed pripremljenu listu.
     * Nakon toga, svaka nova vijest dohvaćena preko getTopStoriesByCategory
     * se dodaje u internal listu. Ne gubi se u prelasku ekrana (dok je app živa).
     */
    fun getAllNews(): List<NewsItem> {
        // Dodamo inicijalne vijesti jedino ako još nisu dodane u newsDAO.allStories
        // Ali pošto newsDAO.allStories u konstruktoru kreira praznu listu, stvari su jednostavne:
        if (newsDAO.getAllStories().isEmpty()) {
            // Dodamo ih direktno u DAO (koristimo refleksiju? Ne, DAO nema metodu za dodavanje ručno)
            // Jednostavno: nadogradimo NewsDAOImpl da daje mogućnost inicijalne liste, ili
            // Ovdje "prebacimo" initialNews u allStories reflektivanjem.
            // Ali for the sake of simplicity, možemo iskoristiti metodu koja ne žali već postojeće:
            initialNews.forEach { item ->
                // Pošto allStories je privatno unutar NewsDAOImpl,
                // možemo iz NewsData pristupiti tako da NewsDAOImpl ima javnu metodu za inicijalno punjenje.
                // Stoga, dodajmo u NewsDAOImpl:
                (newsDAO as NewsDAO).addInitialNewsItem(item)
            }
        }
        return newsDAO.getAllStories()
    }

    /**
     * Ovo je Pomoćna metoda koju dodajemo u NewsDAOImpl radi inicijalnog punjenja.
     */
    // Ionako ćemo u NewsDAOImpl definirati:
    // fun addInitialNewsItem(item: NewsItem)
}