# Crypto Portfolio App

A modern Android application for tracking cryptocurrency portfolios, built with **MVVM + Clean Architecture**, **Jetpack Compose**, and following Android best practices.

## Architecture

This project implements **Clean Architecture** with **MVVM (Model-View-ViewModel)** pattern, ensuring:

- **Separation of Concerns**: Each layer has a single responsibility
- **Dependency Inversion**: Higher-level modules don't depend on lower-level modules
- **Testability**: Easy to unit test individual components
- **Maintainability**: Code is organized and easy to modify
- **Scalability**: Architecture supports feature growth

### Architecture Layers

```
┌─────────────────────────────────────┐
│            Presentation             │  ← UI Layer (Compose + ViewModels)
├─────────────────────────────────────┤
│              Domain                 │  ← Business Logic (Use Cases + Models)
├─────────────────────────────────────┤
│               Data                  │  ← Data Sources (Repository + API/DB)
└─────────────────────────────────────┘
```

## Tech Stack

- **UI**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Dagger Hilt
- **Navigation**: Compose Navigation
- **State Management**: Compose State + ViewModel
- **Language**: Kotlin
- **Async**: Coroutines + Flow
- **Networking**: Retrofit (assumed)
- **Local Storage**: Room Database (assumed)

## 📁 Project Structure

```
app/
├── manifests/
│   └── AndroidManifest.xml
├── kotlin+java/
│   └── com.app.cryptoportfolio/
│       ├── data/                          # Data Layer
│       │   ├── dto/                       # Data Transfer Objects
│       │   ├── repository/                # Repository Implementations
│       │   └── source/                    # Data Sources (API, Database)
│       ├── di/                            # Dependency Injection
│       │   └── DataModule.kt              # Hilt Modules
│       ├── domain/                        # Domain Layer (Business Logic)
│       │   ├── model/                     # Domain Models
│       │   ├── repository/                # Repository Interfaces
│       │   └── usecase/                   # Use Cases
│       ├── navigation/                    # Navigation Setup
│       │   ├── CryptoNavHost.kt          # Navigation Host
│       │   ├── NavigationExtensions.kt    # Navigation Helpers
│       │   └── Routes.kt                  # Route Definitions
│       ├── presentation/                  # Presentation Layer
│       │   ├── components/                # Reusable UI Components
│       │   ├── screen/                    # Screen Composables
│       │   │   ├── AnalyticsScreen.kt
│       │   │   ├── ExchangeScreen.kt
│       │   │   ├── PortfolioScreen.kt
│       │   │   ├── RecordScreen.kt
│       │   │   └── WalletScreen.kt
│       │   ├── state/                     # UI State Classes
│       │   │   ├── AnalyticsUIState.kt
│       │   │   ├── ExchangeUIState.kt
│       │   │   ├── PortfolioUIState.kt
│       │   │   ├── RecordUIState.kt
│       │   │   ├── UIState.kt
│       │   │   └── WalletUIState.kt
│       │   └── viewmodel/                 # ViewModels
│       ├── ui/                            # UI Theme & Components
│       │   ├── CryptoPortfolioApplication.kt
│       │   └── MainActivity.kt
│       └── CryptoPortfolioApplication.kt  # Application Class
```

## Features

- **Portfolio Management**: Track your cryptocurrency investments
- **Analytics Dashboard**: Visualize portfolio performance
- **Exchange Integration**: View exchange rates and market data
- **Transaction Records**: Keep track of buy/sell transactions
- **Wallet Management**: Manage multiple crypto wallets
- **Real-time Updates**: Live cryptocurrency prices
- **Modern UI**: Beautiful Material Design 3 interface

## 🔧 Setup & Installation

### Clone the Repository

```bash
git clone [https://github.com/sarojsahu-dev/Crypto-Portfolio.git]
cd crypto-portfolio-app
```

### Build and Run

1. **Open in Android Studio**
   ```bash
   # Open Android Studio and select "Open an existing project"
   # Navigate to the cloned repository folder
   ```

2. **Sync Project**
   - Android Studio will automatically start syncing
   - Wait for Gradle sync to complete




## Navigation Structure

The app uses **Type-Safe Navigation** with Compose Navigation:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Portfolio      │◄──►│   Analytics     │◄──►│    Exchange     │
│    Screen       │    │     Screen      │    │     Screen      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         ▲                        ▲                        ▲
         │                        │                        │
         ▼                        ▼                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     Record      │    │     Wallet      │    │   Other Screens │
│     Screen      │    │     Screen      │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```


### Test Structure
- **Unit Tests**: Domain layer (Use Cases, Models)
- **Integration Tests**: Repository and ViewModel tests
- **UI Tests**: Compose UI tests for screens

## State Management

The app uses **Unidirectional Data Flow (UDF)**:

```
User Action → ViewModel → Use Case → Repository → Data Source
     ↑                                                   │
UI State ←── ViewModel ←── Domain Model ←── Data ←───────┘
```

## UI/UX Features

- **Material Design 3**: Modern, accessible design system
- **Dark/Light Theme**: Automatic theme switching
- **Responsive Layout**: Adapts to different screen sizes
- **Smooth Animations**: Polished user interactions
- **Accessibility**: Full screen reader and navigation support

## Used

- [Jetpack Compose](https://developer.android.com/jetpack/compose) for modern UI toolkit
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) by Uncle Bob
- [Android Architecture Guidelines](https://developer.android.com/topic/architecture)
- [Material Design 3](https://m3.material.io/)

