package com.kl3jvi.animity.parsers

// enum class Providers(val providerName: String) {
//    GOGOANIME("gogoAnime"),
//    NINEANIME("nineAnime")
// }

sealed interface Providers {
    object GoGoAnime : Providers
    object WcoFunAnime : Providers
}
