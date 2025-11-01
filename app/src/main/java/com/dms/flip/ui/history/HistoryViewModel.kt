package com.dms.flip.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dms.flip.domain.model.PleasureHistory
import com.dms.flip.domain.usecase.weekly.GetWeeklyHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getWeeklyHistoryUseCase: GetWeeklyHistoryUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadWeeklyHistory(weekOffset = 0)
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.OnRetryClicked -> {
                loadWeeklyHistory(_uiState.value.weekOffset)
            }
            is HistoryEvent.OnCardClicked -> {
                _uiState.update { it.copy(selectedPleasureHistory = event.item) }
            }
            is HistoryEvent.OnBottomSheetDismissed -> {
                _uiState.update { it.copy(selectedPleasureHistory = null) }
            }
            is HistoryEvent.OnPreviousWeekClicked -> {
                val nextOffset = _uiState.value.weekOffset - 1
                loadWeeklyHistory(nextOffset)
            }
            is HistoryEvent.OnNextWeekClicked -> {
                val nextOffset = _uiState.value.weekOffset + 1
                loadWeeklyHistory(nextOffset)
            }
            is HistoryEvent.OnDiscoverTodayClicked -> {
                // Tu pourras déclencher ici l’UX “découvrir le flip du jour”
            }
        }
    }

    private fun loadWeeklyHistory(weekOffset: Int) = viewModelScope.launch {
        val (startMillis, endMillis) = weekBoundsMillis(weekOffset)
        val (weekTitle, weekDatesLabel) = weekLabels(weekOffset, startMillis, endMillis)

        getWeeklyHistoryUseCase()
            .onStart {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        weekOffset = weekOffset,
                        weekTitle = weekTitle,
                        weekDates = weekDatesLabel
                    )
                }
            }
            .catch { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message ?: "Une erreur inconnue est survenue"
                    )
                }
            }
            .collect { allEntries ->
                // Filtre local (si ton use case ne prend pas encore de bornes)
                val entriesOfWeek = allEntries.filter {
                    it.dateDrawn in startMillis until endMillis
                }

                val weeklyDays = generateFullWeek(startMillis, entriesOfWeek)
                val streak = computeStreak(entriesOfWeek, endMillis)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weeklyDays = weeklyDays,
                        streakDays = streak,
                        error = null
                    )
                }
            }
    }

    /** Retourne les bornes [lundi 00:00; lundi suivant 00:00) en ms pour weekOffset (0 = semaine courante). */
    private fun weekBoundsMillis(weekOffset: Int): Pair<Long, Long> {
        val cal = Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            // Place le curseur au lundi de la semaine courante
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            add(Calendar.WEEK_OF_YEAR, weekOffset)
        }
        val start = cal.timeInMillis
        cal.add(Calendar.WEEK_OF_YEAR, 1)
        val end = cal.timeInMillis
        return start to end
    }

    /** "Cette semaine"/"Semaine dernière"/"Semaine prochaine" + "17 – 23 juin". */
    private fun weekLabels(weekOffset: Int, start: Long, end: Long): Pair<String, String> {
        val title = when {
            weekOffset == 0 -> "Cette Semaine"
            weekOffset == -1 -> "Semaine dernière"
            weekOffset == 1 -> "Semaine prochaine"
            weekOffset < 0 -> "Il y a ${-weekOffset} semaines"
            else -> "Dans ${weekOffset} semaines"
        }
        val dfDay = SimpleDateFormat("d", Locale.getDefault())
        val dfMonth = SimpleDateFormat("MMM", Locale.getDefault())

        val cal = Calendar.getInstance()
        cal.timeInMillis = start
        val startDay = dfDay.format(cal.time)
        val startMonth = dfMonth.format(cal.time)

        cal.timeInMillis = end - 1
        val endDay = dfDay.format(cal.time)
        val endMonth = dfMonth.format(cal.time)

        val dates = if (startMonth == endMonth) {
            "$startDay – $endDay ${endMonth.replaceFirstChar { it.titlecase(Locale.getDefault()) }}"
        } else {
            "$startDay ${startMonth} – $endDay ${endMonth}"
        }
        return title to dates
    }

    /** Génère 7 jours avec correspondance d’entrée par date. */
    private fun generateFullWeek(
        weekStartMillis: Long,
        historyEntries: List<PleasureHistory>
    ): List<WeeklyDay> {
        val days = (0 until 7).map { offset ->
            val dayCal = Calendar.getInstance().apply {
                timeInMillis = weekStartMillis
                add(Calendar.DAY_OF_YEAR, offset)
            }
            val dayName = getDayName(offset + 1) // 1..7
            val entryForDay = historyEntries.find { entry ->
                sameDay(entry.dateDrawn, dayCal.timeInMillis)
            }
            WeeklyDay(dayName = dayName, historyEntry = entryForDay)
        }
        return days
    }

    private fun sameDay(a: Long, b: Long): Boolean {
        val ca = Calendar.getInstance().apply { timeInMillis = a }
        val cb = Calendar.getInstance().apply { timeInMillis = b }
        return ca.get(Calendar.YEAR) == cb.get(Calendar.YEAR) &&
                ca.get(Calendar.DAY_OF_YEAR) == cb.get(Calendar.DAY_OF_YEAR)
    }

    /** Streak simple sur la semaine affichée (du dernier jour complété consécutif jusqu’à "hier" max). */
    private fun computeStreak(entriesOfWeek: List<PleasureHistory>, weekEnd: Long): Int {
        if (entriesOfWeek.isEmpty()) return 0
        // On calcule depuis le jour affiché le plus récent vers l’arrière
        val cal = Calendar.getInstance().apply { timeInMillis = weekEnd - 1 }
        var streak = 0
        repeat(7) {
            val hasCompletedThatDay = entriesOfWeek.any { e ->
                e.completed && sameDay(e.dateDrawn, cal.timeInMillis)
            }
            if (hasCompletedThatDay) {
                streak++
                cal.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                return streak
            }
        }
        return streak
    }
}
