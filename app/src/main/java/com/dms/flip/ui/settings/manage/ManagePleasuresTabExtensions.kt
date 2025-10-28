package com.dms.flip.ui.settings.manage

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dms.flip.R

val ManagePleasuresTab.displayName: String
    @Composable
    get() = when (this) {
        ManagePleasuresTab.SMALL -> stringResource(R.string.small_pleasures)
        ManagePleasuresTab.BIG -> stringResource(R.string.big_pleasures)
    }

val ManagePleasuresTab.description: String
    @Composable
    get() = when (this) {
        ManagePleasuresTab.SMALL -> stringResource(R.string.small_pleasures_description)
        ManagePleasuresTab.BIG -> stringResource(R.string.big_pleasures_description)
    }

val ManagePleasuresTab.emptyStateMessage: String
    @Composable
    get() = when (this) {
        ManagePleasuresTab.SMALL -> stringResource(R.string.small_pleasures_empty_state_message)
        ManagePleasuresTab.BIG -> stringResource(R.string.big_pleasures_empty_state_message)
    }
