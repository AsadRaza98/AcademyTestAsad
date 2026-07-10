package com.asad.academytest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asad.academytest.R
import com.asad.academytest.model.Item
import com.asad.academytest.ui.theme.AcademyTestTheme
import com.asad.academytest.viewmodel.ItemsListViewModel

/**
 * The list screen. Android equivalent of the SwiftUI `ItemsListView`.
 *
 * Stateless: it renders the (already sorted) [items] and raises every user intent to the
 * caller. Shows an [EmptyState] when there are no items, otherwise a scrollable list with
 * swipe-to-delete (the analog of SwiftUI's `.onDelete`).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsListScreen(
    items: List<Item>,
    onItemClick: (Item) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onDelete: (Item) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.items_title)) },
                actions = {
                    IconButton(onClick = onAddClick) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.add_item),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        if (items.isEmpty()) {
            EmptyState(
                modifier = Modifier.padding(innerPadding),
                icon = Icons.Filled.Inbox,
                title = stringResource(R.string.empty_title),
                description = stringResource(R.string.empty_list_description),
                actionLabel = stringResource(R.string.add_item),
                onAction = onAddClick,
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                items(items = items, key = { it.id }) { item ->
                    SwipeToDeleteContainer(onDelete = { onDelete(item) }) {
                        ItemRow(
                            item = item,
                            onToggleFavorite = { onToggleFavorite(item.id) },
                            // Opaque background so the delete layer only shows while swiping.
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable { onItemClick(item) },
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

/**
 * Wraps a row so a right-to-left swipe deletes it — the equivalent of SwiftUI's
 * swipe-to-delete on a `List` row.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteContainer(
    onDelete: () -> Unit,
    content: @Composable () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        },
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        },
        content = { content() },
    )
}

@Preview(showBackground = true)
@Composable
private fun ItemsListScreenPreview() {
    AcademyTestTheme {
        ItemsListScreen(
            items = ItemsListViewModel.defaultItems,
            onItemClick = {},
            onToggleFavorite = {},
            onDelete = {},
            onAddClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Empty")
@Composable
private fun ItemsListScreenEmptyPreview() {
    AcademyTestTheme {
        ItemsListScreen(
            items = emptyList(),
            onItemClick = {},
            onToggleFavorite = {},
            onDelete = {},
            onAddClick = {},
        )
    }
}
