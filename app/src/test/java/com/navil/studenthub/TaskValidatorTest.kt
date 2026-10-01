package com.navil.studenthub

import com.google.common.truth.Truth.assertThat
import com.navil.studenthub.util.Validators
import org.junit.Test

class TaskValidatorTest {

    @Test
    fun `empty task title fails validation`() {
        val result = Validators.validateTaskInput(
            title = "   ",
            dueDateEpochDay = 20000L
        )
        assertThat(result.isValid).isFalse()
        assertThat(result.errorMessage).contains("title")
    }

    @Test
    fun `missing due date fails validation`() {
        val result = Validators.validateTaskInput(
            title = "Assignment 1",
            dueDateEpochDay = 0L
        )
        assertThat(result.isValid).isFalse()
        assertThat(result.errorMessage).contains("Due date")
    }

    @Test
    fun `valid task passes validation`() {
        val result = Validators.validateTaskInput(
            title = "Final Project Submission",
            dueDateEpochDay = 20000L
        )
        assertThat(result.isValid).isTrue()
        assertThat(result.errorMessage).isNull()
    }
}
