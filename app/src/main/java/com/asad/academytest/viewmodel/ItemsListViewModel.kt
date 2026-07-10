package com.asad.academytest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asad.academytest.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.text.Collator

/**
 * Android equivalent of the SwiftUI `ItemsListViewModel`.
 *
 * It owns the full list of items and every operation on it. Views observe the exposed
 * [StateFlow]s (state flows down) and call methods to mutate (actions flow up), which is
 * the Compose analog of the original's `@Observable` + `private(set)` design.
 */
class ItemsListViewModel(
    initialItems: List<Item> = defaultItems,
) : ViewModel() {

    private val _items = MutableStateFlow(initialItems)
    /** Source-of-truth list, in insertion order. */
    val items: StateFlow<List<Item>> = _items.asStateFlow()

    private val _selectedItemId = MutableStateFlow<String?>(null)
    /** Id of the currently selected item, or null. Mirrors `selectedItemID`. */
    val selectedItemId: StateFlow<String?> = _selectedItemId.asStateFlow()

    // Ever-increasing counter, seeded one past the highest existing index.
    private var nextCreationIndex: Int =
        (initialItems.maxOfOrNull { it.creationIndex } ?: -1) + 1

    // Locale-aware, case-insensitive comparison — the analog of Swift's
    // `localizedCaseInsensitiveCompare` (SECONDARY = case-insensitive, accent-sensitive).
    private val collator = Collator.getInstance().apply { strength = Collator.SECONDARY }

    private val itemComparator = Comparator<Item> { first, second ->
        val byName = collator.compare(first.name, second.name)
        if (byName != 0) byName else first.creationIndex - second.creationIndex
    }

    /** Items sorted by name (case-insensitive), tie-broken by creation order. */
    val sortedItems: StateFlow<List<Item>> = _items
        .map { list -> list.sortedWith(itemComparator) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = initialItems.sortedWith(itemComparator),
        )

    /** The currently selected item, kept live so edits reflect everywhere. */
    val selectedItem: StateFlow<Item?> = combine(_items, _selectedItemId) { items, id ->
        items.firstOrNull { it.id == id }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null,
    )

    fun select(id: String?) {
        _selectedItemId.value = id
    }

    /** Adds a new item with a trimmed name and returns it. */
    fun addItem(name: String): Item {
        val trimmed = name.trim()
        val newItem = Item(
            creationIndex = nextCreationIndex,
            name = trimmed,
            isFavorite = false,
        )
        nextCreationIndex += 1
        _items.update { it + newItem }
        return newItem
    }

    /** Toggles the favorite flag for the item with the given id. */
    fun toggleFavorite(id: String) {
        _items.update { list ->
            list.map { if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it }
        }
    }

    fun delete(item: Item) = deleteItems(setOf(item.id))

    /** Deletes from the detail screen and clears the selection. */
    fun deleteFromDetail(item: Item) {
        _items.update { list -> list.filterNot { it.id == item.id } }
        _selectedItemId.value = null
    }

    private fun deleteItems(idsToDelete: Set<String>) {
        _items.update { list -> list.filterNot { it.id in idsToDelete } }

        val selected = _selectedItemId.value
        if (selected != null && selected in idsToDelete) {
            // Re-select the first remaining item, matching the original's behaviour.
            _selectedItemId.value = sortedItems.value.firstOrNull()?.id
        }
    }

    companion object {
        val defaultItems: List<Item> = listOf(
            Item(creationIndex = 0, name = "Lupo 🐺", isFavorite = true),
            Item(creationIndex = 1, name = "Giraffa 🦒", isFavorite = false),
            Item(creationIndex = 2, name = "Leone 🦁", isFavorite = false),
        )
    }
}
