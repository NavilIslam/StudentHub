package com.navil.studenthub.data.local.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.navil.studenthub.data.local.dao.ClassDao;
import com.navil.studenthub.data.local.dao.ClassDao_Impl;
import com.navil.studenthub.data.local.dao.ExamDao;
import com.navil.studenthub.data.local.dao.ExamDao_Impl;
import com.navil.studenthub.data.local.dao.GpaCourseDao;
import com.navil.studenthub.data.local.dao.GpaCourseDao_Impl;
import com.navil.studenthub.data.local.dao.SemesterDao;
import com.navil.studenthub.data.local.dao.SemesterDao_Impl;
import com.navil.studenthub.data.local.dao.StudySessionDao;
import com.navil.studenthub.data.local.dao.StudySessionDao_Impl;
import com.navil.studenthub.data.local.dao.TaskDao;
import com.navil.studenthub.data.local.dao.TaskDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ClassDao _classDao;

  private volatile TaskDao _taskDao;

  private volatile StudySessionDao _studySessionDao;

  private volatile GpaCourseDao _gpaCourseDao;

  private volatile SemesterDao _semesterDao;

  private volatile ExamDao _examDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `classes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `subject` TEXT NOT NULL, `instructor` TEXT NOT NULL, `dayOfWeek` TEXT NOT NULL, `startTimeMinutes` INTEGER NOT NULL, `endTimeMinutes` INTEGER NOT NULL, `room` TEXT NOT NULL, `notes` TEXT NOT NULL, `colorHex` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `course` TEXT NOT NULL, `description` TEXT NOT NULL, `dueDateEpochDay` INTEGER NOT NULL, `dueTimeMinutes` INTEGER, `priority` TEXT NOT NULL, `hasReminder` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `createdAtEpochMs` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `study_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestampEpochMs` INTEGER NOT NULL, `durationMinutes` INTEGER NOT NULL, `sessionType` TEXT NOT NULL, `subject` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `gpa_courses` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `semesterId` INTEGER NOT NULL, `courseName` TEXT NOT NULL, `creditHours` REAL NOT NULL, `grade` TEXT NOT NULL, FOREIGN KEY(`semesterId`) REFERENCES `semesters`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_gpa_courses_semesterId` ON `gpa_courses` (`semesterId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `semesters` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `academicYear` TEXT NOT NULL, `semesterNumber` INTEGER NOT NULL, `startDate` TEXT NOT NULL, `endDate` TEXT NOT NULL, `isCurrent` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exams` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `courseId` INTEGER, `semesterId` INTEGER, `dateEpochDay` INTEGER NOT NULL, `startTimeMinutes` INTEGER NOT NULL, `endTimeMinutes` INTEGER, `room` TEXT NOT NULL, `notes` TEXT NOT NULL, `isCompleted` INTEGER NOT NULL, `createdAtEpochMs` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exams_courseId` ON `exams` (`courseId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exams_semesterId` ON `exams` (`semesterId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exams_dateEpochDay` ON `exams` (`dateEpochDay`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'bff6e717b9853500f34e85323d17099d')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `classes`");
        db.execSQL("DROP TABLE IF EXISTS `tasks`");
        db.execSQL("DROP TABLE IF EXISTS `study_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `gpa_courses`");
        db.execSQL("DROP TABLE IF EXISTS `semesters`");
        db.execSQL("DROP TABLE IF EXISTS `exams`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsClasses = new HashMap<String, TableInfo.Column>(9);
        _columnsClasses.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClasses.put("subject", new TableInfo.Column("subject", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClasses.put("instructor", new TableInfo.Column("instructor", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClasses.put("dayOfWeek", new TableInfo.Column("dayOfWeek", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClasses.put("startTimeMinutes", new TableInfo.Column("startTimeMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClasses.put("endTimeMinutes", new TableInfo.Column("endTimeMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClasses.put("room", new TableInfo.Column("room", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClasses.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClasses.put("colorHex", new TableInfo.Column("colorHex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysClasses = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesClasses = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoClasses = new TableInfo("classes", _columnsClasses, _foreignKeysClasses, _indicesClasses);
        final TableInfo _existingClasses = TableInfo.read(db, "classes");
        if (!_infoClasses.equals(_existingClasses)) {
          return new RoomOpenHelper.ValidationResult(false, "classes(com.navil.studenthub.data.local.entity.ClassEntity).\n"
                  + " Expected:\n" + _infoClasses + "\n"
                  + " Found:\n" + _existingClasses);
        }
        final HashMap<String, TableInfo.Column> _columnsTasks = new HashMap<String, TableInfo.Column>(10);
        _columnsTasks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("course", new TableInfo.Column("course", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("dueDateEpochDay", new TableInfo.Column("dueDateEpochDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("dueTimeMinutes", new TableInfo.Column("dueTimeMinutes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("priority", new TableInfo.Column("priority", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("hasReminder", new TableInfo.Column("hasReminder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("createdAtEpochMs", new TableInfo.Column("createdAtEpochMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTasks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTasks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTasks = new TableInfo("tasks", _columnsTasks, _foreignKeysTasks, _indicesTasks);
        final TableInfo _existingTasks = TableInfo.read(db, "tasks");
        if (!_infoTasks.equals(_existingTasks)) {
          return new RoomOpenHelper.ValidationResult(false, "tasks(com.navil.studenthub.data.local.entity.TaskEntity).\n"
                  + " Expected:\n" + _infoTasks + "\n"
                  + " Found:\n" + _existingTasks);
        }
        final HashMap<String, TableInfo.Column> _columnsStudySessions = new HashMap<String, TableInfo.Column>(5);
        _columnsStudySessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudySessions.put("timestampEpochMs", new TableInfo.Column("timestampEpochMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudySessions.put("durationMinutes", new TableInfo.Column("durationMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudySessions.put("sessionType", new TableInfo.Column("sessionType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudySessions.put("subject", new TableInfo.Column("subject", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStudySessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStudySessions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStudySessions = new TableInfo("study_sessions", _columnsStudySessions, _foreignKeysStudySessions, _indicesStudySessions);
        final TableInfo _existingStudySessions = TableInfo.read(db, "study_sessions");
        if (!_infoStudySessions.equals(_existingStudySessions)) {
          return new RoomOpenHelper.ValidationResult(false, "study_sessions(com.navil.studenthub.data.local.entity.StudySessionEntity).\n"
                  + " Expected:\n" + _infoStudySessions + "\n"
                  + " Found:\n" + _existingStudySessions);
        }
        final HashMap<String, TableInfo.Column> _columnsGpaCourses = new HashMap<String, TableInfo.Column>(5);
        _columnsGpaCourses.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGpaCourses.put("semesterId", new TableInfo.Column("semesterId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGpaCourses.put("courseName", new TableInfo.Column("courseName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGpaCourses.put("creditHours", new TableInfo.Column("creditHours", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGpaCourses.put("grade", new TableInfo.Column("grade", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGpaCourses = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysGpaCourses.add(new TableInfo.ForeignKey("semesters", "CASCADE", "NO ACTION", Arrays.asList("semesterId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesGpaCourses = new HashSet<TableInfo.Index>(1);
        _indicesGpaCourses.add(new TableInfo.Index("index_gpa_courses_semesterId", false, Arrays.asList("semesterId"), Arrays.asList("ASC")));
        final TableInfo _infoGpaCourses = new TableInfo("gpa_courses", _columnsGpaCourses, _foreignKeysGpaCourses, _indicesGpaCourses);
        final TableInfo _existingGpaCourses = TableInfo.read(db, "gpa_courses");
        if (!_infoGpaCourses.equals(_existingGpaCourses)) {
          return new RoomOpenHelper.ValidationResult(false, "gpa_courses(com.navil.studenthub.data.local.entity.GpaCourseEntity).\n"
                  + " Expected:\n" + _infoGpaCourses + "\n"
                  + " Found:\n" + _existingGpaCourses);
        }
        final HashMap<String, TableInfo.Column> _columnsSemesters = new HashMap<String, TableInfo.Column>(7);
        _columnsSemesters.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSemesters.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSemesters.put("academicYear", new TableInfo.Column("academicYear", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSemesters.put("semesterNumber", new TableInfo.Column("semesterNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSemesters.put("startDate", new TableInfo.Column("startDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSemesters.put("endDate", new TableInfo.Column("endDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSemesters.put("isCurrent", new TableInfo.Column("isCurrent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSemesters = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSemesters = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSemesters = new TableInfo("semesters", _columnsSemesters, _foreignKeysSemesters, _indicesSemesters);
        final TableInfo _existingSemesters = TableInfo.read(db, "semesters");
        if (!_infoSemesters.equals(_existingSemesters)) {
          return new RoomOpenHelper.ValidationResult(false, "semesters(com.navil.studenthub.data.local.entity.SemesterEntity).\n"
                  + " Expected:\n" + _infoSemesters + "\n"
                  + " Found:\n" + _existingSemesters);
        }
        final HashMap<String, TableInfo.Column> _columnsExams = new HashMap<String, TableInfo.Column>(11);
        _columnsExams.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("courseId", new TableInfo.Column("courseId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("semesterId", new TableInfo.Column("semesterId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("dateEpochDay", new TableInfo.Column("dateEpochDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("startTimeMinutes", new TableInfo.Column("startTimeMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("endTimeMinutes", new TableInfo.Column("endTimeMinutes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("room", new TableInfo.Column("room", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("createdAtEpochMs", new TableInfo.Column("createdAtEpochMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExams = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExams = new HashSet<TableInfo.Index>(3);
        _indicesExams.add(new TableInfo.Index("index_exams_courseId", false, Arrays.asList("courseId"), Arrays.asList("ASC")));
        _indicesExams.add(new TableInfo.Index("index_exams_semesterId", false, Arrays.asList("semesterId"), Arrays.asList("ASC")));
        _indicesExams.add(new TableInfo.Index("index_exams_dateEpochDay", false, Arrays.asList("dateEpochDay"), Arrays.asList("ASC")));
        final TableInfo _infoExams = new TableInfo("exams", _columnsExams, _foreignKeysExams, _indicesExams);
        final TableInfo _existingExams = TableInfo.read(db, "exams");
        if (!_infoExams.equals(_existingExams)) {
          return new RoomOpenHelper.ValidationResult(false, "exams(com.navil.studenthub.data.local.entity.ExamEntity).\n"
                  + " Expected:\n" + _infoExams + "\n"
                  + " Found:\n" + _existingExams);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "bff6e717b9853500f34e85323d17099d", "23313eb6c30ec5744e79426439c69ecb");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "classes","tasks","study_sessions","gpa_courses","semesters","exams");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `classes`");
      _db.execSQL("DELETE FROM `tasks`");
      _db.execSQL("DELETE FROM `study_sessions`");
      _db.execSQL("DELETE FROM `gpa_courses`");
      _db.execSQL("DELETE FROM `semesters`");
      _db.execSQL("DELETE FROM `exams`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ClassDao.class, ClassDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TaskDao.class, TaskDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StudySessionDao.class, StudySessionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GpaCourseDao.class, GpaCourseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SemesterDao.class, SemesterDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExamDao.class, ExamDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ClassDao classDao() {
    if (_classDao != null) {
      return _classDao;
    } else {
      synchronized(this) {
        if(_classDao == null) {
          _classDao = new ClassDao_Impl(this);
        }
        return _classDao;
      }
    }
  }

  @Override
  public TaskDao taskDao() {
    if (_taskDao != null) {
      return _taskDao;
    } else {
      synchronized(this) {
        if(_taskDao == null) {
          _taskDao = new TaskDao_Impl(this);
        }
        return _taskDao;
      }
    }
  }

  @Override
  public StudySessionDao studySessionDao() {
    if (_studySessionDao != null) {
      return _studySessionDao;
    } else {
      synchronized(this) {
        if(_studySessionDao == null) {
          _studySessionDao = new StudySessionDao_Impl(this);
        }
        return _studySessionDao;
      }
    }
  }

  @Override
  public GpaCourseDao gpaCourseDao() {
    if (_gpaCourseDao != null) {
      return _gpaCourseDao;
    } else {
      synchronized(this) {
        if(_gpaCourseDao == null) {
          _gpaCourseDao = new GpaCourseDao_Impl(this);
        }
        return _gpaCourseDao;
      }
    }
  }

  @Override
  public SemesterDao semesterDao() {
    if (_semesterDao != null) {
      return _semesterDao;
    } else {
      synchronized(this) {
        if(_semesterDao == null) {
          _semesterDao = new SemesterDao_Impl(this);
        }
        return _semesterDao;
      }
    }
  }

  @Override
  public ExamDao examDao() {
    if (_examDao != null) {
      return _examDao;
    } else {
      synchronized(this) {
        if(_examDao == null) {
          _examDao = new ExamDao_Impl(this);
        }
        return _examDao;
      }
    }
  }
}
