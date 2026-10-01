package com.navil.studenthub.data.repository

import com.navil.studenthub.data.local.dao.StudySessionDao
import com.navil.studenthub.data.local.entity.StudySessionEntity
import com.navil.studenthub.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudyRepository(private val studySessionDao: StudySessionDao) {

    fun getAllSessions(): Flow<List<StudySessionEntity>> = studySessionDao.getAllSessions()

    fun getTodaySessions(): Flow<List<StudySessionEntity>> {
        val startOfToday = DateUtils.getStartOfDayEpochMs()
        return studySessionDao.getSessionsSince(startOfToday)
    }

    fun getTodayTotalMinutes(): Flow<Long> {
        val startOfToday = DateUtils.getStartOfDayEpochMs()
        return studySessionDao.getTotalMinutesSince(startOfToday).map { it ?: 0L }
    }

    fun getThisWeekTotalMinutes(): Flow<Long> {
        val startOfWeek = DateUtils.getStartOfWeekEpochMs()
        return studySessionDao.getTotalMinutesSince(startOfWeek).map { it ?: 0L }
    }

    fun getTodaySessionCount(): Flow<Int> {
        val startOfToday = DateUtils.getStartOfDayEpochMs()
        return studySessionDao.getSessionCountSince(startOfToday)
    }

    suspend fun insertSession(durationMinutes: Long, sessionType: String = "Study", subject: String = ""): Long {
        val session = StudySessionEntity(
            timestampEpochMs = System.currentTimeMillis(),
            durationMinutes = durationMinutes,
            sessionType = sessionType,
            subject = subject
        )
        return studySessionDao.insertSession(session)
    }

    suspend fun deleteSessionById(id: Long) = studySessionDao.deleteSessionById(id)
}
