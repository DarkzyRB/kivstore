# KivStore - DataStore Wrapper Library

[![](https://jitpack.io/v/darkzyrb/kivstore.svg)](https://jitpack.io/#darkzyrb/kivstore)


**KivStore** is a lightweight library that simplifies the use of Android's DataStore. It provides a type-safe API based on delegates for preference management and also offers an easy way to handle Flow or LiveData observers for the created models.

---

## 📦 Features

- **Type-Safe Preferences:** Easily define preferences using property delegates (intType, stringType, booleanType, etc.).
- **Reactive Streams:** Observe preference changes using Kotlin Flow.
- **Thread-Safe:** All operations are Coroutine-aware and run on Dispatchers.IO by default.
- **Minimal Boilerplate:** No need to manually handle DataStore or Preferences.Key—just declare properties.
- **Memory Efficiency:** Uses buffer(Channel.BUFFERED) to handle backpressure smoothly.

---

## 🛠️ Installation

1. Add JitPack to your root `settings.gradle.kts` or `build.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

2. Add the module to your build.gradle.kts:

```kotlin
implementation("com.github.darkzyrb.kivstore:kivstore:v1.0.4")
```

---

## 🛠 Usage

**1. Initialize KivStore**

Call KivStore.init(context) in your Application class:

```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        KivStore.init(this)
    }
}
```

**2. Define a DataStore Model**

Extend KivStoreModel and declare properties using delegates:

```kotlin
object UserSettings : KivStoreModel() {
    var userId by intType(default = 0)
    var username by stringType(default = "guest")
    var isPremium by booleanType(default = false)
}
```

Available Delegates:

- intType()
- stringType()
- booleanType()
- doubleType()
- floatType()
- longType()

**3. Read/Write Values**

Access values like regular properties:

```kotlin
// Write
UserSettings.userId = 42
UserSettings.username = "dev_user"

// Read
val currentUser = UserSettings.username
```

**4. Observe Changes with Flow**

Use asFlow() or asLiveData to observe preference changes reactively:

```kotlin
UserSettings.asFlow(UserSettings::isPremium)
    .collect { isPremium ->
        println("Premium status changed: $isPremium")
    }
```

```kotlin
UserSettings.asLiveData(UserSettings::isPremium)
    .observe(this) { isPremium ->
        println("Premium status changed: $isPremium")
    }
```

*(Automatically handles backpressure with buffer(Channel.BUFFERED))*

---

## 📄 License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).