package com.navil.studenthub.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.navil.studenthub.data.local.database.Converters;
import com.navil.studenthub.data.local.entity.GpaCourseEntity;
import com.navil.studenthub.model.GpaGrade;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class GpaCourseDao_Impl implements GpaCourseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GpaCourseEntity> __insertionAdapterOfGpaCourseEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<GpaCourseEntity> __deletionAdapterOfGpaCourseEntity;

  private final EntityDeletionOrUpdateAdapter<GpaCourseEntity> __updateAdapterOfGpaCourseEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCourseById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCoursesBySemesterId;

  private final SharedSQLiteStatement __preparedStmtOfClearAllCourses;

  public GpaCourseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGpaCourseEntity = new EntityInsertionAdapter<GpaCourseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `gpa_courses` (`id`,`semesterId`,`courseName`,`creditHours`,`grade`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GpaCourseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSemesterId());
        statement.bindString(3, entity.getCourseName());
        statement.bindDouble(4, entity.getCreditHours());
        final String _tmp = __converters.fromGpaGrade(entity.getGrade());
        statement.bindString(5, _tmp);
      }
    };
    this.__deletionAdapterOfGpaCourseEntity = new EntityDeletionOrUpdateAdapter<GpaCourseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `gpa_courses` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GpaCourseEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfGpaCourseEntity = new EntityDeletionOrUpdateAdapter<GpaCourseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `gpa_courses` SET `id` = ?,`semesterId` = ?,`courseName` = ?,`creditHours` = ?,`grade` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GpaCourseEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSemesterId());
        statement.bindString(3, entity.getCourseName());
        statement.bindDouble(4, entity.getCreditHours());
        final String _tmp = __converters.fromGpaGrade(entity.getGrade());
        statement.bindString(5, _tmp);
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteCourseById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM gpa_courses WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCoursesBySemesterId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM gpa_courses WHERE semesterId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllCourses = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM gpa_courses";
        return _query;
      }
    };
  }

  @Override
  public Object insertCourse(final GpaCourseEntity course,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfGpaCourseEntity.insertAndReturnId(course);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCourse(final GpaCourseEntity course,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfGpaCourseEntity.handle(course);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCourse(final GpaCourseEntity course,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGpaCourseEntity.handle(course);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCourseById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCourseById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteCourseById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCoursesBySemesterId(final long semesterId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCoursesBySemesterId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, semesterId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteCoursesBySemesterId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllCourses(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllCourses.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAllCourses.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<GpaCourseEntity>> getCoursesForSemester(final long semesterId) {
    final String _sql = "SELECT * FROM gpa_courses WHERE semesterId = ? ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, semesterId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"gpa_courses"}, new Callable<List<GpaCourseEntity>>() {
      @Override
      @NonNull
      public List<GpaCourseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSemesterId = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfCreditHours = CursorUtil.getColumnIndexOrThrow(_cursor, "creditHours");
          final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
          final List<GpaCourseEntity> _result = new ArrayList<GpaCourseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GpaCourseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSemesterId;
            _tmpSemesterId = _cursor.getLong(_cursorIndexOfSemesterId);
            final String _tmpCourseName;
            _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            final double _tmpCreditHours;
            _tmpCreditHours = _cursor.getDouble(_cursorIndexOfCreditHours);
            final GpaGrade _tmpGrade;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfGrade);
            _tmpGrade = __converters.toGpaGrade(_tmp);
            _item = new GpaCourseEntity(_tmpId,_tmpSemesterId,_tmpCourseName,_tmpCreditHours,_tmpGrade);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<GpaCourseEntity>> getAllCourses() {
    final String _sql = "SELECT * FROM gpa_courses ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"gpa_courses"}, new Callable<List<GpaCourseEntity>>() {
      @Override
      @NonNull
      public List<GpaCourseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSemesterId = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfCreditHours = CursorUtil.getColumnIndexOrThrow(_cursor, "creditHours");
          final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
          final List<GpaCourseEntity> _result = new ArrayList<GpaCourseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GpaCourseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSemesterId;
            _tmpSemesterId = _cursor.getLong(_cursorIndexOfSemesterId);
            final String _tmpCourseName;
            _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            final double _tmpCreditHours;
            _tmpCreditHours = _cursor.getDouble(_cursorIndexOfCreditHours);
            final GpaGrade _tmpGrade;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfGrade);
            _tmpGrade = __converters.toGpaGrade(_tmp);
            _item = new GpaCourseEntity(_tmpId,_tmpSemesterId,_tmpCourseName,_tmpCreditHours,_tmpGrade);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getCoursesForSemesterSync(final long semesterId,
      final Continuation<? super List<GpaCourseEntity>> $completion) {
    final String _sql = "SELECT * FROM gpa_courses WHERE semesterId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, semesterId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<GpaCourseEntity>>() {
      @Override
      @NonNull
      public List<GpaCourseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSemesterId = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfCreditHours = CursorUtil.getColumnIndexOrThrow(_cursor, "creditHours");
          final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
          final List<GpaCourseEntity> _result = new ArrayList<GpaCourseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GpaCourseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSemesterId;
            _tmpSemesterId = _cursor.getLong(_cursorIndexOfSemesterId);
            final String _tmpCourseName;
            _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            final double _tmpCreditHours;
            _tmpCreditHours = _cursor.getDouble(_cursorIndexOfCreditHours);
            final GpaGrade _tmpGrade;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfGrade);
            _tmpGrade = __converters.toGpaGrade(_tmp);
            _item = new GpaCourseEntity(_tmpId,_tmpSemesterId,_tmpCourseName,_tmpCreditHours,_tmpGrade);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllCoursesSync(final Continuation<? super List<GpaCourseEntity>> $completion) {
    final String _sql = "SELECT * FROM gpa_courses";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<GpaCourseEntity>>() {
      @Override
      @NonNull
      public List<GpaCourseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSemesterId = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterId");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfCreditHours = CursorUtil.getColumnIndexOrThrow(_cursor, "creditHours");
          final int _cursorIndexOfGrade = CursorUtil.getColumnIndexOrThrow(_cursor, "grade");
          final List<GpaCourseEntity> _result = new ArrayList<GpaCourseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GpaCourseEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSemesterId;
            _tmpSemesterId = _cursor.getLong(_cursorIndexOfSemesterId);
            final String _tmpCourseName;
            _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            final double _tmpCreditHours;
            _tmpCreditHours = _cursor.getDouble(_cursorIndexOfCreditHours);
            final GpaGrade _tmpGrade;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfGrade);
            _tmpGrade = __converters.toGpaGrade(_tmp);
            _item = new GpaCourseEntity(_tmpId,_tmpSemesterId,_tmpCourseName,_tmpCreditHours,_tmpGrade);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
