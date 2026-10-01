package com.navil.studenthub.util

import com.navil.studenthub.model.AcademicDayOfWeek
import com.navil.studenthub.model.ValidationResult

object Validators {

    fun validateClassInput(
        subject: String,
        dayOfWeek: AcademicDayOfWeek?,
        startTimeMinutes: Int?,
        endTimeMinutes: Int?
    ): ValidationResult {
        if (subject.trim().isEmpty()) {
            return ValidationResult.error("Subject name is required.")
        }
        if (dayOfWeek == null) {
            return ValidationResult.error("Day of week is required.")
        }
        if (startTimeMinutes == null) {
            return ValidationResult.error("Start time is required.")
        }
        if (endTimeMinutes == null) {
            return ValidationResult.error("End time is required.")
        }
        if (endTimeMinutes <= startTimeMinutes) {
            return ValidationResult.error("End time must be after start time.")
        }
        return ValidationResult.success()
    }

    fun validateTaskInput(
        title: String,
        dueDateEpochDay: Long?
    ): ValidationResult {
        if (title.trim().isEmpty()) {
            return ValidationResult.error("Task title is required.")
        }
        if (dueDateEpochDay == null || dueDateEpochDay <= 0) {
            return ValidationResult.error("Due date is required.")
        }
        return ValidationResult.success()
    }

    fun validateCourseInput(
        courseName: String,
        creditHoursText: String
    ): ValidationResult {
        if (courseName.trim().isEmpty()) {
            return ValidationResult.error("Course name is required.")
        }
        val credits = creditHoursText.toDoubleOrNull()
        if (credits == null || credits <= 0.0) {
            return ValidationResult.error("Enter a valid positive credit value (e.g. 3.0).")
        }
        if (credits > 12.0) {
            return ValidationResult.error("Credit hours cannot exceed 12 per course.")
        }
        return ValidationResult.success()
    }
}
