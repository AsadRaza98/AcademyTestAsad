package com.asad.academytest.model

import java.util.UUID

/**
 * Domain model for a single item.
 *
 * Android equivalent of the SwiftUI `ItemViewModel`, which was `Identifiable` + `Hashable`.
 * A Kotlin `data class` gives us structural `equals`/`hashCode`/`copy` for free, so we can
 * keep the model immutable and let the [ItemsListViewModel] be the single source of truth.
 *
 * As in the original, [id] is the stable identity and [creationIndex] preserves insertion
 * order (used as a tie-breaker when two names sort equally).
 */
data class Item(
    val id: String = UUID.randomUUID().toString(),
    val creationIndex: Int,
    val name: String,
    val isFavorite: Boolean = false,
)
