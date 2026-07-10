# AcademyTest — Android

A small **item-management** app for Android, built with **Kotlin** and **Jetpack Compose**.

It's an Android version of a SwiftUI app: you get a sorted list of items — each with a name
and a favorite (star) — a detail screen, and a way to add new items. Marking something as a
favorite updates everywhere in the app instantly.

Original SwiftUI exercise: [wearebeatcode/AcademyTest2026](https://github.com/wearebeatcode/AcademyTest2026).

## What it does

- Shows a list of items, sorted alphabetically by name
- Tap the ⭐ to mark an item as favorite — from the list or the detail screen
- Tap an item to open its details
- Add a new item with the **+** button
- Swipe a row to delete it (or delete from the detail screen)
- Friendly empty screen when there are no items

## How to run it

You'll need **Android Studio**.

1. Open the project in Android Studio and wait for it to finish loading.
2. Pick an emulator or plug in an Android phone.
3. Press **Run** ▶️.

## How it's built

- **Kotlin + Jetpack Compose** for the whole UI.
- One place holds all the data (`ItemsListViewModel`), so any change — like tapping a star —
  shows up everywhere at once.
- Every screen and component has a **Preview**, so you can see it inside Android Studio
  without running the app.

## Using AI

I used an AI assistant (Claude) as a pair-programmer while porting this from SwiftUI:

- We planned the app in small pieces and built it one layer at a time.
- Each piece was checked (it had to build successfully) and reviewed before I committed it.
- The commit history is intentionally step-by-step, so you can follow how the app came
  together rather than seeing just the final result.

## Good to know

- Data is kept in memory, so it resets when the app restarts (same as the original).
- On tablets the list and detail could be shown side-by-side — a nice future improvement.
