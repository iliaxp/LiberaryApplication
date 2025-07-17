# 📚 Mahdi Shelf (Library Application)

A modern, feature-rich Android application for browsing, searching, and purchasing books, built with Jetpack Compose. This app demonstrates best practices in UI/UX, state management, and modular architecture.

---

**✨ Table of Contents**
- [🎯 Overview](#-overview)
- [🚀 Features](#-features)
- [🖼️ Screenshots](#-screenshots)
- [🏛️ Architecture](#-architecture)
- [📦 Data Models](#-data-models)
- [🔧 Setup & Installation](#-setup--installation)
- [▶️ Build & Run](#-build--run)
- [📚 Dependencies](#-dependencies)
- [🗂️ Folder Structure](#-folder-structure)
- [🤝 Contributing](#-contributing)
- [📝 License](#-license)

---

**🎯 OVERVIEW**

Welcome to **Mahdi Shelf**! 🌟

A sample library/bookstore app where you can:
- 📖 Browse a curated collection of books by category
- 🔍 Search, filter, and sort books
- 📝 View detailed information about each book
- 🛒 Add books to a shopping cart
- 👤 Register and onboard as a new user
- 💳 Complete a simulated checkout/payment process

Built with **Jetpack Compose** for a modern, declarative UI, and clean architecture principles.

---

**🚀 FEATURES**

- 🎬 Splash & Welcome Screens: Animated onboarding for first-time users
- 📝 User Registration: Simple registration form
- 🏠 Home/Library Screen:
  - 🖼️ Book list with image slider, category filter, and sorting
  - 🔎 Real-time search and filtering
- 📚 Book Details: Author, category, price, rating, and description
- 🛒 Cart: Add/remove books, update quantities, view total price
- 💳 Checkout/Payment: Simulated credit card payment with validation
- ⚡ State Management: ViewModel & Kotlin Flows
- 🎨 Material 3 Design: Modern, responsive UI with dark/light theme

---

**🖼️ SCREENSHOTS**

> _Add screenshots of the main screens here (Splash, Welcome, Library, Book Details, Cart, Payment)_

---

**🏛️ ARCHITECTURE**

- 🧩 **Jetpack Compose**: Declarative UI
- 🏗️ **MVVM**: ViewModel for state and business logic
- 🔄 **Kotlin Flows**: Reactive state updates
- 🧭 **Navigation**: Compose Navigation for screen transitions
- 🧱 **Modular Components**: Reusable UI (BookCard, CategoryButtons, etc.)

---

**📦 DATA MODELS**

```kotlin
// Book.kt
data class Book(
    val id: String,
    val name: String,
    val imageUrl: String,
    val price: Int,
    val category: Category,
    val description: String,
    val author: String = "",
    val rating: Float = 0f,
    val reviews: Int = 0
)

data class CartItem(
    val book: Book,
    val quantity: Int = 1
)

// Category.kt
enum class Category {
    ALL, SCIENCE, FICTION, HORROR, HISTORY, ROMANCE, DRAMA, FANTASY, SCIENCE_FICTION
}
```

---

**🔧 SETUP & INSTALLATION**

1. **Clone the repository:**
   ```sh
   git clone <repo-url>
   cd LiberaryApplication
   ```
2. **Open in Android Studio** (Giraffe or newer recommended)
3. **Sync Gradle** to download dependencies
4. **Run on emulator or device** (minSdk 24, targetSdk 35)

---

**▶️ BUILD & RUN**

- **From Android Studio:**
  - Click `Run` or use `Shift+F10`
- **From command line:**
  ```sh
  ./gradlew :app:assembleDebug
  ./gradlew :app:installDebug
  ```

---

**📚 DEPENDENCIES**

- [Jetpack Compose](https://developer.android.com/jetpack/compose) 🧩
- [Material 3](https://m3.material.io/) 🎨
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) 🧭
- [Coil](https://coil-kt.github.io/coil/compose/) 🖼️ (image loading)
- [Accompanist](https://google.github.io/accompanist/) 🛠️ (system UI)
- [Kotlin Coroutines & Flows](https://kotlinlang.org/docs/flow.html) 🔄
- [JUnit, Espresso](https://developer.android.com/training/testing) 🧪 (testing)

---

**🗂️ FOLDER STRUCTURE**

```
LiberaryApplication/
├── app/
│   ├── src/main/java/com/iliaxp/liberaryapplication/
│   │   ├── model/         # Data models (Book, Category, CartItem)
│   │   ├── ui/
│   │   │   ├── components/ # Reusable UI components
│   │   │   └── screens/    # App screens (Library, BookDetails, Cart, etc.)
│   │   ├── viewmodel/     # ViewModel and Factory
│   │   └── MainActivity.kt# App entry point
│   ├── res/               # Resources (drawables, layouts, values)
│   └── AndroidManifest.xml
├── build.gradle.kts       # Project-level Gradle config
├── settings.gradle.kts    # Project/module settings
└── ...
```

---

**🤝 CONTRIBUTING**

Contributions are welcome! Please open issues or pull requests for bug fixes, features, or improvements.

1. 🍴 Fork the repo
2. 🌱 Create a feature branch (`git checkout -b feature/your-feature`)
3. 💾 Commit your changes
4. 🚀 Push and open a PR

---

**📝 LICENSE**

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details. 
