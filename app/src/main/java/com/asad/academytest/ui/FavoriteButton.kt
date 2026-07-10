package com.asad.academytest.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.asad.academytest.R
import com.asad.academytest.ui.theme.AcademyTestTheme

// Golden star tint, matching SwiftUI's `.yellow` used in the original FavoriteButton.
private val FavoriteYellow = Color(0xFFFFCC00)

/**
 * Reusable star toggle, shared by the list row and the detail screen.
 *
 * Android equivalent of the SwiftUI `FavoriteButton`. Instead of a two-way `@Binding`,
 * the state is hoisted: the current value comes in via [isFavorite] and the toggle
 * intent goes out via [onToggle] (state down, events up).
 */
@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onToggle, modifier = modifier) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
            contentDescription = stringResource(
                if (isFavorite) R.string.remove_from_favorites else R.string.add_to_favorites
            ),
            tint = if (isFavorite) FavoriteYellow else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteButtonPreview() {
    AcademyTestTheme {
        // Interactive: tap the star in the preview to toggle it.
        var isFavorite by remember { mutableStateOf(true) }
        FavoriteButton(isFavorite = isFavorite, onToggle = { isFavorite = !isFavorite })
    }
}
