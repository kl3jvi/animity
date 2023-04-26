package com.kl3jvi.animity.data.enums

enum class AnimeTypes {
    GOGO_ANIME,
    ENIME;

    fun displayName(): String = when (this) {
        GOGO_ANIME -> "GoGo Anime"
        ENIME -> "Enime"
    }

    companion object {
        val animeEntries = AnimeTypes.values().map { it.displayName() }.toTypedArray()

        fun getNameRepresentation(name: String) =
            values().find { it.name == name }?.displayName() ?: "GoGo Anime"
    }
}
