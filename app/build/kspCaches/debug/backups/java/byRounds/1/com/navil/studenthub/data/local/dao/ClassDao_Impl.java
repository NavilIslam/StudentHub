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
import com.navil.studenthub.data.local.database.Converters;
import com.navil.studenthub.data.local.entity.ClassEntity;
import com.navil.studenthub.model.AcademicDayOfWeek;
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
public final class ClassDao_Impl implements ClassDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ClassEntity> __insertionAdapterOfClassEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ClassEntity> __deletionAdapterOfClassEntity;

  private final EntityDeletionOrUpdateAdapter<ClassEntity> __updateAdapterOfClassEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteClassById;

  public ClassDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfClassEntity = new EntityInsertionAdapter<ClassEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `classes` (`id`,`subject`,`instructor`,`dayOfWeek`,`startTimeMinutes`,`endTimeMinutes`,`room`,`notes`,`colorHex`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ClassEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSubject());
        statement.bindString(3, entity.getInstructor());
        final String _tmp = __converters.fromAcademicDayOfWeek(entity.getDayOfWeek());
        statement.bindString(4, _tmp);
        statement.bindLong(5, entity.getStartTimeMinutes());
        statement.bindLong(6, entity.getEndTimeMinutes());
        statement.bindString(7, entity.getRoom());
        statement.bindString(8, entity.getNotes());
        statement.bindString(9, entity.getColorHex());
      }
    };
    this.__deletionAdapterOfClassEntity = new EntityDeletionOrUpdateAdapter<ClassEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `classes` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ClassEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfClassEntity = new EntityDeletionOrUpdateAdapter<ClassEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `classes` SET `id` = ?,`subject` = ?,`instructor` = ?,`dayOfWeek` = ?,`startTimeMinutes` = ?,`endTimeMinutes` = ?,`room` = ?,`notes` = ?,`colorHex` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ClassEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getSubject());
        statement.bindString(3, entity.getInstructor());
        final String _tmp = __converters.fromAcademicDayOfWeek(entity.getDayOfWeek());
        statement.bindString(4, _tmp);
        statement.bindLong(5, entity.getStartTimeMinutes());
        statement.bindLong(6, entity.getEndTimeMinutes());
        statement.bindString(7, entity.getRoom());
        statement.bindString(8, entity.getNotes());
        statement.bindString(9, entity.getColorHex());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteClassById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM classes WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertClass(final ClassEntity classEntity,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfClassEntity.insertAndReturnId(classEntity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteClass(final ClassEntity classEntity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfClassEntity.handle(classEntity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateClass(final ClassEntity classEntity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfClassEntity.handle(classEntity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteClassById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteClassById.acquire();
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
          __preparedStmtOfDeleteClassById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ClassEntity>> getAllClasses() {
    final String _sql = "SELECT * FROM classes ORDER BY startTimeMinutes ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"classes"}, new Callable<List<ClassEntity>>() {
      @Override
      @NonNull
      public List<ClassEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
          final int _cursorIndexOfInstructor = CursorUtil.getColumnIndexOrThrow(_cursor, "instructor");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final List<ClassEntity> _result = new ArrayList<ClassEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ClassEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSubject;
            _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
            final String _tmpInstructor;
            _tmpInstructor = _cursor.getString(_cursorIndexOfInstructor);
            final AcademicDayOfWeek _tmpDayOfWeek;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfDayOfWeek);
            _tmpDayOfWeek = __converters.toAcademicDayOfWeek(_tmp);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final int _tmpEndTimeMinutes;
            _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            _item = new ClassEntity(_tmpId,_tmpSubject,_tmpInstructor,_tmpDayOfWeek,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpNotes,_tmpColorHex);
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
  public Flow<List<ClassEntity>> getClassesForDay(final AcademicDayOfWeek dayOfWeek) {
    final String _sql = "SELECT * FROM classes WHERE dayOfWeek = ? ORDER BY startTimeMinutes ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromAcademicDayOfWeek(dayOfWeek);
    _statement.bindString(_argIndex, _tmp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"classes"}, new Callable<List<ClassEntity>>() {
      @Override
      @NonNull
      public List<ClassEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
          final int _cursorIndexOfInstructor = CursorUtil.getColumnIndexOrThrow(_cursor, "instructor");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final List<ClassEntity> _result = new ArrayList<ClassEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ClassEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSubject;
            _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
            final String _tmpInstructor;
            _tmpInstructor = _cursor.getString(_cursorIndexOfInstructor);
            final AcademicDayOfWeek _tmpDayOfWeek;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfDayOfWeek);
            _tmpDayOfWeek = __converters.toAcademicDayOfWeek(_tmp_1);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final int _tmpEndTimeMinutes;
            _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            _item = new ClassEntity(_tmpId,_tmpSubject,_tmpInstructor,_tmpDayOfWeek,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpNotes,_tmpColorHex);
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
  public Object getClassById(final long id, final Continuation<? super ClassEntity> $completion) {
    final String _sql = "SELECT * FROM classes WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ClassEntity>() {
      @Override
      @Nullable
      public ClassEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
          final int _cursorIndexOfInstructor = CursorUtil.getColumnIndexOrThrow(_cursor, "instructor");
          final int _cursorIndexOfDayOfWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "dayOfWeek");
          final int _cursorIndexOfStartTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startTimeMinutes");
          final int _cursorIndexOfEndTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endTimeMinutes");
          final int _cursorIndexOfRoom = CursorUtil.getColumnIndexOrThrow(_cursor, "room");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final ClassEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpSubject;
            _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
            final String _tmpInstructor;
            _tmpInstructor = _cursor.getString(_cursorIndexOfInstructor);
            final AcademicDayOfWeek _tmpDayOfWeek;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfDayOfWeek);
            _tmpDayOfWeek = __converters.toAcademicDayOfWeek(_tmp);
            final int _tmpStartTimeMinutes;
            _tmpStartTimeMinutes = _cursor.getInt(_cursorIndexOfStartTimeMinutes);
            final int _tmpEndTimeMinutes;
            _tmpEndTimeMinutes = _cursor.getInt(_cursorIndexOfEndTimeMinutes);
            final String _tmpRoom;
            _tmpRoom = _cursor.getString(_cursorIndexOfRoom);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            _result = new ClassEntity(_tmpId,_tmpSubject,_tmpInstructor,_tmpDayOfWeek,_tmpStartTimeMinutes,_tmpEndTimeMinutes,_tmpRoom,_tmpNotes,_tmpColorHex);
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
