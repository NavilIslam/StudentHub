package com.navil.studenthub.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.navil.studenthub.StudentHubApplication

object AppViewModelProvider {

    val Factory = viewModelFactory {
        initializer {
            val app = studentHubApplication()
            HomeViewModel(
                classRepository = app.classRepository,
                taskRepository = app.taskRepository,
                studyRepository = app.studyRepository,
                gpaRepository = app.gpaRepository,
                examRepository = app.examRepository,
                userPreferencesRepository = app.userPreferencesRepository
            )
        }

        initializer {
            val app = studentHubApplication()
            ScheduleViewModel(
                classRepository = app.classRepository,
                examRepository = app.examRepository
            )
        }

        initializer {
            val app = studentHubApplication()
            TasksViewModel(
                taskRepository = app.taskRepository
            )
        }

        initializer {
            val app = studentHubApplication()
            StudyViewModel(
                studyRepository = app.studyRepository,
                userPreferencesRepository = app.userPreferencesRepository
            )
        }

        initializer {
            val app = studentHubApplication()
            GpaViewModel(
                gpaRepository = app.gpaRepository,
                semesterRepository = app.semesterRepository
            )
        }

        initializer {
            val app = studentHubApplication()
            ExamViewModel(
                examRepository = app.examRepository,
                gpaRepository = app.gpaRepository,
                semesterRepository = app.semesterRepository
            )
        }

        initializer {
            val app = studentHubApplication()
            CalendarViewModel(
                classRepository = app.classRepository,
                taskRepository = app.taskRepository,
                examRepository = app.examRepository,
                gpaRepository = app.gpaRepository,
                semesterRepository = app.semesterRepository
            )
        }

        initializer {
            val app = studentHubApplication()
            SettingsViewModel(
                userPreferencesRepository = app.userPreferencesRepository
            )
        }
    }
}

fun CreationExtras.studentHubApplication(): StudentHubApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as StudentHubApplication)
