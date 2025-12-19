# 🎁 Gift Tracker Code Kata

> **Gift-Tracker-Code-Kata** is a **code kata** designed to practice **Jetpack Compose** and **UI
animations** in Android by building a simple Christmas Gift Tracker app.

---

## 📸 Screenshots

<p align="center">
  <img src="docs/images/GiftListScreen.jpeg" width="30%" alt="Gift List Screen"/>
  <img src="docs/images/GiftDetailScreen.jpeg" width="30%" alt="Gift Detail Screen"/>
  <img src="docs/images/SummaryScreen.jpeg" width="30%" alt="Summary Screen"/>
</p>

---

## 🎯 Overview

This repository contains a collaborative Christmas-themed **Android Code Kata** designed to practice
modern UI development with **Jetpack Compose**.

It's meant to be done with teammates, focusing on building **adaptive layouts** for mobile and
tablet, adding small **animations**, and exploring the new **Navigation 3** APIs — especially the
use of **pane-based navigation** for multi-window experiences.

Perfect for a short team session to experiment, learn, and have fun while creating a simple but
responsive **Gift Tracker** app.

---

## 🧠 What is a Code Kata?

A **code kata** is a small programming exercise intended to be repeated and refined to improve
skills, design decisions, and confidence.

In this kata, the main goal is to **practice Jetpack Compose**, **animations**, and **modern Android
UI patterns**.

---

## 🚀 Project Goals

By completing this kata, you will build an app that allows users to:

1. ✅ Display a list of gifts
2. ➕ Add new gifts
3. 👁️ View gift details
4. ✏️ Edit existing gifts
5. 🗑️ Delete gifts
6. 💫 Add UI animations and transitions
7. 📱 Create adaptive layouts (phone / tablet)
8. 🔄 Handle basic state and navigation
9. 🎨 Improve UI/UX with feedback and empty states
10. 🧭 Explore the new Navigation 3 API
11. 🖼️ Play with edge-to-edge and theming

---

## 🧩 Tasks to Complete

Below is the recommended list of tasks to **progressively complete the app**.

## Create Composables

### 1. 📋 Gift List (`GiftListScreen`)

#### Create Gift Item Composable

Display a gift with the following information:

- Person Name
- Gift Name
- Price
- Status (isPurchased?)

<p align="center">
  <img src="docs/images/GiftItem.png" width="60%" alt="Gift Item"/>
</p>

#### Create Floating Action Button

<p align="center">
  <img src="docs/images/FAB.png" width="30%" alt="Floating Action Button"/>
</p>

---

### 2. ➕ Add Gift Screen (`GiftDetailScreen`)

Create a Gift detail screen to add or edit a gift.

#### Navigation

- If we click on the FAB → pass `null` (new gift)
- If we click on an existing gift → pass the gift ID

#### Reusable TextField Component

Create a reusable TextField composable with:

- Label
- Initial Value
- `onValueChange` callback
- Category
- Optional TrailingIcon
- Keyboard Options & Actions

<p align="center">
  <img src="docs/images/EditTextField.png" width="60%" alt="Edit Text Field"/>
</p>

#### Gift Status Field

Add field below the text fields to show if the gift is purchased or not.

<p align="center">
  <img src="docs/images/GiftStatus.png" width="60%" alt="Gift Status"/>
</p>

#### TopAppBar

Create the form with a TopAppBar and a back button or close button depending if the user is editing.

<p align="center">
  <img src="docs/images/GiftDetailTopBar.png" width="60%" alt="Gift Detail Top Bar"/>
</p>

#### Save/Edit Button

Add a save/edit button to change mode.

<p align="center">
  <img src="docs/images/SaveButton.png" width="40%" alt="Save Button"/>
  <img src="docs/images/EditButton.png" width="40%" alt="Edit Button"/>
</p>

---

### 3. 🔍 Summary Screen (`SummaryScreen`)

Display main info about the total of the gifts.

#### Reusable Summary Item

Create an item which contains an icon, text and `@Composable` content. We should be able to reuse it
and pass the surface and icon color.

