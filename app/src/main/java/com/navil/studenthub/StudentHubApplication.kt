package com.navil.studenthub

import android.app.Application
import com.navil.studenthub.data.local.database.AppDatabase
import com.navil.studenthub.data.repository.ClassRepository
import com.navil.studenthub.data.repository.ExamRepository
import com.navil.studenthub.data.repository.GpaRepository
import com.navil.studenthub.data.repository.SemesterRepository
import com.navil.studenthub.data.repository.StudyRepository
import com.navil.studenthub.data.repository.TaskRepository
import com.navil.studenthub.data.repository.UserPreferencesRepository

class StudentHubApplication : Application() {

    lateinit var classRepository: ClassRepository
        private set

    lateinit var taskRepository: TaskRepository
        private set

    lateinit var studyRepository: StudyRepository
        private set

    lateinit var gpaRepository: GpaRepository
        private set

    lateinit var semesterRepository: SemesterRepository
        private set

    lateinit var examRepository: ExamRepository
        private set

    lateinit var userPreferencesRepository: UserPreferencesRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val database = AppDatabase.getDatabase(this)
        classRepository = ClassRepository(database.classDao())
        taskRepository = TaskRepository(database.taskDao())
        studyRepository = StudyRepository(database.studySessionDao())
        semesterRepository = SemesterRepository(database.semesterDao())
        gpaRepository = GpaRepository(database.gpaCourseDao(), database.semesterDao())
        examRepository = ExamRepository(database.examDao())
        userPreferencesRepository = UserPreferencesRepository(this)
    }
}
