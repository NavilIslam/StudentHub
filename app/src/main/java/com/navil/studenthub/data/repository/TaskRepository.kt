package com.navil.studenthub.data.repository

import com.navil.studenthub.data.local.dao.TaskDao
import com.navil.studenthub.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()

    fun getTasksForDay(epochDay: Long): Flow<List<TaskEntity>> = taskDao.getTasksForDay(epochDay)

    fun getUpcomingTasks(limit: Int = 5): Flow<List<TaskEntity>> = taskDao.getUpcomingTasks(limit)

    suspend fun getTaskById(id: Long): TaskEntity? = taskDao.getTaskById(id)

    suspend fun insertTask(task: TaskEntity): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: TaskEntity) = taskDao.updateTask(task)

    suspend fun updateTaskCompletion(id: Long, isCompleted: Boolean) =
        taskDao.updateTaskCompletion(id, isCompleted)

    suspend fun deleteTask(task: TaskEntity) = taskDao.deleteTask(task)

    suspend fun deleteTaskById(id: Long) = taskDao.deleteTaskById(id)
}
