package com.navil.studenthub.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.navil.studenthub.data.local.dao.ClassDao
import com.navil.studenthub.data.local.dao.ExamDao
import com.navil.studenthub.data.local.dao.GpaCourseDao
import com.navil.studenthub.data.local.dao.SemesterDao
import com.navil.studenthub.data.local.dao.StudySessionDao
import com.navil.studenthub.data.local.dao.TaskDao
import com.navil.studenthub.data.local.entity.ClassEntity
import com.navil.studenthub.data.local.entity.ExamEntity
import com.navil.studenthub.data.local.entity.GpaCourseEntity
import com.navil.studenthub.data.local.entity.SemesterEntity
import com.navil.studenthub.data.local.entity.StudySessionEntity
import com.navil.studenthub.data.local.entity.TaskEntity

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `semesters` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `academicYear` TEXT NOT NULL,
                `semesterNumber` INTEGER NOT NULL,
                `startDate` TEXT NOT NULL,
                `endDate` TEXT NOT NULL,
                `isCurrent` INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            INSERT INTO `semesters` (`name`, `academicYear`, `semesterNumber`, `startDate`, `endDate`, `isCurrent`)
            VALUES ('Semester 1', '2026', 1, '', '', 1)
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `gpa_courses_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `semesterId` INTEGER NOT NULL,
                `courseName` TEXT NOT NULL,
                `creditHours` REAL NOT NULL,
                `grade` TEXT NOT NULL,
                FOREIGN KEY(`semesterId`) REFERENCES `semesters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            INSERT INTO `gpa_courses_new` (`id`, `semesterId`, `courseName`, `creditHours`, `grade`)
            SELECT `id`, 1, `courseName`, `creditHours`, `grade` FROM `gpa_courses`
            """.trimIndent()
        )

        db.execSQL("DROP TABLE `gpa_courses`")
        db.execSQL("ALTER TABLE `gpa_courses_new` RENAME TO `gpa_courses`")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_gpa_courses_semesterId` ON `gpa_courses` (`semesterId`)")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `exams` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `title` TEXT NOT NULL,
                `courseId` INTEGER,
                `semesterId` INTEGER,
                `dateEpochDay` INTEGER NOT NULL,
                `startTimeMinutes` INTEGER NOT NULL,
                `endTimeMinutes` INTEGER,
                `room` TEXT NOT NULL,
                `notes` TEXT NOT NULL,
                `isCompleted` INTEGER NOT NULL,
                `createdAtEpochMs` INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exams_courseId` ON `exams` (`courseId`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exams_semesterId` ON `exams` (`semesterId`)")
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exams_dateEpochDay` ON `exams` (`dateEpochDay`)")
    }
}

@Database(
    entities = [
        ClassEntity::class,
        TaskEntity::class,
        StudySessionEntity::class,
        GpaCourseEntity::class,
        SemesterEntity::class,
        ExamEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun classDao(): ClassDao
    abstract fun taskDao(): TaskDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun gpaCourseDao(): GpaCourseDao
    abstract fun semesterDao(): SemesterDao
    abstract fun examDao(): ExamDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_hub_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
