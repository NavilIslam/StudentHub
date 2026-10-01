package com.navil.studenthub.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.navil.studenthub.data.local.entity.ExamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {

    @Query("SELECT * FROM exams ORDER BY dateEpochDay ASC, startTimeMinutes ASC")
    fun getAllExams(): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE isCompleted = 0 AND dateEpochDay >= :todayEpochDay ORDER BY dateEpochDay ASC, startTimeMinutes ASC")
    fun getUpcomingExams(todayEpochDay: Long): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE dateEpochDay = :dateEpochDay ORDER BY startTimeMinutes ASC")
    fun getExamsForDate(dateEpochDay: Long): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE semesterId = :semesterId ORDER BY dateEpochDay ASC, startTimeMinutes ASC")
    fun getExamsForSemester(semesterId: Long): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE isCompleted = 1 ORDER BY dateEpochDay DESC, startTimeMinutes DESC")
    fun getCompletedExams(): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE id = :id LIMIT 1")
    fun getExamById(id: Long): Flow<ExamEntity?>

    @Query("SELECT * FROM exams WHERE id = :id LIMIT 1")
    suspend fun getExamByIdSync(id: Long): ExamEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: ExamEntity): Long

    @Update
    suspend fun updateExam(exam: ExamEntity)

    @Delete
    suspend fun deleteExam(exam: ExamEntity)

    @Query("DELETE FROM exams WHERE id = :id")
    suspend fun deleteExamById(id: Long)

    @Query("UPDATE exams SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateExamCompletion(id: Long, isCompleted: Boolean)

    @Query("DELETE FROM exams")
    suspend fun clearAllExams()
}
