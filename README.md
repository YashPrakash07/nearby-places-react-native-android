# 📍 Nearby Places App

A React Native application that displays nearby places on a map using **Android Native MVVM architecture** for data handling.

## 🏗️ Architecture Overview

This project follows a **hybrid architecture** combining:

- **React Native** → UI Layer (Map, Markers, Loading states)
- **Android Native (Kotlin)** → Business Logic following MVVM pattern

```
┌─────────────────────────────────────────────────────────────────┐
│                         REACT NATIVE                            │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  App.js                                                  │   │
│  │  • MapView with Markers                                  │   │
│  │  • Loading indicator                                     │   │
│  │  • Error handling via Alert                              │   │
│  └─────────────────────────────────────────────────────────┘   │
│                              │                                  │
│                     NativeModules.PlacesModule                  │
└──────────────────────────────┼──────────────────────────────────┘
                               │
┌──────────────────────────────┼──────────────────────────────────┐
│                      ANDROID NATIVE (MVVM)                      │
│                              │                                  │
│  ┌───────────────────────────▼───────────────────────────────┐ │
│  │  Bridge Layer                                              │ │
│  │  └── PlacesModule.kt (React Native ↔ Android)             │ │
│  └───────────────────────────┬───────────────────────────────┘ │
│                              │                                  │
│  ┌───────────────────────────▼───────────────────────────────┐ │
│  │  ViewModel                                                 │ │
│  │  └── PlacesViewModel.kt                                    │ │
│  │      • Exposes LiveData<Result<List<Place>>>               │ │
│  │      • Handles coroutine scope                             │ │
│  │      • Manages success/failure states                      │ │
│  └───────────────────────────┬───────────────────────────────┘ │
│                              │                                  │
│  ┌───────────────────────────▼───────────────────────────────┐ │
│  │  Repository                                                │ │
│  │  └── PlacesRepository.kt                                   │ │
│  │      • Single source of truth for places data              │ │
│  │      • Coordinates with LocationProvider                   │ │
│  │      • Returns nearby places based on location             │ │
│  └───────────────────────────┬───────────────────────────────┘ │
│                              │                                  │
│  ┌───────────────────────────▼───────────────────────────────┐ │
│  │  Data Sources                                              │ │
│  │  ├── LocationProvider.kt (FusedLocationProviderClient)     │ │
│  │  └── Place.kt (Data Model)                                 │ │
│  └───────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

## 📁 Project Structure

```
NearbyPlacesApp/
├── App.js                          # React Native UI
├── package.json
└── android/app/src/main/java/com/nearbyplaces/
    ├── bridge/
    │   └── PlacesModule.kt         # RN ↔ Native Bridge
    ├── viewmodel/
    │   └── PlacesViewModel.kt      # ViewModel (business logic)
    ├── repository/
    │   └── PlacesRepository.kt     # Data repository
    ├── location/
    │   └── LocationProvider.kt     # Location services wrapper
    └── model/
        └── Place.kt                # Data model
```

## 🔧 MVVM Components Explained

### 1. Model (`Place.kt`)

```kotlin
data class Place(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val distanceMeters: Double
)
```

Simple data class representing a nearby place.

### 2. ViewModel (`PlacesViewModel.kt`)

- Uses `LiveData` for reactive state management
- Wraps results in `Result<T>` for success/failure handling
- Launches coroutines via `viewModelScope`
- **Single responsibility**: Orchestrate data loading and expose state

### 3. Repository (`PlacesRepository.kt`)

- **Single source of truth** for places data
- Coordinates with `LocationProvider` to get current location
- Returns nearby places based on user's coordinates
- Easily extendable to add API calls or local caching

### 4. LocationProvider (`LocationProvider.kt`)

- Wraps `FusedLocationProviderClient` for location services
- Uses Kotlin coroutines (`suspendCancellableCoroutine`)
- Handles location unavailability gracefully

### 5. Bridge (`PlacesModule.kt`)

- Exposes native functionality to React Native via `@ReactMethod`
- Handles permission checking
- Converts Kotlin data to React Native compatible format (`WritableArray`/`WritableMap`)

## 🚀 Getting Started

### Prerequisites

- Node.js ≥ 16
- Android Studio with SDK 33+
- React Native CLI

### Installation

```bash
# Install dependencies
npm install

# Start Metro bundler
npx react-native start

# Run on Android (in another terminal)
npx react-native run-android
```

### Permissions

The app requires location permission. Add to `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

## 📊 Data Flow

1. **React Native** calls `PlacesModule.fetchNearbyPlaces()`
2. **PlacesModule** checks location permissions
3. **PlacesViewModel** is instantiated with repository
4. **PlacesViewModel.load()** triggers async data fetch
5. **PlacesRepository** calls **LocationProvider** for coordinates
6. **PlacesRepository** returns nearby places list
7. **ViewModel** updates `LiveData` with result
8. **PlacesModule** observes LiveData and resolves Promise
9. **React Native** receives data and renders markers

## 🛡️ Error Handling

| Layer      | Error Handling                          |
| ---------- | --------------------------------------- |
| UI (RN)    | `Alert.alert()` for user feedback       |
| ViewModel  | `Result.failure()` wrapper              |
| Repository | Exception propagation                   |
| Location   | Custom `LOCATION_UNAVAILABLE` exception |
| Bridge     | Promise rejection with error codes      |

## 📝 License

MIT License
