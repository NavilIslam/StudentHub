package com.navil.studenthub.model

/**
 * Represents days of the academic week.
 * In Bangladeshi university academic schedules, Sunday is the first day of the week.
 */
enum class AcademicDayOfWeek(val dayIndex: Int, val displayName: String, val shortName: String) {
    SUNDAY(0, "Sunday", "Sun"),
    MONDAY(1, "Monday", "Mon"),
    TUESDAY(2, "Tuesday", "Tue"),
    WEDNESDAY(3, "Wednesday", "Wed"),
    THURSDAY(4, "Thursday", "Thu"),
    FRIDAY(5, "Friday", "Fri"),
    SATURDAY(6, "Saturday", "Sat");

    companion object {
        fun fromIndex(index: Int): AcademicDayOfWeek {
            return entries.firstOrNull { it.dayIndex == index } ?: SUNDAY
        }

        fun fromJavaDayOfWeek(dayOfWeek: java.time.DayOfWeek): AcademicDayOfWeek {
            return when (dayOfWeek) {
                java.time.DayOfWeek.SUNDAY -> SUNDAY
                java.time.DayOfWeek.MONDAY -> MONDAY
                java.time.DayOfWeek.TUESDAY -> TUESDAY
                java.time.DayOfWeek.WEDNESDAY -> WEDNESDAY
                java.time.DayOfWeek.THURSDAY -> THURSDAY
                java.time.DayOfWeek.FRIDAY -> FRIDAY
                java.time.DayOfWeek.SATURDAY -> SATURDAY
            }
        }
    }
}
