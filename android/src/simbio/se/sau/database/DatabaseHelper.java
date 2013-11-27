package simbio.se.sau.database;

import java.util.ArrayList;
import java.util.List;

import simbio.se.sau.API;
import simbio.se.shiva.Shiva;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;

/**
 * The SQLite Helper, extends it to use the helper
 * 
 * @author Ademar Alves de Oliveira ademar111190@gmail.com
 * @date 2013-sep-30 17:16:17
 * @since {@link API#Version_3_0_0}
 */
public abstract class DatabaseHelper extends SQLiteOpenHelper {

	// class fields
	protected DatabaseDelegate delegate;
	protected Handler handler;

	/**
	 * @return a {@link List} of {@link Class} of models useds on sql
	 */
	protected abstract List<Class<?>> getModelsToBeCreatedOnDatabase();

	/**
	 * Default constructor
	 * 
	 * @param context
	 *            the {@link Context}
	 * @param delegate
	 *            the {@link DatabaseDelegate} to handle the events
	 * @param databaseName
	 *            an name to be used on database, not send <code>null</code>
	 * @param databaseVersion
	 *            the version of database, is a good idea start with 1 and add more 1 each time you need update the database structure
	 * @since {@link API#Version_3_0_0}
	 */
	public DatabaseHelper(Context context, DatabaseDelegate delegate, String databaseName, int databaseVersion) {
		this(context, delegate, databaseName, databaseVersion, null);
	}

	/**
	 * Constructor with {@link CursorFactory}
	 * 
	 * @param context
	 *            the {@link Context}
	 * @param delegate
	 *            the {@link DatabaseDelegate} to handle the events
	 * @param databaseName
	 *            an name to be used on database, not send <code>null</code>
	 * @param databaseVersion
	 *            the version of database, is a good idea start with 1 and add more 1 each time you need update the database structure, more details see {@link SQLiteOpenHelper#onUpgrade(SQLiteDatabase, int, int)} and
	 *            {@link SQLiteOpenHelper#onDowngrade(SQLiteDatabase, int, int)}
	 * @param cursorFactory
	 *            the {@link CursorFactory}, can be <code>null</code>
	 * @since {@link API#Version_3_0_0}
	 */
	public DatabaseHelper(Context context, DatabaseDelegate delegate, String databaseName, int databaseVersion, CursorFactory cursorFactory) {
		super(context, databaseName, cursorFactory, databaseVersion);
		this.delegate = delegate;
		this.handler = new Handler();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		List<Class<?>> clazzez = getModelsToBeCreatedOnDatabase();
		if (clazzez != null)
			for (Class<?> clazz : clazzez)
				onCreateHelper(db, clazz);
	}

	/**
	 * Only a {@link DatabaseHelper#onCreate(SQLiteDatabase)} macro helper
	 * 
	 * @param db
	 *            the {@link SQLiteDatabase} to call the {@link SQLiteDatabase#execSQL(String)} method
	 * @param clazz
	 *            a {@link Class} to get sql create query with {@link Shiva#toCreateTableQuery(Class)}
	 * @since {@link API#Version_3_0_0}
	 */
	protected void onCreateHelper(SQLiteDatabase db, Class<?> clazz) {
		db.execSQL(Shiva.toCreateTableQuery(clazz));
	}

	/**
	 * This method send to delegate the return error of sqlite, it means, the sqlite query will run on backgrounds {@link Thread}, so this method return to main {@link Thread} and send the return
	 * 
	 * @param requestId
	 *            the request id
	 * @param exception
	 *            the {@link Exception}
	 * @see DatabaseDelegate#onRequestFail(DatabaseHelper, int, Exception)
	 * @since {@link API#Version_3_0_0}
	 */
	protected void sendRequestFail(final int requestId, final Exception exception) {
		if (delegate != null)
			handler.post(new Runnable() {
				@Override
				public void run() {
					delegate.onRequestFail(DatabaseHelper.this, requestId, exception);
				}
			});
	}

