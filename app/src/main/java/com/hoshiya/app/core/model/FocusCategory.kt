package com.hoshiya.app.core.model

enum class FocusCategory(
    val title: String,
    val emoji: String,
    val iconName: String
) {
    STUDY("Study", "📚", "MenuBook"),
    CODE("Coding", "💻", "Code"),
    ART("Art / Creative", "🎨", "Palette"),
    READ("Reading", "📖", "AutoStories"),
    WRITE("Writing", "✍️", "Edit"),
    OTHER("General", "✨", "Stars");

    companion object {
        fun fromName(name: String?): FocusCategory {
            return values().firstOrNull { it.name.equals(name, ignoreCase = true) } ?: STUDY
        }
    }
}
