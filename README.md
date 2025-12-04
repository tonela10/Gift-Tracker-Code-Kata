# Gift Tracker 🎁

A modern Android application for tracking gifts, built with **Jetpack Compose**, **Room**, **Hilt**, and following **Clean Architecture** principles.

## ✨ Features

- **Gift Management**: Add, view, edit, and delete gifts
- **Status Tracking**: Track gift lifecycle (💡 Idea → 🛒 Purchased → 🎀 Wrapped → 🎁 Given)
- **Occasion Support**: Categorize gifts by occasion (Birthday, Christmas, Wedding, etc.)
- **Smart Filtering**: Filter by status, search by name or recipient
- **Persistent Storage**: Room database for reliable data persistence
- **Modern UI**: Built with Jetpack Compose and Material 3 design
- **Smooth Animations**: Beautiful transitions and item animations
- **Unit Tested**: Comprehensive test coverage for ViewModels

## 🏗️ Architecture

This project follows **Clean Architecture** with **MVVM** pattern:

```
app/
├── data/
│   ├── local/
│   │   ├── GiftEntity.kt      # Room entity
│   │   ├── GiftDao.kt         # Data Access Object
│   │   ├── GiftDatabase.kt    # Room database
│   │   └── Converters.kt      # Type converters
│   └── repository/
│       └── GiftRepositoryImpl.kt
├── domain/
│   ├── model/
│   │   └── Gift.kt            # Domain model
│   └── repository/
│       └── GiftRepository.kt  # Repository interface
├── di/
│   ├── DatabaseModule.kt      # Hilt database module
│   └── RepositoryModule.kt    # Hilt repository binding
├── viewmodel/
│   ├── GiftListViewModel.kt
│   ├── GiftFormViewModel.kt
│   └── GiftDetailViewModel.kt
├── ui/
│   ├── screens/
│   │   ├── GiftListScreen.kt
│   │   ├── GiftFormScreen.kt
│   │   └── GiftDetailScreen.kt
│   ├── state/
│   │   └── GiftUiState.kt     # UI state classes
│   └── theme/
└── navigation/
    └── NavGraph.kt
```

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Kotlin |
| **UI** | Jetpack Compose + Material 3 |
| **Architecture** | MVVM + Clean Architecture |
| **DI** | Hilt |
| **Database** | Room |
| **Async** | Kotlin Coroutines + Flow |
| **Navigation** | Navigation Compose |
| **Testing** | JUnit + Coroutines Test |
| **Logging** | Timber |

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17 or higher
- Android SDK 35+

### Build & Run

1. Clone the repository:
   ```bash
   git clone https://github.com/Arafo/Gift-Tracker-Code-Kata.git
   ```

2. Open in Android Studio

3. Sync Gradle and run on emulator or device

### Command Line

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Run all checks
./gradlew check
```

## 📱 Screens

### Gift List
- Displays all gifts with status indicators
- Search and filter functionality
- Quick status advancement
- Statistics summary

### Add/Edit Gift
- Form with validation
- Status and occasion selection
- Optional notes field

### Gift Detail
- Full gift information
- Status progress visualization
- Edit and delete options

## 🔄 Gift Status Flow

```
💡 Idea  →  🛒 Purchased  →  🎀 Wrapped  →  🎁 Given
```

## 📋 Occasions

- 🎂 Birthday
- 🎄 Christmas
- 💕 Anniversary
- 💒 Wedding
- 🎓 Graduation
- ❤️ Valentine's Day
- 👩 Mother's Day
- 👨 Father's Day
- 🎁 Other

## 🧪 Testing

The project includes comprehensive unit tests:

```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests "GiftListViewModelTest"
```

Test coverage includes:
- ViewModel state management
- Filter and search logic
- Gift model behavior
- Validation logic

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

---

Built with ❤️ using Jetpack Compose
