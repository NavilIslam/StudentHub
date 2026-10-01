package com.navil.studenthub.util

import com.navil.studenthub.model.AcademicDayOfWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object DateUtils {

    private val timeFormatter12Hour = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
    private val timeFormatter24Hour = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
    private val fullDateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
    private val todayHeroFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH)
    private val shortDateFormatter = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH)
    private val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)
    private val calendarSelectedDateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH)

    fun formatMonthYear(yearMonth: java.time.YearMonth): String {
        return yearMonth.format(monthYearFormatter)
    }

    fun formatCalendarSelectedDate(date: LocalDate): String {
        return date.format(calendarSelectedDateFormatter)
    }

    fun getTodayAcademicDay(): AcademicDayOfWeek {
        val today = LocalDate.now()
        return AcademicDayOfWeek.fromJavaDayOfWeek(today.dayOfWeek)
    }

    fun getFormattedTodayDate(): String {
        return LocalDate.now().format(todayHeroFormatter)
    }

    data class DayBubbleInfo(
        val dayOfWeek: AcademicDayOfWeek,
        val dayNumber: Int,
        val localDate: LocalDate,
        val isToday: Boolean
    )

    fun getDaysForWeek(weekOffset: Long = 0): List<DayBubbleInfo> {
        val today = LocalDate.now()
        val currentDayOfWeek = today.dayOfWeek
        val daysSinceSunday = if (currentDayOfWeek == DayOfWeek.SUNDAY) 0L else currentDayOfWeek.value.toLong()
        val startOfAcademicWeek = today.minusDays(daysSinceSunday).plusWeeks(weekOffset)

        return AcademicDayOfWeek.entries.map { academicDay ->
            val date = startOfAcademicWeek.plusDays(academicDay.dayIndex.toLong())
            DayBubbleInfo(
                dayOfWeek = academicDay,
                dayNumber = date.dayOfMonth,
                localDate = date,
                isToday = date == today
            )
        }
    }

    fun getWeekHeaderTitle(weekOffset: Long = 0): String {
        val today = LocalDate.now()
        val currentDayOfWeek = today.dayOfWeek
        val daysSinceSunday = if (currentDayOfWeek == DayOfWeek.SUNDAY) 0L else currentDayOfWeek.value.toLong()
        val startOfAcademicWeek = today.minusDays(daysSinceSunday).plusWeeks(weekOffset)
        val endOfAcademicWeek = startOfAcademicWeek.plusDays(6)

        val startMonth = startOfAcademicWeek.format(DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH))
        val endMonth = if (startOfAcademicWeek.month == endOfAcademicWeek.month) {
            "${endOfAcademicWeek.dayOfMonth}"
        } else {
            endOfAcademicWeek.format(DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH))
        }

        return "Week of $startMonth–$endMonth"
    }

    fun formatTimeMinutes(minutesOfDay: Int): String {
        val hours = minutesOfDay / 60
        val mins = minutesOfDay % 60
        val localTime = LocalTime.of(hours.coerceIn(0, 23), mins.coerceIn(0, 59))
        return localTime.format(timeFormatter12Hour)
    }

    fun parseTimeToMinutes(timeString: String): Int? {
        return try {
            val trimmed = timeString.trim()
            val parsed = if (trimmed.contains("AM", ignoreCase = true) || trimmed.contains("PM", ignoreCase = true)) {
                LocalTime.parse(trimmed, DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH))
            } else {
                LocalTime.parse(trimmed, timeFormatter24Hour)
            }
            parsed.hour * 60 + parsed.minute
        } catch (e: Exception) {
            null
        }
    }

    fun formatEpochDay(epochDay: Long): String {
        val localDate = LocalDate.ofEpochDay(epochDay)
        return localDate.format(fullDateFormatter)
    }

    fun getRelativeDueDate(dueDateEpochDay: Long): RelativeDueDate {
        val todayEpochDay = getTodayEpochDay()
        val diff = dueDateEpochDay - todayEpochDay

        return when {
            diff < 0 -> RelativeDueDate(
                text = "OVERDUE • Due ${-diff} ${if (-diff == 1L) "day" else "days"} ago",
                isOverdue = true,
                isUrgent = true,
                groupKey = "OVERDUE"
            )
            diff == 0L -> RelativeDueDate(
                text = "Due today",
                isOverdue = false,
                isUrgent = true,
                groupKey = "TODAY"
            )
            diff == 1L -> RelativeDueDate(
                text = "Due tomorrow",
                isOverdue = false,
                isUrgent = false,
                groupKey = "TOMORROW"
            )
            diff in 2..6 -> {
                val dayName = LocalDate.ofEpochDay(dueDateEpochDay).format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH))
                RelativeDueDate(
                    text = "Due $dayName",
                    isOverdue = false,
                    isUrgent = false,
                    groupKey = "THIS WEEK"
                )
            }
            else -> RelativeDueDate(
                text = formatEpochDay(dueDateEpochDay),
                isOverdue = false,
                isUrgent = false,
                groupKey = "LATER"
            )
        }
    }

    data class RelativeDueDate(
        val text: String,
        val isOverdue: Boolean,
        val isUrgent: Boolean,
        val groupKey: String = "UPCOMING"
    )

    fun isClassCurrentlyActive(dayOfWeek: AcademicDayOfWeek, startTimeMinutes: Int, endTimeMinutes: Int): Boolean {
        if (dayOfWeek != getTodayAcademicDay()) return false
        val currentMinutes = getCurrentTimeMinutes()
        return currentMinutes in startTimeMinutes..endTimeMinutes
    }

    fun getClassCountdownText(dayOfWeek: AcademicDayOfWeek, startTimeMinutes: Int, endTimeMinutes: Int): String? {
        if (dayOfWeek != getTodayAcademicDay()) return null
        val currentMinutes = getCurrentTimeMinutes()
        return when {
            currentMinutes in startTimeMinutes..endTimeMinutes -> "HAPPENING NOW"
            currentMinutes < startTimeMinutes -> {
                val diff = startTimeMinutes - currentMinutes
                if (diff < 60) "Starts in $diff min"
                else "Starts in ${diff / 60}h ${diff % 60}m"
            }
            else -> null
        }
    }

    fun getCurrentTimeMinutes(): Int {
        val now = LocalTime.now()
        return now.hour * 60 + now.minute
    }

    fun getTodayEpochDay(): Long {
        return LocalDate.now().toEpochDay()
    }

    fun formatDuration(totalSeconds: Long): String {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds)
    }

    fun formatMinutesToHoursAndMinutes(totalMinutes: Long): String {
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return if (hours > 0) {
            "${hours}h ${minutes}m"
        } else {
            "${minutes}m"
        }
    }

    fun getStartOfDayEpochMs(localDate: LocalDate = LocalDate.now()): Long {
        return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun getStartOfWeekEpochMs(): Long {
        val today = LocalDate.now()
        val currentDayOfWeek = today.dayOfWeek
        val daysSinceSunday = if (currentDayOfWeek == DayOfWeek.SUNDAY) 0L else currentDayOfWeek.value.toLong()
        val startOfAcademicWeek = today.minusDays(daysSinceSunday)
        return startOfAcademicWeek.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    enum class ExamStatus {
        UPCOMING,
        TODAY,
        HAPPENING_NOW,
        COMPLETED,
        PAST
    }

    fun getExamStatus(
        dateEpochDay: Long,
        startTimeMinutes: Int,
        endTimeMinutes: Int? = null,
        isCompleted: Boolean = false
    ): ExamStatus {
        if (isCompleted) return ExamStatus.COMPLETED

        val todayEpoch = getTodayEpochDay()
        if (dateEpochDay < todayEpoch) return ExamStatus.PAST
        if (dateEpochDay > todayEpoch) return ExamStatus.UPCOMING

        // Today
        val currentMinutes = getCurrentTimeMinutes()
        val end = endTimeMinutes ?: (startTimeMinutes + 120)
        return when {
            currentMinutes in startTimeMinutes..end -> ExamStatus.HAPPENING_NOW
            currentMinutes > end -> ExamStatus.PAST
            else -> ExamStatus.TODAY
        }
    }

    fun getExamCountdownText(
        dateEpochDay: Long,
        startTimeMinutes: Int,
        endTimeMinutes: Int? = null,
        isCompleted: Boolean = false
    ): String {
        if (isCompleted) return "COMPLETED"

        val todayEpoch = getTodayEpochDay()
        val diffDays = dateEpochDay - todayEpoch

        if (diffDays < 0) return "PAST"

        if (diffDays == 0L) {
            val currentMinutes = getCurrentTimeMinutes()
            val end = endTimeMinutes ?: (startTimeMinutes + 120)
            return when {
                currentMinutes in startTimeMinutes..end -> "HAPPENING NOW"
                currentMinutes > end -> "TODAY • Finished"
                startTimeMinutes - currentMinutes in 1..59 -> "TODAY • In ${startTimeMinutes - currentMinutes}m"
                else -> "TODAY"
            }
        }

        if (diffDays == 1L) return "1 DAY LEFT"
        return "$diffDays DAYS LEFT"
    }

    fun getDaysUntilExam(dateEpochDay: Long): Long {
        val todayEpoch = getTodayEpochDay()
        return (dateEpochDay - todayEpoch).coerceAtLeast(0L)
    }
}
