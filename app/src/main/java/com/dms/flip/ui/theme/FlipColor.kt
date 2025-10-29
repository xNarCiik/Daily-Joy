package com.dms.flip.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ========== DARK THEME COLORS (Basé sur le HTML de la maquette) ==========

val DarkPrimary = Color(0xFFA8C7FA) // Blue accent (#a8c7fa) - CORRIGÉ depuis violet
val DarkOnPrimary = Color(0xFF00315C) // Bleu très foncé pour texte sur primary
val DarkPrimaryContainer = Color(0xFF004A9B) // hsl(215, 89%, 35%) converti - Bleu foncé container
val DarkOnPrimaryContainer = Color(0xFFD4E3FF) // #d4e3ff - Texte sur primary container

val DarkSecondary = Color(0xFF67DFFF) // Le cyan/bleu du bouton "À demain"
val DarkOnSecondary = Color(0xFF00363D)
val DarkSecondaryContainer = Color(0xFF004F58)
val DarkOnSecondaryContainer = Color(0xFFB8EAFA)

val DarkTertiary = Color(0xFF66DDA3) // Le vert "En ligne"
val DarkOnTertiary = Color(0xFF003823)
val DarkTertiaryContainer = Color(0xFF005235)
val DarkOnTertiaryContainer = Color(0xFF83FAAE)

val DarkError = Color(0xFFF2B8B5) // #f2b8b5 - CORRIGÉ depuis #FFB4AB
val DarkOnError = Color(0xFF601410) // #601410

val DarkBackground = Color(0xFF1C1B1F) // #1c1b1f - Fond de l'app
val DarkOnBackground = Color(0xFFE6E1E5) // #e6e1e5 - CORRIGÉ depuis #E5E1E6
val DarkSurface = Color(0xFF2A292D) // #2a292d - CORRIGÉ depuis #2A292E - Fond des cartes
val DarkOnSurface = Color(0xFFE6E1E5) // #e6e1e5 - Texte sur les cartes
val DarkSurfaceVariant = Color(0xFF3B3A40) // #3B3A40 - Fond plus clair
val DarkOnSurfaceVariant = Color(0xFFCAC4D0) // #cac4d0 - Texte secondaire
val DarkSurfaceContainer = Color(0xFF211F26) // #211F26 - Container navbar (plus foncé, proche du background)
val DarkOutline = Color(0xFF938F99) // #938f99 - Bordures subtiles

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
    surfaceContainer = DarkSurfaceContainer,
    outline = DarkOutline
)

// ========== LIGHT THEME COLORS ==========
// TODO: À ajuster plus tard si nécessaire pour le mode clair

val LightPrimary = Color(0xFF2B6CB0) // Bleu moyen pour le light mode
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFD4E3FF)
val LightOnPrimaryContainer = Color(0xFF001B3D)

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

val LightBackground = Color(0xFFF6F7F8) // #f6f7f8 du HTML
val LightOnBackground = Color(0xFF1B1B1F)
val LightSurface = Color(0xFFFAFAFA) // Fond des cartes claires
val LightOnSurface = Color(0xFF1B1B1F)
val LightSurfaceVariant = Color(0xFFE7E0EC) // Fond plus clair
val LightOnSurfaceVariant = Color(0xFF49454E)
val LightSurfaceContainer = Color(0xFFECECEC) // Container cards light
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
    surfaceContainer = LightSurfaceContainer,
    outline = LightOutline
)
