package com.navil.studenthub.data.repository

import com.navil.studenthub.data.local.dao.GpaCourseDao
import com.navil.studenthub.data.local.dao.SemesterDao
import com.navil.studenthub.data.local.entity.GpaCourseEntity
import com.navil.studenthub.model.GpaGrade
import com.navil.studenthub.util.GpaCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GpaRepository(
    private val gpaCourseDao: GpaCourseDao,
    private val semesterDao: SemesterDao
) {

    fun getCoursesForSemester(semesterId: Long): Flow<List<GpaCourseEntity>> =
        gpaCourseDao.getCoursesForSemester(semesterId)

    fun getAllCourses(): Flow<List<GpaCourseEntity>> = gpaCourseDao.getAllCourses()

    fun getSemesterGpaSummary(semesterId: Long): Flow<GpaCalculator.GpaResult> {
        return gpaCourseDao.getCoursesForSemester(semesterId).map { list ->
            val courseInputs = list.map {
                GpaCalculator.CourseInput(
                    courseName = it.courseName,
                    creditHours = it.creditHours,
                    grade = it.grade
                )
            }
            GpaCalculator.calculateGpa(courseInputs)
        }
    }

    fun getCgpaSummary(): Flow<GpaCalculator.GpaResult> {
        return gpaCourseDao.getAllCourses().map { list ->
            val courseInputs = list.map {
                GpaCalculator.CourseInput(
                    courseName = it.courseName,
                    creditHours = it.creditHours,
                    grade = it.grade
                )
            }
            GpaCalculator.calculateCgpa(courseInputs)
        }
    }

    fun getSemesterPerformanceHistory(): Flow<List<GpaCalculator.SemesterPerformance>> {
        return combine(
            semesterDao.getAllSemesters(),
            gpaCourseDao.getAllCourses()
        ) { semesters, courses ->
            semesters.map { sem ->
                val semCourses = courses.filter { it.semesterId == sem.id }
                val courseInputs = semCourses.map {
                    GpaCalculator.CourseInput(
                        courseName = it.courseName,
                        creditHours = it.creditHours,
                        grade = it.grade
                    )
                }
                val gpaResult = GpaCalculator.calculateGpa(courseInputs)
                GpaCalculator.SemesterPerformance(
                    semesterId = sem.id,
                    semesterName = sem.name,
                    academicYear = sem.academicYear,
                    semesterNumber = sem.semesterNumber,
                    gpa = gpaResult.gpa,
                    totalCredits = gpaResult.totalCredits,
                    totalCourses = semCourses.size,
                    isCurrent = sem.isCurrent
                )
            }.sortedWith(
                compareByDescending<GpaCalculator.SemesterPerformance> { it.isCurrent }
                    .thenByDescending { it.semesterNumber }
                    .thenByDescending { it.semesterId }
            )
        }
    }

    suspend fun insertCourse(semesterId: Long, courseName: String, creditHours: Double, grade: GpaGrade): Long {
        val entity = GpaCourseEntity(
            semesterId = semesterId,
            courseName = courseName.trim(),
            creditHours = creditHours,
            grade = grade
        )
        return gpaCourseDao.insertCourse(entity)
    }

    suspend fun updateCourse(id: Long, semesterId: Long, courseName: String, creditHours: Double, grade: GpaGrade) {
        val entity = GpaCourseEntity(
            id = id,
            semesterId = semesterId,
            courseName = courseName.trim(),
            creditHours = creditHours,
            grade = grade
        )
        gpaCourseDao.updateCourse(entity)
    }

    suspend fun deleteCourse(course: GpaCourseEntity) = gpaCourseDao.deleteCourse(course)

    suspend fun deleteCourseById(id: Long) = gpaCourseDao.deleteCourseById(id)

    suspend fun deleteCoursesBySemesterId(semesterId: Long) = gpaCourseDao.deleteCoursesBySemesterId(semesterId)

    suspend fun clearAllCourses() = gpaCourseDao.clearAllCourses()
}
