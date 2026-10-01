package com.navil.studenthub

import com.navil.studenthub.util.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ExamUtilsTest {

    @Test
    fun testExamStatus_completed() {
        val today = LocalDate.now().toEpochDay()
        val status = DateUtils.getExamStatus(
            dateEpochDay = today + 5,
            startTimeMinutes = 10 * 60,
            endTimeMinutes = 12 * 60,
            isCompleted = true
        )
        assertEquals(DateUtils.ExamStatus.COMPLETED, status)

        val countdown = DateUtils.getExamCountdownText(
            dateEpochDay = today + 5,
            startTimeMinutes = 10 * 60,
            endTimeMinutes = 12 * 60,
            isCompleted = true
        )
        assertEquals("COMPLETED", countdown)
    }

    @Test
    fun testExamStatus_futureDates() {
        val today = LocalDate.now().toEpochDay()

        // 5 days left
        val status5 = DateUtils.getExamStatus(
            dateEpochDay = today + 5,
            startTimeMinutes = 10 * 60,
            endTimeMinutes = 12 * 60,
            isCompleted = false
        )
        assertEquals(DateUtils.ExamStatus.UPCOMING, status5)

        val countdown5 = DateUtils.getExamCountdownText(
            dateEpochDay = today + 5,
            startTimeMinutes = 10 * 60,
            endTimeMinutes = 12 * 60,
            isCompleted = false
        )
        assertEquals("5 DAYS LEFT", countdown5)

        // 1 day left (Tomorrow)
        val status1 = DateUtils.getExamStatus(
            dateEpochDay = today + 1,
            startTimeMinutes = 10 * 60,
            endTimeMinutes = 12 * 60,
            isCompleted = false
        )
        assertEquals(DateUtils.ExamStatus.UPCOMING, status1)

        val countdown1 = DateUtils.getExamCountdownText(
            dateEpochDay = today + 1,
            startTimeMinutes = 10 * 60,
            endTimeMinutes = 12 * 60,
            isCompleted = false
        )
        assertEquals("1 DAY LEFT", countdown1)
    }

    @Test
    fun testExamStatus_pastDate() {
        val today = LocalDate.now().toEpochDay()
        val statusPast = DateUtils.getExamStatus(
            dateEpochDay = today - 3,
            startTimeMinutes = 10 * 60,
            endTimeMinutes = 12 * 60,
            isCompleted = false
        )
        assertEquals(DateUtils.ExamStatus.PAST, statusPast)

        val countdownPast = DateUtils.getExamCountdownText(
            dateEpochDay = today - 3,
            startTimeMinutes = 10 * 60,
            endTimeMinutes = 12 * 60,
            isCompleted = false
        )
        assertEquals("PAST", countdownPast)
    }

    @Test
    fun testDaysUntilExam() {
        val today = LocalDate.now().toEpochDay()
        assertEquals(5L, DateUtils.getDaysUntilExam(today + 5))
        assertEquals(1L, DateUtils.getDaysUntilExam(today + 1))
        assertEquals(0L, DateUtils.getDaysUntilExam(today))
        assertEquals(0L, DateUtils.getDaysUntilExam(today - 2))
    }

    @Test
    fun testStartEndTimes() {
        val startMinutes = 10 * 60 // 10:00 AM = 600
        val endMinutes = 12 * 60 // 12:00 PM = 720
        assertTrue(endMinutes > startMinutes)

        val formattedStart = DateUtils.formatTimeMinutes(startMinutes)
        val formattedEnd = DateUtils.formatTimeMinutes(endMinutes)
        assertEquals("10:00 AM", formattedStart)
        assertEquals("12:00 PM", formattedEnd)
    }
}
