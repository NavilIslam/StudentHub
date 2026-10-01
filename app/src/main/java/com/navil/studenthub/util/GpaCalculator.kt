package com.navil.studenthub.util

import com.navil.studenthub.model.GpaGrade
import java.math.BigDecimal
import java.math.RoundingMode

object GpaCalculator {

    data class CourseInput(
        val courseName: String,
        val creditHours: Double,
        val grade: GpaGrade
    )

    data class GpaResult(
        val totalCredits: Double,
        val totalQualityPoints: Double,
        val gpa: Double,
        val totalCourses: Int = 0,
        val completedCredits: Double = 0.0
    )

    data class SemesterPerformance(
        val semesterId: Long,
        val semesterName: String,
        val academicYear: String,
        val semesterNumber: Int,
        val gpa: Double,
        val totalCredits: Double,
        val totalCourses: Int,
        val isCurrent: Boolean
    )

    fun calculateGpa(courses: List<CourseInput>): GpaResult {
        if (courses.isEmpty()) {
            return GpaResult(
                totalCredits = 0.0,
                totalQualityPoints = 0.0,
                gpa = 0.0,
                totalCourses = 0,
                completedCredits = 0.0
            )
        }

        var totalCredits = 0.0
        var completedCredits = 0.0
        var totalQualityPoints = 0.0

        for (course in courses) {
            if (course.creditHours > 0) {
                totalCredits += course.creditHours
                if (course.grade != GpaGrade.F) {
                    completedCredits += course.creditHours
                }
                totalQualityPoints += (course.creditHours * course.grade.gradePoint)
            }
        }

        val gpa = if (totalCredits > 0) {
            BigDecimal(totalQualityPoints / totalCredits)
                .setScale(2, RoundingMode.HALF_UP)
                .toDouble()
        } else {
            0.0
        }

        val roundedCredits = BigDecimal(totalCredits).setScale(1, RoundingMode.HALF_UP).toDouble()
        val roundedCompletedCredits = BigDecimal(completedCredits).setScale(1, RoundingMode.HALF_UP).toDouble()
        val roundedPoints = BigDecimal(totalQualityPoints).setScale(2, RoundingMode.HALF_UP).toDouble()

        return GpaResult(
            totalCredits = roundedCredits,
            totalQualityPoints = roundedPoints,
            gpa = gpa,
            totalCourses = courses.size,
            completedCredits = roundedCompletedCredits
        )
    }

    fun calculateSemesterGpa(courses: List<CourseInput>): Double {
        return calculateGpa(courses).gpa
    }

    fun calculateCgpa(allCourses: List<CourseInput>): GpaResult {
        return calculateGpa(allCourses)
    }

    fun calculateTotalCredits(courses: List<CourseInput>): Double {
        return calculateGpa(courses).totalCredits
    }
}
