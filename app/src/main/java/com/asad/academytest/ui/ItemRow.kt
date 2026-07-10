package com.asad.academytest.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asad.academytest.R
import com.asad.academytest.model.Item
import com.asad.academytest.ui.theme.AcademyTestTheme

/**
 * A single row in the items list.
 *
 * Android equivalent of the SwiftUI `ItemRowView`: name on top, a favorite/not-favorite
 * caption below, and the star toggle trailing. Stateless — the favorite toggle is raised
 * to the caller via [onToggleFavorite].
 */
@Composable
fun ItemRow(
    item: Item,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(
                    if (item.isFavorite) R.string.favorite else R.string.not_favorite
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        FavoriteButton(
            isFavorite = item.isFavorite,
            onToggle = onToggleFavorite,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemRowPreview() {
    AcademyTestTheme {
        Column {
            ItemRow(
                item = Item(creationIndex = 0, name = "Caffè", isFavorite = true),
                onToggleFavorite = {},
            )
            ItemRow(
                item = Item(creationIndex = 1, name = "Zaino", isFavorite = false),
                onToggleFavorite = {},
            )
        }
    }
}