	/**
	 * This method send to delegate the return success of sqlite, it means, the sqlite query will run on backgrounds {@link Thread}, so this method return to main {@link Thread} and send the return
	 * 
	 * @param requestId
	 *            the request id
	 * @param object
	 *            the {@link Object} returned
	 * @see DatabaseDelegate#onRequestSuccess(DatabaseHelper, int, Object)
	 * @since {@link API#Version_3_0_0}
	 */
	public void sendRequestSuccess(final int requestId, final Object object) {
		if (delegate != null)
			handler.post(new Runnable() {
				@Override
				public void run() {
					delegate.onRequestSuccess(DatabaseHelper.this, requestId, object);
				}
			});
	}

	/**
	 * Inserta {@link Object} on database, this will run on a background {@link Thread}
	 * 
	 * @param object
	 *            the {@link Object} to be saved
	 * @param requestId
	 *            the id of request to be handled on {@link DatabaseDelegate#onRequestSuccess(DatabaseHelper, int, Object)} or {@link DatabaseDelegate#onRequestFail(DatabaseHelper, int, Exception)}
	 * @since {@link API#Version_3_0_0}
	 */
	public void insert(Object object, int requestId) {
		if (object == null)
			sendRequestFail(requestId, new NullPointerException());
		else {
			ArrayList<Object> objects = new ArrayList<Object>();
			objects.add(object);
			insert(objects, requestId);
		}
	}

