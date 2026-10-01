package com.navil.studenthub.data.repository

import com.navil.studenthub.data.local.dao.ClassDao
import com.navil.studenthub.data.local.entity.ClassEntity
import com.navil.studenthub.model.AcademicDayOfWeek
import kotlinx.coroutines.flow.Flow

class ClassRepository(private val classDao: ClassDao) {

    fun getAllClasses(): Flow<List<ClassEntity>> = classDao.getAllClasses()

    fun getClassesForDay(dayOfWeek: AcademicDayOfWeek): Flow<List<ClassEntity>> =
        classDao.getClassesForDay(dayOfWeek)

    suspend fun getClassById(id: Long): ClassEntity? = classDao.getClassById(id)

    suspend fun insertClass(classEntity: ClassEntity): Long = classDao.insertClass(classEntity)

    suspend fun updateClass(classEntity: ClassEntity) = classDao.updateClass(classEntity)

    suspend fun deleteClass(classEntity: ClassEntity) = classDao.deleteClass(classEntity)

    suspend fun deleteClassById(id: Long) = classDao.deleteClassById(id)
}
