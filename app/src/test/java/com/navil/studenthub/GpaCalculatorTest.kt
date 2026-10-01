package com.navil.studenthub

import com.google.common.truth.Truth.assertThat
import com.navil.studenthub.model.GpaGrade
import com.navil.studenthub.util.GpaCalculator
import org.junit.Test

class GpaCalculatorTest {

    @Test
    fun `empty courses returns zero GPA and zero credits`() {
        val result = GpaCalculator.calculateGpa(emptyList())
        assertThat(result.gpa).isEqualTo(0.0)
        assertThat(result.totalCredits).isEqualTo(0.0)
        assertThat(result.totalQualityPoints).isEqualTo(0.0)
        assertThat(result.completedCredits).isEqualTo(0.0)
        assertThat(result.totalCourses).isEqualTo(0)
    }

    @Test
    fun `single course calculates accurately`() {
        val courses = listOf(
            GpaCalculator.CourseInput("CSE110", 3.0, GpaGrade.A)
        )
        val result = GpaCalculator.calculateGpa(courses)
        assertThat(result.gpa).isEqualTo(4.00)
        assertThat(result.totalCredits).isEqualTo(3.0)
        assertThat(result.totalQualityPoints).isEqualTo(12.00)
        assertThat(result.completedCredits).isEqualTo(3.0)
        assertThat(result.totalCourses).isEqualTo(1)
    }

    @Test
    fun `semester GPA with Course A 4_00 and Course B 3_00 equals 3_50`() {
        // Course A: 3 credits, 4.00
        // Course B: 3 credits, 3.00
        // Expected GPA: 3.50
        val courses = listOf(
            GpaCalculator.CourseInput("Course A", 3.0, GpaGrade.A),
            GpaCalculator.CourseInput("Course B", 3.0, GpaGrade.B)
        )
        val gpa = GpaCalculator.calculateSemesterGpa(courses)
        assertThat(gpa).isEqualTo(3.50)
    }

    @Test
    fun `multiple courses calculate weighted GPA correctly`() {
        // Course 1: 3.0 cr * 4.00 (A)  = 12.00 pts
        // Course 2: 4.0 cr * 3.30 (B+) = 13.20 pts
        // Course 3: 1.5 cr * 3.70 (A-) =  5.55 pts
        // Total cr = 8.5, Total pts = 30.75, GPA = 30.75 / 8.5 = 3.6176... -> 3.62
        val courses = listOf(
            GpaCalculator.CourseInput("CSE110", 3.0, GpaGrade.A),
            GpaCalculator.CourseInput("MAT120", 4.0, GpaGrade.B_PLUS),
            GpaCalculator.CourseInput("PHY111", 1.5, GpaGrade.A_MINUS)
        )
        val result = GpaCalculator.calculateGpa(courses)
        assertThat(result.totalCredits).isEqualTo(8.5)
        assertThat(result.totalQualityPoints).isEqualTo(30.75)
        assertThat(result.gpa).isEqualTo(3.62)
        assertThat(result.completedCredits).isEqualTo(8.5)
    }

