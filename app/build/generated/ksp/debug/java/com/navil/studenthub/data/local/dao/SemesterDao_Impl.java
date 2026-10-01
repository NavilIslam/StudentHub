package com.navil.studenthub.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.navil.studenthub.data.local.entity.SemesterEntity;
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
public final class SemesterDao_Impl implements SemesterDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SemesterEntity> __insertionAdapterOfSemesterEntity;

  private final EntityDeletionOrUpdateAdapter<SemesterEntity> __deletionAdapterOfSemesterEntity;

  private final EntityDeletionOrUpdateAdapter<SemesterEntity> __updateAdapterOfSemesterEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSemesterById;

  private final SharedSQLiteStatement __preparedStmtOfClearAllCurrentFlags;

  private final SharedSQLiteStatement __preparedStmtOfMarkSemesterAsCurrent;

  private final SharedSQLiteStatement __preparedStmtOfClearAllSemesters;

  public SemesterDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSemesterEntity = new EntityInsertionAdapter<SemesterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `semesters` (`id`,`name`,`academicYear`,`semesterNumber`,`startDate`,`endDate`,`isCurrent`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SemesterEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getAcademicYear());
        statement.bindLong(4, entity.getSemesterNumber());
        statement.bindString(5, entity.getStartDate());
        statement.bindString(6, entity.getEndDate());
        final int _tmp = entity.isCurrent() ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
    this.__deletionAdapterOfSemesterEntity = new EntityDeletionOrUpdateAdapter<SemesterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `semesters` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SemesterEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfSemesterEntity = new EntityDeletionOrUpdateAdapter<SemesterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `semesters` SET `id` = ?,`name` = ?,`academicYear` = ?,`semesterNumber` = ?,`startDate` = ?,`endDate` = ?,`isCurrent` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SemesterEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getAcademicYear());
        statement.bindLong(4, entity.getSemesterNumber());
        statement.bindString(5, entity.getStartDate());
        statement.bindString(6, entity.getEndDate());
        final int _tmp = entity.isCurrent() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteSemesterById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM semesters WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllCurrentFlags = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE semesters SET isCurrent = 0";
        return _query;
      }
    };
    this.__preparedStmtOfMarkSemesterAsCurrent = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE semesters SET isCurrent = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllSemesters = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM semesters";
        return _query;
      }
    };
  }

  @Override
  public Object insertSemester(final SemesterEntity semester,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSemesterEntity.insertAndReturnId(semester);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSemester(final SemesterEntity semester,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSemesterEntity.handle(semester);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSemester(final SemesterEntity semester,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSemesterEntity.handle(semester);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object setCurrentSemester(final long semesterId,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> SemesterDao.DefaultImpls.setCurrentSemester(SemesterDao_Impl.this, semesterId, __cont), $completion);
  }

  @Override
  public Object deleteSemesterById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSemesterById.acquire();
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
          __preparedStmtOfDeleteSemesterById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllCurrentFlags(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllCurrentFlags.acquire();
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
          __preparedStmtOfClearAllCurrentFlags.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markSemesterAsCurrent(final long semesterId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkSemesterAsCurrent.acquire();
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
          __preparedStmtOfMarkSemesterAsCurrent.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllSemesters(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllSemesters.acquire();
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
          __preparedStmtOfClearAllSemesters.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SemesterEntity>> getAllSemesters() {
    final String _sql = "SELECT * FROM semesters ORDER BY isCurrent DESC, semesterNumber ASC, id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"semesters"}, new Callable<List<SemesterEntity>>() {
      @Override
      @NonNull
      public List<SemesterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAcademicYear = CursorUtil.getColumnIndexOrThrow(_cursor, "academicYear");
          final int _cursorIndexOfSemesterNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterNumber");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsCurrent = CursorUtil.getColumnIndexOrThrow(_cursor, "isCurrent");
          final List<SemesterEntity> _result = new ArrayList<SemesterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SemesterEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAcademicYear;
            _tmpAcademicYear = _cursor.getString(_cursorIndexOfAcademicYear);
            final int _tmpSemesterNumber;
            _tmpSemesterNumber = _cursor.getInt(_cursorIndexOfSemesterNumber);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpEndDate;
            _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            final boolean _tmpIsCurrent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCurrent);
            _tmpIsCurrent = _tmp != 0;
            _item = new SemesterEntity(_tmpId,_tmpName,_tmpAcademicYear,_tmpSemesterNumber,_tmpStartDate,_tmpEndDate,_tmpIsCurrent);
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
  public Flow<SemesterEntity> getSemesterById(final long id) {
    final String _sql = "SELECT * FROM semesters WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"semesters"}, new Callable<SemesterEntity>() {
      @Override
      @Nullable
      public SemesterEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAcademicYear = CursorUtil.getColumnIndexOrThrow(_cursor, "academicYear");
          final int _cursorIndexOfSemesterNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterNumber");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsCurrent = CursorUtil.getColumnIndexOrThrow(_cursor, "isCurrent");
          final SemesterEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAcademicYear;
            _tmpAcademicYear = _cursor.getString(_cursorIndexOfAcademicYear);
            final int _tmpSemesterNumber;
            _tmpSemesterNumber = _cursor.getInt(_cursorIndexOfSemesterNumber);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpEndDate;
            _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            final boolean _tmpIsCurrent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCurrent);
            _tmpIsCurrent = _tmp != 0;
            _result = new SemesterEntity(_tmpId,_tmpName,_tmpAcademicYear,_tmpSemesterNumber,_tmpStartDate,_tmpEndDate,_tmpIsCurrent);
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
  public Flow<SemesterEntity> getCurrentSemester() {
    final String _sql = "SELECT * FROM semesters WHERE isCurrent = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"semesters"}, new Callable<SemesterEntity>() {
      @Override
      @Nullable
      public SemesterEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAcademicYear = CursorUtil.getColumnIndexOrThrow(_cursor, "academicYear");
          final int _cursorIndexOfSemesterNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterNumber");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsCurrent = CursorUtil.getColumnIndexOrThrow(_cursor, "isCurrent");
          final SemesterEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAcademicYear;
            _tmpAcademicYear = _cursor.getString(_cursorIndexOfAcademicYear);
            final int _tmpSemesterNumber;
            _tmpSemesterNumber = _cursor.getInt(_cursorIndexOfSemesterNumber);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpEndDate;
            _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            final boolean _tmpIsCurrent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCurrent);
            _tmpIsCurrent = _tmp != 0;
            _result = new SemesterEntity(_tmpId,_tmpName,_tmpAcademicYear,_tmpSemesterNumber,_tmpStartDate,_tmpEndDate,_tmpIsCurrent);
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
  public Object getCurrentSemesterSync(final Continuation<? super SemesterEntity> $completion) {
    final String _sql = "SELECT * FROM semesters WHERE isCurrent = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SemesterEntity>() {
      @Override
      @Nullable
      public SemesterEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAcademicYear = CursorUtil.getColumnIndexOrThrow(_cursor, "academicYear");
          final int _cursorIndexOfSemesterNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterNumber");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsCurrent = CursorUtil.getColumnIndexOrThrow(_cursor, "isCurrent");
          final SemesterEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAcademicYear;
            _tmpAcademicYear = _cursor.getString(_cursorIndexOfAcademicYear);
            final int _tmpSemesterNumber;
            _tmpSemesterNumber = _cursor.getInt(_cursorIndexOfSemesterNumber);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpEndDate;
            _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            final boolean _tmpIsCurrent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCurrent);
            _tmpIsCurrent = _tmp != 0;
            _result = new SemesterEntity(_tmpId,_tmpName,_tmpAcademicYear,_tmpSemesterNumber,_tmpStartDate,_tmpEndDate,_tmpIsCurrent);
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

  @Override
  public Object getFirstSemesterSync(final Continuation<? super SemesterEntity> $completion) {
    final String _sql = "SELECT * FROM semesters ORDER BY id ASC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SemesterEntity>() {
      @Override
      @Nullable
      public SemesterEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAcademicYear = CursorUtil.getColumnIndexOrThrow(_cursor, "academicYear");
          final int _cursorIndexOfSemesterNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "semesterNumber");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfIsCurrent = CursorUtil.getColumnIndexOrThrow(_cursor, "isCurrent");
          final SemesterEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAcademicYear;
            _tmpAcademicYear = _cursor.getString(_cursorIndexOfAcademicYear);
            final int _tmpSemesterNumber;
            _tmpSemesterNumber = _cursor.getInt(_cursorIndexOfSemesterNumber);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpEndDate;
            _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            final boolean _tmpIsCurrent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCurrent);
            _tmpIsCurrent = _tmp != 0;
            _result = new SemesterEntity(_tmpId,_tmpName,_tmpAcademicYear,_tmpSemesterNumber,_tmpStartDate,_tmpEndDate,_tmpIsCurrent);
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

  @Override
  public Object getSemesterCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM semesters";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