<p align="center:
  <img src="docs/images/SummaryItem.png" width="60%" alt="Summary Item"/>
</p>

#### Grid Layout

Create a grid layout with an odd number of items:

- When we have only one item in a row, it should take the full width
- Maximum number of columns: **2** when compact, **3** when expanded

**Recommended Items:**

- Total Gifts
- Total Expense
- Most expensive gift
- Least expensive gift

<p align="center">
  <img src="docs/images/LazyGrid.png" width="80%" alt="Lazy Grid"/>
</p>

---

#### 3.1 📊 Circular Chart

Create a circular chart to show the total expense and the expense per person.

> **TODO:** Add documentation about how to create a circular chart in Compose.

<p align="center">
  <img src="docs/images/ExpensePerPerson.png" width="60%" alt="Expense Per Person Chart"/>
</p>

---

#### 3.2 ⏭️ Next Steps Section

Create a NextSteps section which shows the remaining gifts to buy.

- Be able to mark the gift as purchased from this section
- Show confirmation snackbar when marking as purchased (TODO)

<p align="center">
  <img src="docs/images/NextSteps.png" width="80%" alt="Next Steps"/>
</p>

---

## 💫 UI Animations

For this part of the kata we will focus on adding animations to improve the user experience.

All the animations are done with the Compose animation APIs. For more information and help, check
the link:

- 📚 [Compose Animation APIs](https://developer.android.com/develop/ui/compose/animation/choose-api)

---

### 1. 🎞️ Gift Item Animations

Add animations to the gift item in the list when the user checks the gift as purchased. The item
should be animated as if it is being **wrapped as a gift**.

**Example:**

<p align="center">
  <img src="docs/video/GiftItem_video.gif" width="80%" alt="Gift Item Animation"/>
</p>

---

### 2. 🎈 FAB Animation

The FAB should only appear when the user is in the Gift List screen. Animate the visibility of the
FAB when navigating to the SummaryScreen.

<p align="center">
  <img src="docs/video/FAB_video.gif" width="60%" alt="FAB Animation"/>
</p>

---

### 3. 💾 Save/Edit Button Animation

When the user is adding or editing a gift, animate the Save/Edit button to give feedback:

- **Editing → Save:** Button transitions with size and color change
- **Save → Editing:** Same transition \+ show a loading indicator

<p align="center">
  <img src="docs/video/save_edit_video.gif" width="60%" alt="Save/Edit Button Animation"/>
</p>

---

### 4. 🔄 Screen Transition Animations

Add transition animations when navigating between screens using Navigation 3.

<p align="center">
  <img src="docs/video/Transition_video.gif" width="80%" alt="Screen Transition Animation"/>
</p>

---

### 5. 🖼️ List/Details View Animation

When the user selects a gift from the list to view its details:

- Animate the transition between the list item and the detail view
- On wide screens, show both views side by side in two different panes

---

### 6. 📜 List Appearance Animation

Animate the items when the gift list appears on the screen to create a more engaging experience.

---

### 7. 🗑️ Swipe to Delete with Animation

When the user swipes a gift item to the right, animate the item showing it's going to be deleted.

📚 [Drag, swipe, and fling](https://developer.android.com/develop/ui/compose/touch-input/pointer-input/drag-swipe-fling#swiping)

<p align="center">
  <img src="docs/video/swipeToDelete_video.gif" width="80%" alt="Swipe To Delete Animation"/>
</p>

---

### 🏆 EXTRA POINTS: Advanced Animations 🤓

It is said that the most brave developers do not stop here. They go further for more animations!

If you are a brave developer, try adding a new animation for the chart in the summary screen:

- Animate the chart when the data changes
- Animate when the user navigates to the summary screen

---

## 🏷️ Create Categories (TODO)

---

## 📝 License

This project is for educational purposes.

---

## 🤝 Contributing

Feel free to fork this repository and practice on your own, or submit PRs with improvements!

---

**Happy Coding! 🎄✨**
