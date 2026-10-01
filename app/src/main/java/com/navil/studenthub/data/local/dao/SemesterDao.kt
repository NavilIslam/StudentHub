package com.navil.studenthub.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.navil.studenthub.data.local.entity.SemesterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SemesterDao {

    @Query("SELECT * FROM semesters ORDER BY isCurrent DESC, semesterNumber ASC, id ASC")
    fun getAllSemesters(): Flow<List<SemesterEntity>>

    @Query("SELECT * FROM semesters WHERE id = :id LIMIT 1")
    fun getSemesterById(id: Long): Flow<SemesterEntity?>

    @Query("SELECT * FROM semesters WHERE isCurrent = 1 LIMIT 1")
    fun getCurrentSemester(): Flow<SemesterEntity?>

    @Query("SELECT * FROM semesters WHERE isCurrent = 1 LIMIT 1")
    suspend fun getCurrentSemesterSync(): SemesterEntity?

    @Query("SELECT * FROM semesters ORDER BY id ASC LIMIT 1")
    suspend fun getFirstSemesterSync(): SemesterEntity?

    @Query("SELECT COUNT(*) FROM semesters")
    suspend fun getSemesterCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSemester(semester: SemesterEntity): Long

    @Update
    suspend fun updateSemester(semester: SemesterEntity)

    @Delete
    suspend fun deleteSemester(semester: SemesterEntity)

    @Query("DELETE FROM semesters WHERE id = :id")
    suspend fun deleteSemesterById(id: Long)

    @Query("UPDATE semesters SET isCurrent = 0")
    suspend fun clearAllCurrentFlags()

    @Query("UPDATE semesters SET isCurrent = 1 WHERE id = :semesterId")
    suspend fun markSemesterAsCurrent(semesterId: Long)

    @Transaction
    suspend fun setCurrentSemester(semesterId: Long) {
        clearAllCurrentFlags()
        markSemesterAsCurrent(semesterId)
    }

    @Query("DELETE FROM semesters")
    suspend fun clearAllSemesters()
}
