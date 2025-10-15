package com.dms.dailyjoy.data.local

import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.data.model.PleasureType

object LocalPleasureDataSource {

    val pleasure = listOf(
        // --- Small Pleasure ---
        Pleasure(
            id = 1,
            title = "Boire un bon café en terrasse",
            description = "Profite d’un moment calme avec ton café préféré.",
            type = PleasureType.SMALL,
            category = PleasureCategory.RELAX
        ),
        Pleasure(
            id = 2,
            title = "Regarder un épisode de ta série préférée",
            description = "Sans culpabiliser, juste chill.",
            type = PleasureType.SMALL,
            category = PleasureCategory.CULTURE
        ),
        Pleasure(
            id = 3,
            title = "Faire une sieste de 20 minutes",
            description = "Recharge ton énergie, ton cerveau te dira merci.",
            type = PleasureType.SMALL,
            category = PleasureCategory.RELAX
        ),
        Pleasure(
            id = 4,
            title = "Écouter ton son préféré à fond",
            description = "Volume au max, vibes garanties.",
            type = PleasureType.SMALL,
            category = PleasureCategory.CREATIVE
        ),
        Pleasure(
            id = 5,
            title = "Appeler un ami pour papoter",
            description = "Juste discuter, sans raison particulière.",
            type = PleasureType.SMALL,
            category = PleasureCategory.SOCIAL
        ),
        Pleasure(
            id = 6,
            title = "Manger un dessert que tu kiffes",
            description = "Oublie la diète, régale-toi.",
            type = PleasureType.SMALL,
            category = PleasureCategory.FOOD
        ),
        Pleasure(
            id = 7,
            title = "Prendre un bain chaud",
            description = "Lâche prise, c’est ton moment.",
            type = PleasureType.SMALL,
            category = PleasureCategory.RELAX
        ),
        Pleasure(
            id = 8,
            title = "Jouer une partie de ton jeu préféré",
            description = "Offre-toi un petit moment gaming.",
            type = PleasureType.SMALL,
            category = PleasureCategory.GAME
        ),
        Pleasure(
            id = 9,
            title = "Aller marcher 15 minutes dehors",
            description = "Respire, déconnecte, regarde le ciel.",
            type = PleasureType.SMALL,
            category = PleasureCategory.NATURE
        ),
        Pleasure(
            id = 10,
            title = "Écrire quelque chose de positif",
            description = "3 trucs cools que t’as vécus aujourd’hui.",
            type = PleasureType.SMALL,
            category = PleasureCategory.CREATIVE
        ),

        // --- Big Pleasure ---
        Pleasure(
            id = 11,
            title = "Aller au restaurant",
            description = "Un vrai repas plaisir, sans regarder le prix.",
            type = PleasureType.BIG,
            category = PleasureCategory.FOOD
        ),
        Pleasure(
            id = 12,
            title = "Faire une journée sans téléphone",
            description = "Déconnexion totale, juste toi et la vraie vie.",
            type = PleasureType.BIG,
            category = PleasureCategory.RELAX
        ),
        Pleasure(
            id = 13,
            title = "Partir en week-end improvisé",
            description = "Attrape ton sac, choisis une destination, go.",
            type = PleasureType.BIG,
            category = PleasureCategory.NATURE
        ),
        Pleasure(
            id = 14,
            title = "Acheter quelque chose que tu veux depuis longtemps",
            description = "Un cadeau pour toi, mérité.",
            type = PleasureType.BIG,
            category = PleasureCategory.SHOPPING
        ),
        Pleasure(
            id = 15,
            title = "Regarder un film au cinéma",
            description = "Popcorn, son Dolby, immersion totale.",
            type = PleasureType.BIG,
            category = PleasureCategory.CULTURE
        ),
        Pleasure(
            id = 16,
            title = "Organiser un dîner entre amis",
            description = "Rires, bouffe, souvenirs à la clé.",
            type = PleasureType.BIG,
            category = PleasureCategory.SOCIAL
        ),
        Pleasure(
            id = 17,
            title = "Faire un massage ou une séance spa",
            description = "Détente maximale, recharge complète.",
            type = PleasureType.BIG,
            category = PleasureCategory.RELAX
        ),
        Pleasure(
            id = 18,
            title = "Aller à un concert ou festival",
            description = "Vis la musique en vrai.",
            type = PleasureType.BIG,
            category = PleasureCategory.CULTURE
        ),
        Pleasure(
            id = 19,
            title = "Commencer un projet créatif",
            description = "Peindre, écrire, rapper, coder… exprime-toi.",
            type = PleasureType.BIG,
            category = PleasureCategory.CREATIVE
        ),
        Pleasure(
            id = 20,
            title = "Faire un cheat meal XXL",
            description = "Ton plat préféré sans aucune culpabilité.",
            type = PleasureType.BIG,
            category = PleasureCategory.FOOD
        )
    )
}
