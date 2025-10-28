package com.dms.flip.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val DarkPrimary = Color(0xFFD0BCFF) // Le violet/mauve du FAB et de la nav
val DarkOnPrimary = Color(0xFF381E72)
val DarkPrimaryContainer = Color(0xFF4F378A) // Le tag "Pleine Conscience"
val DarkOnPrimaryContainer = Color(0xFFEADDFF)

val DarkSecondary = Color(0xFF67DFFF) // Le cyan/bleu du bouton "À demain"
val DarkOnSecondary = Color(0xFF00363D)
val DarkSecondaryContainer = Color(0xFF004F58)
val DarkOnSecondaryContainer = Color(0xFFB8EAFA)

val DarkTertiary = Color(0xFF66DDA3) // Le vert "En ligne"
val DarkOnTertiary = Color(0xFF003823)
val DarkTertiaryContainer = Color(0xFF005235)
val DarkOnTertiaryContainer = Color(0xFF83FAAE)

val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)

val DarkBackground = Color(0xFF1B1B1F) // Fond de l'app (très sombre)
val DarkOnBackground = Color(0xFFE5E1E6) // Texte principal (blanc cassé)
val DarkSurface = Color(0xFF2A292E) // Fond des cartes (ex: "Elodie Dubois")
val DarkOnSurface = Color(0xFFE5E1E6) // Texte sur les cartes
val DarkSurfaceVariant = Color(0xFF3B3A40) // Fond plus clair (ex: carte "Aya Nakamura")
val DarkOnSurfaceVariant = Color(0xFFCAC4D0) // Texte secondaire (ex: "Hors ligne")
val DarkOutline = Color(0xFF938F99) // Bordures subtiles

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    error = DarkError,
    onError = DarkOnError,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline
)

// 2. Thème Clair (Light Mode) - Généré à partir du Dark
// Tu peux le personnaliser davantage avec le M3 Theme Builder

val LightPrimary = Color(0xFF6750A4)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFEADDFF)
val LightOnPrimaryContainer = Color(0xFF22005D)

val LightSecondary = Color(0xFF006874)
val LightOnSecondary = Color(0xFFFFFFFF)
val LightSecondaryContainer = Color(0xFFB8EAFA)
val LightOnSecondaryContainer = Color(0xFF001F24)

val LightTertiary = Color(0xFF006D44)
val LightOnTertiary = Color(0xFFFFFFFF)
val LightTertiaryContainer = Color(0xFF83FAAE)
val LightOnTertiaryContainer = Color(0xFF002111)

val LightError = Color(0xFFB3261E)
val LightOnError = Color(0xFFFFFFFF)

val LightBackground = Color(0xFFFEFBFF) // Fond clair
val LightOnBackground = Color(0xFF1B1B1F)
val LightSurface = Color(0xFFF3EDF7) // Fond des cartes claires
val LightOnSurface = Color(0xFF1B1B1F)
val LightSurfaceVariant = Color(0xFFE7E0EC) // Fond plus clair
val LightOnSurfaceVariant = Color(0xFF49454E)
val LightOutline = Color(0xFF79747E)

val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    error = LightError,
    onError = LightOnError,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline
)
