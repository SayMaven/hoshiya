package com.hoshiya.app.core.model

data class AnimeQuote(
    val japanese: String,
    val romaji: String,
    val translation: String,
    val character: String,
    val anime: String
) {
    companion object {
        val ALL_QUOTES = listOf(
            AnimeQuote(
                japanese = "頑張って、先輩！星がいつも見守っています。",
                romaji = "Ganbatte, Senpai! Hoshi ga itsumo mimamotteimasu.",
                translation = "Semangat ya Senpai! Bintang-bintang selalu menemanimu fokus.",
                character = "Hoshiya Companion",
                anime = "Original"
            ),
            AnimeQuote(
                japanese = "諦めたらそこで試合終了ですよ。",
                romaji = "Akirametara sokode shiai shuuryou desu yo.",
                translation = "Kalau kamu menyerah sekarang, permainan sudah berakhir.",
                character = "Anzai Sensei",
                anime = "Slam Dunk"
            ),
            AnimeQuote(
                japanese = "小さな一歩でも、歩き続ければ遠くまで行ける。",
                romaji = "Chiisana ippo demo, arukitsuzukereba tooku made ikeru.",
                translation = "Meskipun hanya langkah kecil, jika terus melangkah kamu akan sampai jauh.",
                character = "Aethel",
                anime = "Frieren: Beyond Journey's End"
            ),
            AnimeQuote(
                japanese = "お疲れ様です！少しお茶でも飲んで休んでね。",
                romaji = "Otsukaresama desu! Sukoshi ocha demo nonde yasunde ne.",
                translation = "Kerja bagus! Istirahat dulu dan nikmati secangkir teh hangat.",
                character = "Hoshiya Maid",
                anime = "Original"
            ),
            AnimeQuote(
                japanese = "夜空の星のように、あなたの努力も必ず輝くよ。",
                romaji = "Yozora no hoshi no you ni, anata no doryoku mo kanarazu kagayaku yo.",
                translation = "Seperti bintang di langit malam, perjuanganmu pasti akan bersinar.",
                character = "Starlight Muse",
                anime = "Original"
            ),
            AnimeQuote(
                japanese = "できるかどうかじゃない。なりたいからやるんだ。",
                romaji = "Dekiru ka dou ka janai. Naritai kara yarun da.",
                translation = "Ini bukan tentang bisa atau tidak. Aku melakukannya karena aku menginginkannya.",
                character = "Monkey D. Luffy",
                anime = "One Piece"
            ),
            AnimeQuote(
                japanese = "努力した者が全て報われるとは限らん。しかし、成功した者は皆すべからく努力しておる。",
                romaji = "Doryoku shita mono ga subete mukuwareru to wa kagiran. Shikashi, seikou shita mono wa mina subekaraku doryoku shite oru.",
                translation = "Tidak semua orang yang berusaha membuahkan hasil, tapi mereka yang berhasil pasti telah berjuang keras.",
                character = "Coach Kamogawa",
                anime = "Hajime no Ippo"
            )
        )

        fun randomQuote(): AnimeQuote = ALL_QUOTES.random()
    }
}
