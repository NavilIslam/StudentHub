package com.navil.studenthub

import com.navil.studenthub.data.local.entity.ClassEntity
import com.navil.studenthub.data.local.entity.ExamEntity
import com.navil.studenthub.data.local.entity.TaskEntity
import com.navil.studenthub.model.AcademicCalendarEvent
import com.navil.studenthub.model.AcademicDayOfWeek
import com.navil.studenthub.model.CalendarEventType
import com.navil.studenthub.model.CalendarEventTypeFilter
import com.navil.studenthub.model.Priority
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class CalendarAggregationTest {

    @Test
    fun testSundayFirstDayOfWeekMapping() {
        // Test that Sunday maps to 0 and is the first day of the academic week
        assertEquals(0, AcademicDayOfWeek.SUNDAY.dayIndex)
        assertEquals(1, AcademicDayOfWeek.MONDAY.dayIndex)
        assertEquals(6, AcademicDayOfWeek.SATURDAY.dayIndex)

        val sunday = LocalDate.of(2026, 8, 23) // Sunday
        assertEquals(AcademicDayOfWeek.SUNDAY, AcademicDayOfWeek.fromJavaDayOfWeek(sunday.dayOfWeek))

        val wednesday = LocalDate.of(2026, 8, 26) // Wednesday
        assertEquals(AcademicDayOfWeek.WEDNESDAY, AcademicDayOfWeek.fromJavaDayOfWeek(wednesday.dayOfWeek))
    }

    @Test
    fun testEventSorting_chronological() {
        val targetDate = LocalDate.of(2026, 8, 27)

        val class1 = AcademicCalendarEvent.ClassEvent(
            classEntity = ClassEntity(id = 1, subject = "Data Structures", instructor = "Dr. Rahman", dayOfWeek = AcademicDayOfWeek.WEDNESDAY, startTimeMinutes = 10 * 60 + 30, endTimeMinutes = 12 * 60 + 30),
            date = targetDate
        )

        val exam1 = AcademicCalendarEvent.ExamEvent(
            examEntity = ExamEntity(id = 1, title = "Database Systems Midterm", dateEpochDay = targetDate.toEpochDay(), startTimeMinutes = 14 * 60, endTimeMinutes = 16 * 60),
            date = targetDate
        )

        val taskUntimed = AcademicCalendarEvent.TaskEvent(
            taskEntity = TaskEntity(id = 1, title = "HCI Report", dueDateEpochDay = targetDate.toEpochDay(), dueTimeMinutes = null, priority = Priority.HIGH),
            date = targetDate
        )

        val taskTimed = AcademicCalendarEvent.TaskEvent(
            taskEntity = TaskEntity(id = 2, title = "Algorithm Quiz Prep", dueDateEpochDay = targetDate.toEpochDay(), dueTimeMinutes = 17 * 60, priority = Priority.MEDIUM),
            date = targetDate
        )

        val events = listOf(taskTimed, exam1, class1, taskUntimed)

        val sorted = events.sortedWith(
            compareBy<AcademicCalendarEvent> { it.isCompleted }
                .thenBy { it.startTimeMinutes ?: Int.MIN_VALUE }
                .thenBy { it.type.ordinal }
                .thenBy { it.title }
        )

        // Expected: Untimed task first (startTimeMinutes == null -> MIN_VALUE), then class at 10:30, then exam at 14:00, then task at 17:00
        assertEquals(taskUntimed.id, sorted[0].id)
        assertEquals(class1.id, sorted[1].id)
        assertEquals(exam1.id, sorted[2].id)
        assertEquals(taskTimed.id, sorted[3].id)
    }

    @Test
    fun testEventTypeFiltering() {
        val targetDate = LocalDate.of(2026, 8, 27)

        val class1 = AcademicCalendarEvent.ClassEvent(
            classEntity = ClassEntity(id = 1, subject = "Data Structures", instructor = "", dayOfWeek = AcademicDayOfWeek.THURSDAY, startTimeMinutes = 600, endTimeMinutes = 700),
            date = targetDate
        )

        val task1 = AcademicCalendarEvent.TaskEvent(
            taskEntity = TaskEntity(id = 1, title = "Task 1", dueDateEpochDay = targetDate.toEpochDay()),
            date = targetDate
        )

        val exam1 = AcademicCalendarEvent.ExamEvent(
            examEntity = ExamEntity(id = 1, title = "Exam 1", dateEpochDay = targetDate.toEpochDay(), startTimeMinutes = 800),
            date = targetDate
        )

        val allEvents = listOf(class1, task1, exam1)

        val classesOnly = allEvents.filter { it.type == CalendarEventType.CLASS }
        val tasksOnly = allEvents.filter { it.type == CalendarEventType.TASK }
        val examsOnly = allEvents.filter { it.type == CalendarEventType.EXAM }

        assertEquals(1, classesOnly.size)
        assertEquals("Data Structures", classesOnly.first().title)

        assertEquals(1, tasksOnly.size)
        assertEquals("Task 1", tasksOnly.first().title)

        assertEquals(1, examsOnly.size)
        assertEquals("Exam 1", examsOnly.first().title)
    }

    @Test
    fun testMonthLengthAndLeapYear() {
        val febLeap = YearMonth.of(2024, 2)
        assertEquals(29, febLeap.lengthOfMonth())

        val febNonLeap = YearMonth.of(2026, 2)
        assertEquals(28, febNonLeap.lengthOfMonth())

        val august = YearMonth.of(2026, 8)
        assertEquals(31, august.lengthOfMonth())
    }
}
