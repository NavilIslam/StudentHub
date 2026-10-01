package com.navil.studenthub.data.repository

import com.navil.studenthub.data.local.dao.ExamDao
import com.navil.studenthub.data.local.entity.ExamEntity
import com.navil.studenthub.util.DateUtils
import kotlinx.coroutines.flow.Flow

class ExamRepository(private val examDao: ExamDao) {

    fun getAllExams(): Flow<List<ExamEntity>> = examDao.getAllExams()

    fun getUpcomingExams(): Flow<List<ExamEntity>> =
        examDao.getUpcomingExams(DateUtils.getTodayEpochDay())

    fun getExamsForDate(dateEpochDay: Long): Flow<List<ExamEntity>> =
        examDao.getExamsForDate(dateEpochDay)

    fun getExamsForSemester(semesterId: Long): Flow<List<ExamEntity>> =
        examDao.getExamsForSemester(semesterId)

    fun getCompletedExams(): Flow<List<ExamEntity>> = examDao.getCompletedExams()

    fun getExamById(id: Long): Flow<ExamEntity?> = examDao.getExamById(id)

    suspend fun insertExam(
        title: String,
        courseId: Long?,
        semesterId: Long?,
        dateEpochDay: Long,
        startTimeMinutes: Int,
        endTimeMinutes: Int?,
        room: String,
        notes: String
    ): Long {
        val entity = ExamEntity(
            title = title.trim(),
            courseId = courseId,
            semesterId = semesterId,
            dateEpochDay = dateEpochDay,
            startTimeMinutes = startTimeMinutes,
            endTimeMinutes = endTimeMinutes,
            room = room.trim(),
            notes = notes.trim(),
            isCompleted = false
        )
        return examDao.insertExam(entity)
    }

    suspend fun updateExam(
        id: Long,
        title: String,
        courseId: Long?,
        semesterId: Long?,
        dateEpochDay: Long,
        startTimeMinutes: Int,
        endTimeMinutes: Int?,
        room: String,
        notes: String,
        isCompleted: Boolean
    ) {
        val entity = ExamEntity(
            id = id,
            title = title.trim(),
            courseId = courseId,
            semesterId = semesterId,
            dateEpochDay = dateEpochDay,
            startTimeMinutes = startTimeMinutes,
            endTimeMinutes = endTimeMinutes,
            room = room.trim(),
            notes = notes.trim(),
            isCompleted = isCompleted
        )
        examDao.updateExam(entity)
    }

    suspend fun deleteExam(exam: ExamEntity) = examDao.deleteExam(exam)

    suspend fun deleteExamById(id: Long) = examDao.deleteExamById(id)

    suspend fun updateExamCompletion(id: Long, isCompleted: Boolean) =
        examDao.updateExamCompletion(id, isCompleted)

    suspend fun clearAllExams() = examDao.clearAllExams()
}
