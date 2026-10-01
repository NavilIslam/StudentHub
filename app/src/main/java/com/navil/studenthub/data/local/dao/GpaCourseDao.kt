package com.navil.studenthub.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.navil.studenthub.data.local.entity.GpaCourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GpaCourseDao {

    @Query("SELECT * FROM gpa_courses WHERE semesterId = :semesterId ORDER BY id ASC")
    fun getCoursesForSemester(semesterId: Long): Flow<List<GpaCourseEntity>>

    @Query("SELECT * FROM gpa_courses ORDER BY id DESC")
    fun getAllCourses(): Flow<List<GpaCourseEntity>>

    @Query("SELECT * FROM gpa_courses WHERE semesterId = :semesterId")
    suspend fun getCoursesForSemesterSync(semesterId: Long): List<GpaCourseEntity>

    @Query("SELECT * FROM gpa_courses")
    suspend fun getAllCoursesSync(): List<GpaCourseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: GpaCourseEntity): Long

    @Update
    suspend fun updateCourse(course: GpaCourseEntity)

    @Delete
    suspend fun deleteCourse(course: GpaCourseEntity)

    @Query("DELETE FROM gpa_courses WHERE id = :id")
    suspend fun deleteCourseById(id: Long)

    @Query("DELETE FROM gpa_courses WHERE semesterId = :semesterId")
    suspend fun deleteCoursesBySemesterId(semesterId: Long)

    @Query("DELETE FROM gpa_courses")
    suspend fun clearAllCourses()
}
