package com.navil.studenthub.data.repository

import com.navil.studenthub.data.local.dao.SemesterDao
import com.navil.studenthub.data.local.entity.SemesterEntity
import kotlinx.coroutines.flow.Flow

class SemesterRepository(private val semesterDao: SemesterDao) {

    fun getAllSemesters(): Flow<List<SemesterEntity>> = semesterDao.getAllSemesters()

    fun getSemesterById(id: Long): Flow<SemesterEntity?> = semesterDao.getSemesterById(id)

    fun getCurrentSemester(): Flow<SemesterEntity?> = semesterDao.getCurrentSemester()

    suspend fun getCurrentSemesterSync(): SemesterEntity? = semesterDao.getCurrentSemesterSync()

    suspend fun getFirstSemesterSync(): SemesterEntity? = semesterDao.getFirstSemesterSync()

    suspend fun insertSemester(
        name: String,
        academicYear: String,
        semesterNumber: Int,
        startDate: String = "",
        endDate: String = "",
        isCurrent: Boolean = false
    ): Long {
        if (isCurrent) {
            semesterDao.clearAllCurrentFlags()
        }
        val entity = SemesterEntity(
            name = name.trim(),
            academicYear = academicYear.trim(),
            semesterNumber = semesterNumber,
            startDate = startDate.trim(),
            endDate = endDate.trim(),
            isCurrent = isCurrent
        )
        val id = semesterDao.insertSemester(entity)
        // If this is the only semester, make it current
        if (!isCurrent && semesterDao.getSemesterCount() == 1) {
            semesterDao.setCurrentSemester(id)
        }
        return id
    }

    suspend fun updateSemester(
        id: Long,
        name: String,
        academicYear: String,
        semesterNumber: Int,
        startDate: String = "",
        endDate: String = "",
        isCurrent: Boolean = false
    ) {
        if (isCurrent) {
            semesterDao.clearAllCurrentFlags()
        }
        val entity = SemesterEntity(
            id = id,
            name = name.trim(),
            academicYear = academicYear.trim(),
            semesterNumber = semesterNumber,
            startDate = startDate.trim(),
            endDate = endDate.trim(),
            isCurrent = isCurrent
        )
        semesterDao.updateSemester(entity)
    }

    suspend fun setCurrentSemester(semesterId: Long) {
        semesterDao.setCurrentSemester(semesterId)
    }

    suspend fun deleteSemester(semester: SemesterEntity) {
        semesterDao.deleteSemester(semester)
        // If deleted semester was current, set first available semester as current
        if (semester.isCurrent) {
            val first = semesterDao.getFirstSemesterSync()
            if (first != null) {
                semesterDao.setCurrentSemester(first.id)
            }
        }
    }

    suspend fun deleteSemesterById(id: Long) {
        semesterDao.deleteSemesterById(id)
        val current = semesterDao.getCurrentSemesterSync()
        if (current == null) {
            val first = semesterDao.getFirstSemesterSync()
            if (first != null) {
                semesterDao.setCurrentSemester(first.id)
            }
        }
    }

    suspend fun ensureDefaultSemesterExists(): Long {
        val count = semesterDao.getSemesterCount()
        if (count == 0) {
            return insertSemester(
                name = "Semester 1",
                academicYear = "2026",
                semesterNumber = 1,
                isCurrent = true
            )
        }
        val current = semesterDao.getCurrentSemesterSync()
        return current?.id ?: (semesterDao.getFirstSemesterSync()?.id ?: 0L)
    }
}
