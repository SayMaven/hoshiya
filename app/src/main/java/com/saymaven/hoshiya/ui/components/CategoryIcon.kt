package com.saymaven.hoshiya.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.School
import androidx.compose.ui.graphics.vector.ImageVector
import com.saymaven.hoshiya.core.model.FocusCategory

fun getCategoryIcon(category: FocusCategory): ImageVector {
    return when (category) {
        FocusCategory.GENERAL -> Icons.Outlined.Layers
        FocusCategory.STUDY -> Icons.Outlined.School
        FocusCategory.DEVELOPMENT -> Icons.Outlined.Code
        FocusCategory.CREATIVE -> Icons.Outlined.Brush
        FocusCategory.READING -> Icons.AutoMirrored.Outlined.MenuBook
        FocusCategory.WRITING -> Icons.Outlined.Edit
    }
}
