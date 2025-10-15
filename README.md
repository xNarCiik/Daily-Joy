# 🌞 Daily Joy — Les petits plaisirs de ta journée

---

## ✨ Concept

**Daily Joy** est une application Android développée avec **Jetpack Compose** et **Material 3**, qui t’invite à tirer chaque jour une ou plusieurs cartes représentant tes **plaisirs du jour** 🎉  
L’idée : s’accorder un moment rien qu’à soi, sans culpabilité, avec une expérience ludique et colorée.

---

## 🪄 Fonctionnalités principales

| 🌟 Fonction | 🧩 Détails |
|--------------|------------|
| **Tirage de carte** | Animation de carte retournée pour découvrir un plaisir du jour |
| **Plaisirs personnalisables** | Liste préremplie + ajout des tiens (Food, Sport, Divertissement, etc.) |
| **Modes configurables** | Choisis ton rythme : 1-3 plaisirs par jour ou 7-21 par semaine |
| **Historique hebdomadaire** | Visualise tes plaisirs accomplis dans une timeline animée |
| **Notifications** | Rappel quotidien avec message motivant 💬 |
| **Thème fun & moderne** | Design Material 3 dynamique, coloré et fluide, mode clair/sombre automatique |

---

## 🧱 Stack technique

| Domaine | Outils |
|----------|--------|
| Langage | **Kotlin (100%)** |
| UI | **Jetpack Compose**, **Material 3**, **MotionLayout**, **Lottie Compose** |
| Architecture | **MVVM + Clean Architecture** |
| Navigation | **Compose Navigation** |
| State management | **StateFlow / MutableState** |
| DI | **Hilt** |
| Images | **Coil** |
| Data | **Room / DataStore Preferences** |
| Build | **Gradle Kotlin DSL** |

---

---

## ⚙️ Structure du projet

com.damien.dailyjoy/
│
├── ui/
│ ├── deck/ # Écran principal (cartes)
│ ├── detail/ # Écran détail plaisir
│ ├── history/ # Historique hebdo
│ ├── settings/ # Paramètres et thèmes
│ └── components/ # UI réutilisables
│
├── data/
│ ├── model/ # Classes: Pleasure, Category, DayHistory
│ ├── local/ # Room / DataStore
│ └── repository/ # Gestion des données
│
├── viewmodel/
│ ├── DeckViewModel.kt
│ ├── HistoryViewModel.kt
│ └── SettingsViewModel.kt
│
└── utils/
  ├── AnimationUtils.kt
  └── ThemeUtils.kt

---

## 🖼️ Screenshots (à venir)

---

## 🚀 Installation

```bash
git clone https://github.com/xNarCiik/Daily-Joy.git
cd DailyJoy
./gradlew assembleDebug
```

---

## 👨‍💻 Auteur

👋 Damien — Développeur mobile freelance

“J’aime créer des expériences visuelles qui inspirent autant qu’elles impressionnent.”

[📫 LinkedIn](https://www.linkedin.com/in/damien-legagnoux)
