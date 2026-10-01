package com.navil.studenthub.data.local.database

import androidx.room.TypeConverter
import com.navil.studenthub.model.AcademicDayOfWeek
import com.navil.studenthub.model.GpaGrade
import com.navil.studenthub.model.Priority

class Converters {

    @TypeConverter
    fun fromAcademicDayOfWeek(day: AcademicDayOfWeek): String = day.name

    @TypeConverter
    fun toAcademicDayOfWeek(value: String): AcademicDayOfWeek = try {
        AcademicDayOfWeek.valueOf(value)
    } catch (e: Exception) {
        AcademicDayOfWeek.SUNDAY
    }

    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name

    @TypeConverter
    fun toPriority(value: String): Priority = try {
        Priority.valueOf(value)
    } catch (e: Exception) {
        Priority.MEDIUM
    }

    @TypeConverter
    fun fromGpaGrade(grade: GpaGrade): String = grade.name

    @TypeConverter
    fun toGpaGrade(value: String): GpaGrade = try {
        GpaGrade.valueOf(value)
    } catch (e: Exception) {
        GpaGrade.fromLetter(value)
    }
}
