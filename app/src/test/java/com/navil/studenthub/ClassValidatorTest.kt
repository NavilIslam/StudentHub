package com.navil.studenthub

import com.google.common.truth.Truth.assertThat
import com.navil.studenthub.model.AcademicDayOfWeek
import com.navil.studenthub.util.Validators
import org.junit.Test

class ClassValidatorTest {

    @Test
    fun `empty subject fails validation`() {
        val result = Validators.validateClassInput(
            subject = "   ",
            dayOfWeek = AcademicDayOfWeek.SUNDAY,
            startTimeMinutes = 540,
            endTimeMinutes = 630
        )
        assertThat(result.isValid).isFalse()
        assertThat(result.errorMessage).contains("Subject")
    }

    @Test
    fun `null day fails validation`() {
        val result = Validators.validateClassInput(
            subject = "Data Structures",
            dayOfWeek = null,
            startTimeMinutes = 540,
            endTimeMinutes = 630
        )
        assertThat(result.isValid).isFalse()
        assertThat(result.errorMessage).contains("Day")
    }

    @Test
    fun `end time earlier than start time fails validation`() {
        val result = Validators.validateClassInput(
            subject = "Algorithms",
            dayOfWeek = AcademicDayOfWeek.MONDAY,
            startTimeMinutes = 600, // 10:00 AM
            endTimeMinutes = 540    // 09:00 AM
        )
        assertThat(result.isValid).isFalse()
        assertThat(result.errorMessage).contains("after")
    }

    @Test
    fun `valid class passes validation`() {
        val result = Validators.validateClassInput(
            subject = "Operating Systems",
            dayOfWeek = AcademicDayOfWeek.SUNDAY,
            startTimeMinutes = 540, // 09:00 AM
            endTimeMinutes = 630    // 10:30 AM
        )
        assertThat(result.isValid).isTrue()
        assertThat(result.errorMessage).isNull()
    }
}
