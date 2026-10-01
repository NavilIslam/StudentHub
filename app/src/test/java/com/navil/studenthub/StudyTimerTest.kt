package com.navil.studenthub

import com.google.common.truth.Truth.assertThat
import com.navil.studenthub.util.DateUtils
import org.junit.Test

class StudyTimerTest {

    @Test
    fun `formatDuration formats MM SS correctly`() {
        assertThat(DateUtils.formatDuration(25 * 60)).isEqualTo("25:00")
        assertThat(DateUtils.formatDuration(5 * 60)).isEqualTo("05:00")
        assertThat(DateUtils.formatDuration(65)).isEqualTo("01:05")
        assertThat(DateUtils.formatDuration(0)).isEqualTo("00:00")
    }

    @Test
    fun `formatMinutesToHoursAndMinutes formats correctly`() {
        assertThat(DateUtils.formatMinutesToHoursAndMinutes(150)).isEqualTo("2h 30m")
        assertThat(DateUtils.formatMinutesToHoursAndMinutes(45)).isEqualTo("45m")
        assertThat(DateUtils.formatMinutesToHoursAndMinutes(240)).isEqualTo("4h 0m")
    }

    @Test
    fun `formatTimeMinutes formats 12 hour times correctly`() {
        assertThat(DateUtils.formatTimeMinutes(9 * 60)).isEqualTo("9:00 AM")
        assertThat(DateUtils.formatTimeMinutes(13 * 60 + 30)).isEqualTo("1:30 PM")
        assertThat(DateUtils.formatTimeMinutes(0)).isEqualTo("12:00 AM")
    }

    @Test
    fun `parseTimeToMinutes parses string correctly`() {
        assertThat(DateUtils.parseTimeToMinutes("9:00 AM")).isEqualTo(540)
        assertThat(DateUtils.parseTimeToMinutes("1:30 PM")).isEqualTo(810)
        assertThat(DateUtils.parseTimeToMinutes("14:45")).isEqualTo(885)
    }
}
