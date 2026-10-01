package com.navil.studenthub.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.navil.studenthub.data.local.entity.ExamEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class ExamDao_Impl implements ExamDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExamEntity> __insertionAdapterOfExamEntity;

  private final EntityDeletionOrUpdateAdapter<ExamEntity> __deletionAdapterOfExamEntity;

  private final EntityDeletionOrUpdateAdapter<ExamEntity> __updateAdapterOfExamEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteExamById;

  private final SharedSQLiteStatement __preparedStmtOfUpdateExamCompletion;

  private final SharedSQLiteStatement __preparedStmtOfClearAllExams;

  public ExamDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExamEntity = new EntityInsertionAdapter<ExamEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exams` (`id`,`title`,`courseId`,`semesterId`,`dateEpochDay`,`startTimeMinutes`,`endTimeMinutes`,`room`,`notes`,`isCompleted`,`createdAtEpochMs`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExamEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getCourseId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getCourseId());
        }
        if (entity.getSemesterId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getSemesterId());
        }
        statement.bindLong(5, entity.getDateEpochDay());
        statement.bindLong(6, entity.getStartTimeMinutes());
        if (entity.getEndTimeMinutes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getEndTimeMinutes());
        }
        statement.bindString(8, entity.getRoom());
        statement.bindString(9, entity.getNotes());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(10, _tmp);
        statement.bindLong(11, entity.getCreatedAtEpochMs());
      }
    };
    this.__deletionAdapterOfExamEntity = new EntityDeletionOrUpdateAdapter<ExamEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `exams` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExamEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfExamEntity = new EntityDeletionOrUpdateAdapter<ExamEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `exams` SET `id` = ?,`title` = ?,`courseId` = ?,`semesterId` = ?,`dateEpochDay` = ?,`startTimeMinutes` = ?,`endTimeMinutes` = ?,`room` = ?,`notes` = ?,`isCompleted` = ?,`createdAtEpochMs` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExamEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getCourseId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getCourseId());
        }
        if (entity.getSemesterId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getSemesterId());
        }
        statement.bindLong(5, entity.getDateEpochDay());
        statement.bindLong(6, entity.getStartTimeMinutes());
        if (entity.getEndTimeMinutes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getEndTimeMinutes());
        }
        statement.bindString(8, entity.getRoom());
        statement.bindString(9, entity.getNotes());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(10, _tmp);
        statement.bindLong(11, entity.getCreatedAtEpochMs());
        statement.bindLong(12, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteExamById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exams WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateExamCompletion = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE exams SET isCompleted = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllExams = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exams";
        return _query;
      }
    };
  }

  @Override
  public Object insertExam(final ExamEntity exam, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExamEntity.insertAndReturnId(exam);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteExam(final ExamEntity exam, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfExamEntity.handle(exam);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateExam(final ExamEntity exam, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfExamEntity.handle(exam);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteExamById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteExamById.acquire();
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
          __preparedStmtOfDeleteExamById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateExamCompletion(final long id, final boolean isCompleted,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateExamCompletion.acquire();
        int _argIndex = 1;
        final int _tmp = isCompleted ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
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
          __preparedStmtOfUpdateExamCompletion.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllExams(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllExams.acquire();
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
          __preparedStmtOfClearAllExams.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ExamEntity>> getAllExams() {
    final String _sql = "SELECT * FROM exams ORDER BY dateEpochDay ASC, startTimeMinutes ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exams"}, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
          final int _cursorIndexOfSemesterId = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterId");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAtEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochMs");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCourseId;
            if (_cursor.isNull(_cursorIndexOfCourseId)) {
              _tmpCourseId = null;
            } else {
              _tmpCourseId = _cursor.getLong(_cursorIndexOfCourseId);
            }
            final Long _tmpSemesterId;
            if (_cursor.isNull(_cursorIndexOfSemesterId)) {
              _tmpSemesterId = null;
            } else {
              _tmpSemesterId = _cursor.getLong(_cursorIndexOfSemesterId);
            }
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final Integer _tmpEndTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfEndTimeMinutes)) {
              _tmpEndTimeMinutes = null;
            } else {
              _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            }
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final long _tmpCreatedAtEpochMs;
            _tmpCreatedAtEpochMs = _cursor.getLong(_cursorIndexOfCreatedAtEpochMs);
            _item = new ExamEntity(_tmpId,_tmpTitle,_tmpCourseId,_tmpSemesterId,_tmpDateEpochDay,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpNotes,_tmpIsCompleted,_tmpCreatedAtEpochMs);
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
  public Flow<List<ExamEntity>> getUpcomingExams(final long todayEpochDay) {
    final String _sql = "SELECT * FROM exams WHERE isCompleted = 0 AND dateEpochDay >= ? ORDER BY dateEpochDay ASC, startTimeMinutes ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, todayEpochDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exams"}, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
          final int _cursorIndexOfSemesterId = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterId");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAtEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochMs");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCourseId;
            if (_cursor.isNull(_cursorIndexOfCourseId)) {
              _tmpCourseId = null;
            } else {
              _tmpCourseId = _cursor.getLong(_cursorIndexOfCourseId);
            }
            final Long _tmpSemesterId;
            if (_cursor.isNull(_cursorIndexOfSemesterId)) {
              _tmpSemesterId = null;
            } else {
              _tmpSemesterId = _cursor.getLong(_cursorIndexOfSemesterId);
            }
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final Integer _tmpEndTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfEndTimeMinutes)) {
              _tmpEndTimeMinutes = null;
            } else {
              _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            }
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final long _tmpCreatedAtEpochMs;
            _tmpCreatedAtEpochMs = _cursor.getLong(_cursorIndexOfCreatedAtEpochMs);
            _item = new ExamEntity(_tmpId,_tmpTitle,_tmpCourseId,_tmpSemesterId,_tmpDateEpochDay,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpNotes,_tmpIsCompleted,_tmpCreatedAtEpochMs);
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
  public Flow<List<ExamEntity>> getExamsForDate(final long dateEpochDay) {
    final String _sql = "SELECT * FROM exams WHERE dateEpochDay = ? ORDER BY startTimeMinutes ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dateEpochDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exams"}, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
          final int _cursorIndexOfSemesterId = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterId");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAtEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochMs");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCourseId;
            if (_cursor.isNull(_cursorIndexOfCourseId)) {
              _tmpCourseId = null;
            } else {
              _tmpCourseId = _cursor.getLong(_cursorIndexOfCourseId);
            }
            final Long _tmpSemesterId;
            if (_cursor.isNull(_cursorIndexOfSemesterId)) {
              _tmpSemesterId = null;
            } else {
              _tmpSemesterId = _cursor.getLong(_cursorIndexOfSemesterId);
            }
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final Integer _tmpEndTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfEndTimeMinutes)) {
              _tmpEndTimeMinutes = null;
            } else {
              _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            }
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final long _tmpCreatedAtEpochMs;
            _tmpCreatedAtEpochMs = _cursor.getLong(_cursorIndexOfCreatedAtEpochMs);
            _item = new ExamEntity(_tmpId,_tmpTitle,_tmpCourseId,_tmpSemesterId,_tmpDateEpochDay,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpNotes,_tmpIsCompleted,_tmpCreatedAtEpochMs);
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
  public Flow<List<ExamEntity>> getExamsForSemester(final long semesterId) {
    final String _sql = "SELECT * FROM exams WHERE semesterId = ? ORDER BY dateEpochDay ASC, startTimeMinutes ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, semesterId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exams"}, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
          final int _cursorIndexOfSemesterId = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterId");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAtEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochMs");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCourseId;
            if (_cursor.isNull(_cursorIndexOfCourseId)) {
              _tmpCourseId = null;
            } else {
              _tmpCourseId = _cursor.getLong(_cursorIndexOfCourseId);
            }
            final Long _tmpSemesterId;
            if (_cursor.isNull(_cursorIndexOfSemesterId)) {
              _tmpSemesterId = null;
            } else {
              _tmpSemesterId = _cursor.getLong(_cursorIndexOfSemesterId);
            }
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final Integer _tmpEndTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfEndTimeMinutes)) {
              _tmpEndTimeMinutes = null;
            } else {
              _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            }
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final long _tmpCreatedAtEpochMs;
            _tmpCreatedAtEpochMs = _cursor.getLong(_cursorIndexOfCreatedAtEpochMs);
            _item = new ExamEntity(_tmpId,_tmpTitle,_tmpCourseId,_tmpSemesterId,_tmpDateEpochDay,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpNotes,_tmpIsCompleted,_tmpCreatedAtEpochMs);
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
  public Flow<List<ExamEntity>> getCompletedExams() {
    final String _sql = "SELECT * FROM exams WHERE isCompleted = 1 ORDER BY dateEpochDay DESC, startTimeMinutes DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exams"}, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
          final int _cursorIndexOfSemesterId = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterId");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAtEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochMs");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCourseId;
            if (_cursor.isNull(_cursorIndexOfCourseId)) {
              _tmpCourseId = null;
            } else {
              _tmpCourseId = _cursor.getLong(_cursorIndexOfCourseId);
            }
            final Long _tmpSemesterId;
            if (_cursor.isNull(_cursorIndexOfSemesterId)) {
              _tmpSemesterId = null;
            } else {
              _tmpSemesterId = _cursor.getLong(_cursorIndexOfSemesterId);
            }
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final Integer _tmpEndTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfEndTimeMinutes)) {
              _tmpEndTimeMinutes = null;
            } else {
              _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            }
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final long _tmpCreatedAtEpochMs;
            _tmpCreatedAtEpochMs = _cursor.getLong(_cursorIndexOfCreatedAtEpochMs);
            _item = new ExamEntity(_tmpId,_tmpTitle,_tmpCourseId,_tmpSemesterId,_tmpDateEpochDay,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpNotes,_tmpIsCompleted,_tmpCreatedAtEpochMs);
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
  public Flow<ExamEntity> getExamById(final long id) {
    final String _sql = "SELECT * FROM exams WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exams"}, new Callable<ExamEntity>() {
      @Override
      @Nullable
      public ExamEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
          final int _cursorIndexOfSemesterId = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterId");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAtEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochMs");
          final ExamEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCourseId;
            if (_cursor.isNull(_cursorIndexOfCourseId)) {
              _tmpCourseId = null;
            } else {
              _tmpCourseId = _cursor.getLong(_cursorIndexOfCourseId);
            }
            final Long _tmpSemesterId;
            if (_cursor.isNull(_cursorIndexOfSemesterId)) {
              _tmpSemesterId = null;
            } else {
              _tmpSemesterId = _cursor.getLong(_cursorIndexOfSemesterId);
            }
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final Integer _tmpEndTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfEndTimeMinutes)) {
              _tmpEndTimeMinutes = null;
            } else {
              _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            }
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final long _tmpCreatedAtEpochMs;
            _tmpCreatedAtEpochMs = _cursor.getLong(_cursorIndexOfCreatedAtEpochMs);
            _result = new ExamEntity(_tmpId,_tmpTitle,_tmpCourseId,_tmpSemesterId,_tmpDateEpochDay,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpNotes,_tmpIsCompleted,_tmpCreatedAtEpochMs);
          } else {
            _result = null;
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
  public Object getExamByIdSync(final long id, final Continuation<? super ExamEntity> $completion) {
    final String _sql = "SELECT * FROM exams WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExamEntity>() {
      @Override
      @Nullable
      public ExamEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
          final int _cursorIndexOfSemesterId = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterId");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAtEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochMs");
          final ExamEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final Long _tmpCourseId;
            if (_cursor.isNull(_cursorIndexOfCourseId)) {
              _tmpCourseId = null;
            } else {
              _tmpCourseId = _cursor.getLong(_cursorIndexOfCourseId);
            }
            final Long _tmpSemesterId;
            if (_cursor.isNull(_cursorIndexOfSemesterId)) {
              _tmpSemesterId = null;
            } else {
              _tmpSemesterId = _cursor.getLong(_cursorIndexOfSemesterId);
            }
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final Integer _tmpEndTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfEndTimeMinutes)) {
              _tmpEndTimeMinutes = null;
            } else {
              _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            }
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final long _tmpCreatedAtEpochMs;
            _tmpCreatedAtEpochMs = _cursor.getLong(_cursorIndexOfCreatedAtEpochMs);
            _result = new ExamEntity(_tmpId,_tmpTitle,_tmpCourseId,_tmpSemesterId,_tmpDateEpochDay,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpNotes,_tmpIsCompleted,_tmpCreatedAtEpochMs);
          } else {
            _result = null;
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