    @Test
    fun `CGPA calculation across multiple semesters is weighted accurately`() {
        // Semester 1: 12.0 credits, 3.50 GPA = 42.0 points (e.g. 4 courses of 3cr: 2 A's (24pts), 2 B's (18pts) = 42pts / 12cr = 3.50)
        // Semester 2: 15.0 credits, 3.75 GPA = 56.25 points (e.g. 3 A's (9cr * 4 = 36), 2 B+'s (6cr * 3.3 = 19.8) -> 55.8 / 15 = 3.72)
        // Overall: sum(points) / sum(credits)
        val sem1Courses = listOf(
            GpaCalculator.CourseInput("CS101", 3.0, GpaGrade.A),      // 12.0 pts
            GpaCalculator.CourseInput("MATH101", 3.0, GpaGrade.A),    // 12.0 pts
            GpaCalculator.CourseInput("ENG101", 3.0, GpaGrade.B),     // 9.0 pts
            GpaCalculator.CourseInput("PHY101", 3.0, GpaGrade.B)      // 9.0 pts
            // 42.0 pts / 12 cr = 3.50 GPA
        )

        val sem2Courses = listOf(
            GpaCalculator.CourseInput("CS201", 3.0, GpaGrade.A),      // 12.0 pts
            GpaCalculator.CourseInput("CS202", 3.0, GpaGrade.A),      // 12.0 pts
            GpaCalculator.CourseInput("MATH201", 3.0, GpaGrade.A),    // 12.0 pts
            GpaCalculator.CourseInput("ENG201", 3.0, GpaGrade.A_MINUS),// 11.1 pts
            GpaCalculator.CourseInput("HIST101", 3.0, GpaGrade.B_PLUS) // 9.9 pts
            // 57.0 pts / 15 cr = 3.80 GPA
        )

        val sem1Gpa = GpaCalculator.calculateSemesterGpa(sem1Courses)
        assertThat(sem1Gpa).isEqualTo(3.50)

        val sem2Gpa = GpaCalculator.calculateSemesterGpa(sem2Courses)
        assertThat(sem2Gpa).isEqualTo(3.80)

        // Cumulative: (42.0 + 57.0) / (12 + 15) = 99.0 / 27.0 = 3.6666... -> 3.67
        val allCourses = sem1Courses + sem2Courses
        val cgpaResult = GpaCalculator.calculateCgpa(allCourses)
        assertThat(cgpaResult.totalCredits).isEqualTo(27.0)
        assertThat(cgpaResult.totalQualityPoints).isEqualTo(99.0)
        assertThat(cgpaResult.gpa).isEqualTo(3.67)
        assertThat(cgpaResult.totalCourses).isEqualTo(9)
    }

    @Test
    fun `failed grade F gives zero points and affects GPA but is not completed credit`() {
        val courses = listOf(
            GpaCalculator.CourseInput("CS101", 3.0, GpaGrade.A), // 12.0 pts
            GpaCalculator.CourseInput("MATH101", 3.0, GpaGrade.F) // 0.0 pts
        )
        val result = GpaCalculator.calculateGpa(courses)
        assertThat(result.totalCredits).isEqualTo(6.0)
        assertThat(result.completedCredits).isEqualTo(3.0)
        assertThat(result.totalQualityPoints).isEqualTo(12.0)
        assertThat(result.gpa).isEqualTo(2.00) // 12.0 / 6.0 = 2.00
    }

    @Test
    fun `zero credit protection returns zero GPA`() {
        val courses = listOf(
            GpaCalculator.CourseInput("ZeroCreditSeminar", 0.0, GpaGrade.A)
        )
        val result = GpaCalculator.calculateGpa(courses)
        assertThat(result.gpa).isEqualTo(0.0)
        assertThat(result.totalCredits).isEqualTo(0.0)
    }

    @Test
    fun `all grades mapping test`() {
        assertThat(GpaGrade.A_PLUS.gradePoint).isEqualTo(4.00)
        assertThat(GpaGrade.A.gradePoint).isEqualTo(4.00)
        assertThat(GpaGrade.A_MINUS.gradePoint).isEqualTo(3.70)
        assertThat(GpaGrade.B_PLUS.gradePoint).isEqualTo(3.30)
        assertThat(GpaGrade.B.gradePoint).isEqualTo(3.00)
        assertThat(GpaGrade.B_MINUS.gradePoint).isEqualTo(2.70)
        assertThat(GpaGrade.C_PLUS.gradePoint).isEqualTo(2.30)
        assertThat(GpaGrade.C.gradePoint).isEqualTo(2.00)
        assertThat(GpaGrade.C_MINUS.gradePoint).isEqualTo(1.70)
        assertThat(GpaGrade.D_PLUS.gradePoint).isEqualTo(1.30)
        assertThat(GpaGrade.D.gradePoint).isEqualTo(1.00)
        assertThat(GpaGrade.D_MINUS.gradePoint).isEqualTo(0.70)
        assertThat(GpaGrade.F.gradePoint).isEqualTo(0.00)
    }
}
