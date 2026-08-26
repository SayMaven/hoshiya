package com.saymaven.hoshiya.core.model

enum class FocusCategory(
    val title: String
) {
    GENERAL("General"),
    STUDY("Study"),
    DEVELOPMENT("Development"),
    CREATIVE("Creative"),
    READING("Reading"),
    WRITING("Writing");

    companion object {
        fun fromName(name: String?): FocusCategory {
            return values().firstOrNull { it.name.equals(name, ignoreCase = true) } ?: GENERAL
        }
    }
}
