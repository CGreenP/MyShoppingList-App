# MyShoppingList App

A modern, user-friendly shopping list application built with Jetpack Compose for Android. This app helps users manage their shopping lists efficiently with a clean and intuitive interface.

Inspired by the Udemy course on [**The Complete Android 14 & Kotlin Development Masterclass**](https://www.udemy.com/course/android-kotlin-developer/) Created by [Denis Panjuta](https://www.udemy.com/user/denispanjuta/).

# Output Screenshot-
<p align="center">
<img src="https://github.com/user-attachments/assets/dfbc6260-683d-4560-b03d-0e354f49970e" width="288">
<img src="https://github.com/user-attachments/assets/96ff82a6-5331-4732-8374-493bce461eb5" width="288">
<img src="https://github.com/user-attachments/assets/ed5df56e-eaeb-4863-be8e-2f98c21e744f" width="288">
<img src="https://github.com/user-attachments/assets/3a3d89b0-2912-41bd-9227-744b36db8e1a" width="288">
</p>

# Output Video-
https://github.com/user-attachments/assets/06e38017-0241-49dc-b7e3-6b063be311ab

## Features

- ✨ Material Design 3 UI components
- 📝 Add, edit, and delete shopping items
- 🔢 Quantity management for each item
- 🔍 Input validation for item names and quantities
- ⚡ Smooth animations and transitions
- ⬆️ Quick scroll-to-top functionality
- 📱 Responsive layout design

## Tech Stack

- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern UI toolkit for Android
- **Material Design 3** - Latest Material Design components and theming
- **Coroutines** - For handling asynchronous operations
- **State Management** - Using Compose's built-in state management

## Architecture

The app follows modern Android development practices:

- **Single Activity Architecture** - Using Compose navigation
- **State Hoisting** - For better state management and reusability
- **Composable Functions** - Modular and reusable UI components
- **Data Classes** - For representing shopping items

## Key Components

### ShoppingItem Data Class
```kotlin
data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)
```

### Main Components
- `ShoppingListApp` - Main composable containing the app's logic and UI
- `ShoppingItemEditor` - Composable for editing shopping items
- `ShoppingListItem` - Composable for displaying individual shopping items

### Features in Detail

#### Adding Items
- Click the "Add Item" button
- Enter item name and quantity
- Input validation ensures:
  - Item name is not empty
  - Quantity is a valid number
  - All required fields are filled

#### Editing Items
- Tap the edit icon on any item
- Modify name or quantity
- Real-time validation
- Save changes with the check button

#### Deleting Items
- Tap the delete icon to remove items from the list
- No confirmation required (can be added if needed)

#### UI/UX Features
- Smooth scrolling with LazyColumn
- Scroll-to-top button appears when scrolling down
- Material Design 3 theming and components
- Responsive layout adapting to different screen sizes

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 21 or higher
- Kotlin 1.5.0 or higher

### Installation
1. Clone the repository:
```bash
git clone https://github.com/CGreenP/MyShoppingList-App.git
```
2. Open the project in Android Studio
3. Run the app on an emulator or physical device

## Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

## Acknowledgements
- Thanks to the [Jetpack Compose](https://developer.android.com/jetpack/compose) team for the amazing UI toolkit
- [Material Design 3](https://m3.material.io) guidelines for the beautiful design system
- Android developer community for inspiration and support
