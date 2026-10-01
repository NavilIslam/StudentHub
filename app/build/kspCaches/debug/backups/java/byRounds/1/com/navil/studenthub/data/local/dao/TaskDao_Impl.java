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
import com.navil.studenthub.data.local.entity.TaskEntity;
import com.navil.studenthub.model.Priority;
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
public final class TaskDao_Impl implements TaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TaskEntity> __insertionAdapterOfTaskEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<TaskEntity> __deletionAdapterOfTaskEntity;

  private final EntityDeletionOrUpdateAdapter<TaskEntity> __updateAdapterOfTaskEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTaskCompletion;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTaskById;

  public TaskDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTaskEntity = new EntityInsertionAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `tasks` (`id`,`title`,`course`,`description`,`dueDateEpochDay`,`dueTimeMinutes`,`priority`,`hasReminder`,`isCompleted`,`createdAtEpochMs`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getCourse());
        statement.bindString(4, entity.getDescription());
        statement.bindLong(5, entity.getDueDateEpochDay());
        if (entity.getDueTimeMinutes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDueTimeMinutes());
        }
        final String _tmp = __converters.fromPriority(entity.getPriority());
        statement.bindString(7, _tmp);
        final int _tmp_1 = entity.getHasReminder() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        final int _tmp_2 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        statement.bindLong(10, entity.getCreatedAtEpochMs());
      }
    };
    this.__deletionAdapterOfTaskEntity = new EntityDeletionOrUpdateAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `tasks` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTaskEntity = new EntityDeletionOrUpdateAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `tasks` SET `id` = ?,`title` = ?,`course` = ?,`description` = ?,`dueDateEpochDay` = ?,`dueTimeMinutes` = ?,`priority` = ?,`hasReminder` = ?,`isCompleted` = ?,`createdAtEpochMs` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getCourse());
        statement.bindString(4, entity.getDescription());
        statement.bindLong(5, entity.getDueDateEpochDay());
        if (entity.getDueTimeMinutes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getDueTimeMinutes());
        }
        final String _tmp = __converters.fromPriority(entity.getPriority());
        statement.bindString(7, _tmp);
        final int _tmp_1 = entity.getHasReminder() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        final int _tmp_2 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        statement.bindLong(10, entity.getCreatedAtEpochMs());
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateTaskCompletion = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE tasks SET isCompleted = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteTaskById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM tasks WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTask(final TaskEntity task, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTaskEntity.insertAndReturnId(task);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTask(final TaskEntity task, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTaskEntity.handle(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTask(final TaskEntity task, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTaskEntity.handle(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTaskCompletion(final long id, final boolean isCompleted,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTaskCompletion.acquire();
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
          __preparedStmtOfUpdateTaskCompletion.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTaskById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTaskById.acquire();
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
          __preparedStmtOfDeleteTaskById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TaskEntity>> getAllTasks() {
    final String _sql = "SELECT * FROM tasks ORDER BY isCompleted ASC, dueDateEpochDay ASC, dueTimeMinutes ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDueDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateEpochDay");
          final int _cursorIndexOfDueTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "dueTimeMinutes");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfHasReminder = CursorUtil.getColumnIndexOrThrow(_cursor, "hasReminder");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAtEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochMs");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpCourse;
            _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final long _tmpDueDateEpochDay;
            _tmpDueDateEpochDay = _cursor.getLong(_cursorIndexOfDueDateEpochDay);
            final Integer _tmpDueTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfDueTimeMinutes)) {
              _tmpDueTimeMinutes = null;
            } else {
              _tmpDueTimeMinutes = _cursor.getInt(_cursorIndexOfDueTimeMinutes);
            }
            final Priority _tmpPriority;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfPriority);
            _tmpPriority = __converters.toPriority(_tmp);
            final boolean _tmpHasReminder;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHasReminder);
            _tmpHasReminder = _tmp_1 != 0;
            final boolean _tmpIsCompleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_2 != 0;
            final long _tmpCreatedAtEpochMs;
            _tmpCreatedAtEpochMs = _cursor.getLong(_cursorIndexOfCreatedAtEpochMs);
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpCourse,_tmpDescription,_tmpDueDateEpochDay,_tmpDueTimeMinutes,_tmpPriority,_tmpHasReminder,_tmpIsCompleted,_tmpCreatedAtEpochMs);
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
  public Flow<List<TaskEntity>> getTasksForDay(final long epochDay) {
    final String _sql = "SELECT * FROM tasks WHERE dueDateEpochDay = ? ORDER BY isCompleted ASC, dueTimeMinutes ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, epochDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDueDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateEpochDay");
          final int _cursorIndexOfDueTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "dueTimeMinutes");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfHasReminder = CursorUtil.getColumnIndexOrThrow(_cursor, "hasReminder");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAtEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochMs");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpCourse;
            _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final long _tmpDueDateEpochDay;
            _tmpDueDateEpochDay = _cursor.getLong(_cursorIndexOfDueDateEpochDay);
            final Integer _tmpDueTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfDueTimeMinutes)) {
              _tmpDueTimeMinutes = null;
            } else {
              _tmpDueTimeMinutes = _cursor.getInt(_cursorIndexOfDueTimeMinutes);
            }
            final Priority _tmpPriority;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfPriority);
            _tmpPriority = __converters.toPriority(_tmp);
            final boolean _tmpHasReminder;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHasReminder);
            _tmpHasReminder = _tmp_1 != 0;
            final boolean _tmpIsCompleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_2 != 0;
            final long _tmpCreatedAtEpochMs;
            _tmpCreatedAtEpochMs = _cursor.getLong(_cursorIndexOfCreatedAtEpochMs);
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpCourse,_tmpDescription,_tmpDueDateEpochDay,_tmpDueTimeMinutes,_tmpPriority,_tmpHasReminder,_tmpIsCompleted,_tmpCreatedAtEpochMs);
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
  public Flow<List<TaskEntity>> getUpcomingTasks(final int limit) {
    final String _sql = "SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY dueDateEpochDay ASC, dueTimeMinutes ASC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDueDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateEpochDay");
          final int _cursorIndexOfDueTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "dueTimeMinutes");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfHasReminder = CursorUtil.getColumnIndexOrThrow(_cursor, "hasReminder");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAtEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochMs");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpCourse;
            _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final long _tmpDueDateEpochDay;
            _tmpDueDateEpochDay = _cursor.getLong(_cursorIndexOfDueDateEpochDay);
            final Integer _tmpDueTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfDueTimeMinutes)) {
              _tmpDueTimeMinutes = null;
            } else {
              _tmpDueTimeMinutes = _cursor.getInt(_cursorIndexOfDueTimeMinutes);
            }
            final Priority _tmpPriority;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfPriority);
            _tmpPriority = __converters.toPriority(_tmp);
            final boolean _tmpHasReminder;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHasReminder);
            _tmpHasReminder = _tmp_1 != 0;
            final boolean _tmpIsCompleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_2 != 0;
            final long _tmpCreatedAtEpochMs;
            _tmpCreatedAtEpochMs = _cursor.getLong(_cursorIndexOfCreatedAtEpochMs);
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpCourse,_tmpDescription,_tmpDueDateEpochDay,_tmpDueTimeMinutes,_tmpPriority,_tmpHasReminder,_tmpIsCompleted,_tmpCreatedAtEpochMs);
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
  public Object getTaskById(final long id, final Continuation<? super TaskEntity> $completion) {
    final String _sql = "SELECT * FROM tasks WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TaskEntity>() {
      @Override
      @Nullable
      public TaskEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDueDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDateEpochDay");
          final int _cursorIndexOfDueTimeMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "dueTimeMinutes");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfHasReminder = CursorUtil.getColumnIndexOrThrow(_cursor, "hasReminder");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCreatedAtEpochMs = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochMs");
          final TaskEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpCourse;
            _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final long _tmpDueDateEpochDay;
            _tmpDueDateEpochDay = _cursor.getLong(_cursorIndexOfDueDateEpochDay);
            final Integer _tmpDueTimeMinutes;
            if (_cursor.isNull(_cursorIndexOfDueTimeMinutes)) {
              _tmpDueTimeMinutes = null;
            } else {
              _tmpDueTimeMinutes = _cursor.getInt(_cursorIndexOfDueTimeMinutes);
            }
            final Priority _tmpPriority;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfPriority);
            _tmpPriority = __converters.toPriority(_tmp);
            final boolean _tmpHasReminder;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHasReminder);
            _tmpHasReminder = _tmp_1 != 0;
            final boolean _tmpIsCompleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_2 != 0;
            final long _tmpCreatedAtEpochMs;
            _tmpCreatedAtEpochMs = _cursor.getLong(_cursorIndexOfCreatedAtEpochMs);
            _result = new TaskEntity(_tmpId,_tmpTitle,_tmpCourse,_tmpDescription,_tmpDueDateEpochDay,_tmpDueTimeMinutes,_tmpPriority,_tmpHasReminder,_tmpIsCompleted,_tmpCreatedAtEpochMs);
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
