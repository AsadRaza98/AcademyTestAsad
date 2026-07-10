# AcademyTest — Android (Jetpack Compose)

An Android port of a small SwiftUI **item-management** app, built with **Kotlin** and
**Jetpack Compose**. It displays a sorted list of items — each with a name and a favorite
state — a detail screen, and an add-item sheet. Changes to an item are reflected everywhere
in the app automatically.

This is the Android equivalent of the SwiftUI exercise at
[wearebeatcode/AcademyTest2026](https://github.com/wearebeatcode/AcademyTest2026).

## Features

- Sorted list of items (case-insensitive by name, tie-broken by creation order)
- Favorite toggle available from both the list row and the detail screen; the change is
  reflected in both places instantly (single source of truth)
- Add new items via a modal bottom sheet (auto-focused field, Save disabled until valid)
- Delete via swipe-to-dismiss on the list, or the destructive action on the detail screen
- Empty state with a call to action
- `@Preview` for every UI component and screen
- Italian UI strings, centralized in `strings.xml`

## Architecture

MVVM with **unidirectional data flow**, the idiomatic Compose analog of the original's
SwiftUI `@Observable` + bindings design:

- **`Item`** — an immutable Kotlin `data class` (the `Identifiable` + `Hashable` analog).
- **`ItemsListViewModel`** — the single source of truth. Holds the item list and selection
  as `StateFlow`s and exposes derived `sortedItems` / `selectedItem`. All mutations
  (`addItem`, `toggleFavorite`, `delete`, `deleteFromDetail`) are methods on the ViewModel.
- **Composables** — stateless where possible: they receive state (flows down) and raise
  events via lambdas (flows up). The single ViewModel is shared across the list and detail,
  so an edit in one place updates the other.

Navigation mirrors the original's **selection-driven** model: with no selection the list is
shown; selecting an item shows the detail; hardware back clears the selection — the way
`NavigationSplitView` collapses to a stack on a phone.

### Project structure

```
app/src/main/java/com/asad/academytest/
├── model/
│   └── Item.kt                 # immutable domain model
├── viewmodel/
│   └── ItemsListViewModel.kt   # single source of truth (StateFlow)
├── ui/
│   ├── FavoriteButton.kt       # reusable star toggle
│   ├── ItemRow.kt              # a single list row
│   ├── ItemsListScreen.kt      # list + swipe-to-delete + top bar
│   ├── ItemDetailScreen.kt     # detail form + destructive delete
│   ├── AddItemSheet.kt         # modal bottom sheet to add an item
│   ├── EmptyState.kt           # reusable "content unavailable" view
│   └── theme/                  # generated Compose theme
└── MainActivity.kt             # app wiring (list <-> detail, add sheet)
```

## SwiftUI → Jetpack Compose mapping

| SwiftUI (original) | Jetpack Compose (this port) |
| --- | --- |
| `struct ...: View { var body }` | `@Composable fun ...()` |
| `#Preview { }` | `@Preview @Composable fun ...()` |
| `ItemViewModel` (`Identifiable`, `Hashable`) | `Item` (`data class`) |
| `@Observable class ItemsListViewModel` | `ItemsListViewModel : ViewModel()` exposing `StateFlow` |
| `@State` (owns model) | `viewModel()` |
| `@Bindable` / `@Binding` | value in + lambda out (state hoisting) |
| `NavigationSplitView` | selection-driven list ⟷ detail + `BackHandler` |
| `List { ForEach }` | `LazyColumn { items(...) }` |
| `.onDelete` (swipe) | `SwipeToDismissBox` |
| `.sheet` + `.presentationDetents([.medium])` | `ModalBottomSheet` |
| `Form` / `Section` | section-titled `Card` |
| `ContentUnavailableView` | `EmptyState` composable |
| `LabeledContent` | label/value `Row` |
| `Image(systemName: "star.fill" / "star")` | `Icons.Filled.Star` / `Icons.Filled.StarBorder` |
| `localizedCaseInsensitiveCompare` | `java.text.Collator` (SECONDARY strength) |
| Italian string literals | `res/values/strings.xml` |

## Building & running

Requirements: **Android Studio** (Ladybug or newer) with the Android SDK.

1. Open the project in Android Studio and let Gradle sync.
2. Select an emulator or a connected device (min SDK 24).
3. Press **Run**.

Command line:

```bash
./gradlew assembleDebug   # build the debug APK
```

## Using AI

This port was built with AI assistance (Claude), used as a pair-programmer rather than a
code dump:

- **Planning:** agreed on the architecture (unidirectional data flow) and a layer-by-layer
  plan up front, so each commit maps to one coherent piece of the original app.
- **Incremental generation:** each layer (data model → leaf components → screens → wiring)
  was generated, **compile-checked with Gradle**, and reviewed before committing.
- **Evaluating output:** decisions where the AI's first suggestion was adjusted — e.g.
  choosing a selection-driven layout over pulling in the Navigation library to stay close to
  the original's model, folding the trivial `AddItemViewModel` into local Compose state, and
  adding a no-arg ViewModel constructor so `viewModel()` can build it.

The commit history is intentionally granular to show how the work evolved.

## Notes & possible enhancements

- **Persistence:** like the original, state is in-memory and resets on relaunch. A real app
  would add a repository + persistence (DataStore / Room).
- **Adaptive layout:** on tablets/foldables, `ListDetailPaneScaffold` (Material 3 adaptive)
  would show list and detail side-by-side, closer to `NavigationSplitView` on iPad.
- **Swipe API:** `SwipeToDismissBox` currently uses the (deprecated) `confirmValueChange`
  callback; it works but could move to the newer dynamic-anchors approach.
- **Tests:** the ViewModel's logic (sorting, add/delete, re-selection) is a good candidate
  for unit tests.
