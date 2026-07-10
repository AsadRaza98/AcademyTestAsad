package com.asad.academytest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asad.academytest.ui.AddItemSheet
import com.asad.academytest.ui.ItemDetailScreen
import com.asad.academytest.ui.ItemsListScreen
import com.asad.academytest.ui.theme.AcademyTestTheme
import com.asad.academytest.viewmodel.ItemsListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AcademyTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AcademyTestApp()
                }
            }
        }
    }
}

/**
 * Root of the app. Android equivalent of the SwiftUI `AcademyTestApp` + `ContentView`.
 *
 * Owns the single [ItemsListViewModel] and mirrors the original's selection-driven
 * navigation: with no selection the list is shown, otherwise the detail — hardware back
 * clears the selection, matching how `NavigationSplitView` collapses on a phone.
 */
@Composable
private fun AcademyTestApp(
    viewModel: ItemsListViewModel = viewModel(),
) {
    val items by viewModel.sortedItems.collectAsStateWithLifecycle()
    val selectedItem by viewModel.selectedItem.collectAsStateWithLifecycle()
    var showAddSheet by rememberSaveable { mutableStateOf(false) }

    val selection = selectedItem
    if (selection == null) {
        ItemsListScreen(
            items = items,
            onItemClick = { viewModel.select(it.id) },
            onToggleFavorite = viewModel::toggleFavorite,
            onDelete = viewModel::delete,
            onAddClick = { showAddSheet = true },
        )
    } else {
        ItemDetailScreen(
            item = selection,
            onToggleFavorite = { viewModel.toggleFavorite(selection.id) },
            onDelete = { viewModel.deleteFromDetail(selection) },
            onBack = { viewModel.select(null) },
        )
        BackHandler { viewModel.select(null) }
    }

    if (showAddSheet) {
        AddItemSheet(
            onDismiss = { showAddSheet = false },
            onSave = { name -> viewModel.addItem(name) },
        )
    }
}
