package com.navil.studenthub.model

import com.navil.studenthub.data.local.entity.ClassEntity
import com.navil.studenthub.data.local.entity.ExamEntity
import com.navil.studenthub.data.local.entity.TaskEntity
import java.time.LocalDate

enum class CalendarEventType(val displayName: String) {
    CLASS("Class"),
    TASK("Task"),
    EXAM("Exam")
}

enum class CalendarEventTypeFilter(val title: String) {
    ALL("All"),
    CLASSES("Classes"),
    TASKS("Tasks"),
    EXAMS("Exams")
}

sealed class AcademicCalendarEvent {
    abstract val id: String
    abstract val date: LocalDate
    abstract val startTimeMinutes: Int?
    abstract val endTimeMinutes: Int?
    abstract val title: String
    abstract val type: CalendarEventType
    abstract val courseName: String?
    abstract val semesterId: Long?
    abstract val isCompleted: Boolean

    data class ClassEvent(
        val classEntity: ClassEntity,
        override val date: LocalDate,
        override val courseName: String? = null,
        override val semesterId: Long? = null
    ) : AcademicCalendarEvent() {
        override val id: String = "class_${classEntity.id}_${date}"
        override val startTimeMinutes: Int = classEntity.startTimeMinutes
        override val endTimeMinutes: Int = classEntity.endTimeMinutes
        override val title: String = classEntity.subject
        override val type: CalendarEventType = CalendarEventType.CLASS
        override val isCompleted: Boolean = false
        val room: String = classEntity.room
        val instructor: String = classEntity.instructor
        val colorHex: String = classEntity.colorHex
    }

    data class TaskEvent(
        val taskEntity: TaskEntity,
        override val date: LocalDate,
        override val semesterId: Long? = null
    ) : AcademicCalendarEvent() {
        override val id: String = "task_${taskEntity.id}"
        override val startTimeMinutes: Int? = taskEntity.dueTimeMinutes
        override val endTimeMinutes: Int? = null
        override val title: String = taskEntity.title
        override val type: CalendarEventType = CalendarEventType.TASK
        override val isCompleted: Boolean = taskEntity.isCompleted
        override val courseName: String? = if (taskEntity.course.isNotBlank()) taskEntity.course else null
        val priority: Priority = taskEntity.priority
        val description: String = taskEntity.description
    }

    data class ExamEvent(
        val examEntity: ExamEntity,
        override val date: LocalDate,
        override val courseName: String? = null,
        val semesterName: String? = null
    ) : AcademicCalendarEvent() {
        override val id: String = "exam_${examEntity.id}"
        override val startTimeMinutes: Int = examEntity.startTimeMinutes
        override val endTimeMinutes: Int? = examEntity.endTimeMinutes
        override val title: String = examEntity.title
        override val type: CalendarEventType = CalendarEventType.EXAM
        override val semesterId: Long? = examEntity.semesterId
        override val isCompleted: Boolean = examEntity.isCompleted
        val room: String = examEntity.room
        val notes: String = examEntity.notes
    }
}