	/**
	 * Inserta {@link Object} on database, this will run on a background {@link Thread}
	 * 
	 * @param objects
	 *            an {@link ArrayList} of {@link Object} to be saved
	 * @param requestId
	 *            the id of request to be handled on {@link DatabaseDelegate#onRequestSuccess(DatabaseHelper, int, Object)} or {@link DatabaseDelegate#onRequestFail(DatabaseHelper, int, Exception)}
	 * @since {@link API#Version_3_0_0}
	 */
	public void insert(final ArrayList<Object> objects, final int requestId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (objects == null)
					sendRequestFail(requestId, new NullPointerException());
				else {
					SQLiteDatabase sqLiteDatabase = getWritableDatabase();
					for (Object object : objects)
						if (object != null)
							sqLiteDatabase.execSQL(Shiva.toInsertQuery(object));
					sendRequestSuccess(requestId, objects);
				}
			}
		}).start();
	}

	/**
	 * This method run a select query on database
	 * 
	 * @param clazz
	 *            the {@link Class} to be selected, it means, the return will be a list of that objects. a <code>null</code> causes {@link DatabaseDelegate#onRequestFail(DatabaseHelper, int, Exception)} with {@link NullPointerException}
	 * @param requestId
	 *            the id of request to be handled on {@link DatabaseDelegate#onRequestSuccess(DatabaseHelper, int, Object)} or {@link DatabaseDelegate#onRequestFail(DatabaseHelper, int, Exception)}
	 * @see SQLiteDatabase#query(String, String[], String, String[], String, String, String)
	 * @since {@link API#Version_3_0_0}
	 */
	public void select(final Class<?> clazz, final int requestId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (clazz == null) {
					sendRequestFail(requestId, new NullPointerException());
					return;
				}
				ArrayList<Object> objects = new ArrayList<Object>();
				SQLiteDatabase sqLiteDatabase = getReadableDatabase();
				Cursor cursor = sqLiteDatabase.rawQuery(Shiva.toSelectQuery(clazz), null);
				if (cursor.moveToFirst())
					do {
						objects.add(ShivaAndroidUtils.getObjectFromCursor(clazz, cursor));
					} while (cursor.moveToNext());
				sendRequestSuccess(requestId, objects);
				cursor.close();
			}
		}).start();
	}

	/**
	 * Delete all data, Caution!
	 * 
	 * @param requestId
	 *            the reques id
	 * @param clazz
	 *            the {@link Class} type to be deleted on database
	 * @since {@link API#Version_3_0_0}
	 */
	public void clearTable(final int requestId, final Class<?> clazz) {
		if (clazz == null)
			sendRequestFail(requestId, new NullPointerException());
		else {
			ArrayList<Class<?>> clazzez = new ArrayList<Class<?>>();
			clazzez.add(clazz);
			clearTables(requestId, clazzez);
		}
	}

	/**
	 * Delete all data, Caution!
	 * 
	 * @param requestId
	 *            the reques id
	 * @param clazzez
	 *            the {@link Class} type to be deleted on database
	 * @since {@link API#Version_3_0_0}
	 */
	public void clearTables(final int requestId, final ArrayList<Class<?>> clazzez) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				SQLiteDatabase sqLiteDatabase = getWritableDatabase();
				for (Class<?> clazz : clazzez)
					if (clazz != null)
						sqLiteDatabase.delete(Shiva.toTableName(clazz), null, null);
				sendRequestSuccess(requestId, null);
			}
		}).start();
	}

	/**
	 * Update {@link Object} on database, this will run on a background {@link Thread}
	 * 
	 * @param object
	 *            the {@link Object} to be saved
	 * @param requestId
	 *            the id of request to be handled on {@link DatabaseDelegate#onRequestSuccess(DatabaseHelper, int, Object)} or {@link DatabaseDelegate#onRequestFail(DatabaseHelper, int, Exception)}
	 * @since {@link API#Version_3_0_0}
	 */
	public void update(Object object, int requestId) {
		if (object == null)
			sendRequestFail(requestId, new NullPointerException());
		else {
			ArrayList<Object> objects = new ArrayList<Object>();
			objects.add(object);
			update(objects, requestId);
		}
	}

	/**
	 * Update {@link Object} on database, this will run on a background {@link Thread}
	 * 
	 * @param objects
	 *            an {@link ArrayList} of {@link Object} to be saved
	 * @param requestId
	 *            the id of request to be handled on {@link DatabaseDelegate#onRequestSuccess(DatabaseHelper, int, Object)} or {@link DatabaseDelegate#onRequestFail(DatabaseHelper, int, Exception)}
	 * @since {@link API#Version_3_0_0}
	 */
	public void update(final ArrayList<Object> objects, final int requestId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (objects == null)
					sendRequestFail(requestId, new NullPointerException());
				else {
					SQLiteDatabase sqLiteDatabase = getWritableDatabase();
					for (Object object : objects)
						if (object != null)
							sqLiteDatabase.execSQL(Shiva.toUpdateQuery(object));
					sendRequestSuccess(requestId, objects);
				}
			}
		}).start();
	}

	/**
	 * Delete {@link Object} on database, this will run on a background {@link Thread}
	 * 
	 * @param object
	 *            the {@link Object} to be saved
	 * @param requestId
	 *            the id of request to be handled on {@link DatabaseDelegate#onRequestSuccess(DatabaseHelper, int, Object)} or {@link DatabaseDelegate#onRequestFail(DatabaseHelper, int, Exception)}
	 * @since {@link API#Version_3_0_0}
	 */
	public void delete(Object object, int requestId) {
		if (object == null)
			sendRequestFail(requestId, new NullPointerException());
		else {
			ArrayList<Object> objects = new ArrayList<Object>();
			objects.add(object);
			delete(objects, requestId);
		}
	}

	/**
	 * Update {@link Object} on database, this will run on a background {@link Thread}
	 * 
	 * @param objects
	 *            an {@link ArrayList} of {@link Object} to be saved
	 * @param requestId
	 *            the id of request to be handled on {@link DatabaseDelegate#onRequestSuccess(DatabaseHelper, int, Object)} or {@link DatabaseDelegate#onRequestFail(DatabaseHelper, int, Exception)}
	 * @since {@link API#Version_3_0_0}
	 */
	public void delete(final ArrayList<Object> objects, final int requestId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (objects == null)
					sendRequestFail(requestId, new NullPointerException());
				else {
					SQLiteDatabase sqLiteDatabase = getWritableDatabase();
					for (Object object : objects)
						if (object != null)
							sqLiteDatabase.execSQL(Shiva.toDeletQuery(object));
					sendRequestSuccess(requestId, objects);
				}
			}
		}).start();
	}

}
