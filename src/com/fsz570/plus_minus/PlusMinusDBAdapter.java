package com.fsz570.plus_minus;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.fsz570.plus_minus.util.VoUtil;
import com.fsz570.plus_minus.vo.VoOp2;
import com.fsz570.plus_minus.vo.VoOp3;
import com.fsz570.plus_minus.vo.VoOp4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlusMinusDBAdapter extends SQLiteOpenHelper {
	private static final String TAG = "PlusMinusDBAdapter";

	private static String DB_PATH = "/data/data/com.fsz570.plus_minus/databases/";
	private static final String DB_NAME = "plus_minus.db";
	private static final String OP2_TABLE_NAME = "op2";
	private static final String OP3_TABLE_NAME = "op3";
	private static final String OP4_TABLE_NAME = "op4";
	private static final String HIGH_SCORE_TABLE_NAME = "HIGH_SCORE";
	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_OP2 = "create table op2 (_id integer primary key, "
			+ "num1 integer not null, op1 integer not null, "
			+ "num2 integer not null, op2 integer not null, "
			+ "num3 integer not null);";

	private static final String CREATE_OP3 = "create table op3 (_id integer primary key, "
			+ "num1 integer not null, op1 integer not null, "
			+ "num2 integer not null, op2 integer not null, "
			+ "num3 integer not null, op3 integer not null, "
			+ "num4 integer not null);";

	private static final String CREATE_OP4 = "create table op4 (_id integer primary key, "
			+ "num1 integer not null, op1 integer not null, "
			+ "num2 integer not null, op2 integer not null, "
			+ "num3 integer not null, op3 integer not null, "
			+ "num4 integer not null, op4 integer not null, "
			+ "num5 integer not null);";
	
	private static final String CREATE_HIGH_SCORE = "create table high_score (_id integer primary key, "
			+ "name text not null, score integer not null);";

	private final Context context;
	private SQLiteDatabase theDataBase;

	public PlusMinusDBAdapter(Context _context) {
		super(_context, DB_NAME, null, 1);
		context = _context;
	}

	// Called when no database exists in
	// disk and the helper class needs
	// to create a new one.
	@Override
	public void onCreate(SQLiteDatabase _db) {
		_db.beginTransaction();

		try {
			// Try to copy predefined db
			Log.d(TAG, "Create DB");
			createDataBase();
		} catch (Exception e) {
			Log.d(TAG, "Copy DB Fail, create new one.");
			_db.execSQL(CREATE_OP2);
			_db.execSQL(CREATE_OP3);
			_db.execSQL(CREATE_OP4);
			_db.execSQL(CREATE_HIGH_SCORE);

			List<VoOp2> list2 = VoUtil.getOp2List(context);
			insertOp2(_db, list2);

			List<VoOp3> list3 = VoUtil.getOp3List(context);
			insertOp3(_db, list3);

			List<VoOp4> list4 = VoUtil.getOp4List(context);
			insertOp4(_db, list4);
		}

		_db.endTransaction();
	}

	// Called when there is a database version mismatch meaning that
	// the version of the database on disk needs to be upgraded to
	// the current version.
	@Override
	public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
		// Log the version upgrade.
		Log.w(TAG, "Upgrading from version " + _oldVersion + " to "
				+ _newVersion + ", which will destroy all old data");
		// Upgrade the existing database to conform to the new version.
		// Multiple previous versions can be handled by comparing
		// _oldVersion and _newVersion values.
		// The simplest case is to drop the old table and create a
		// new one.

		// Create a new one.
		onCreate(_db);
	}

	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		theDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	@Override
	public synchronized void close() {
		if (theDataBase != null)
			theDataBase.close();
		super.close();
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
			this.getReadableDatabase();
			Log.d(TAG, "DB Exist, try to copy another one!");
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		} else {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}

		if (checkDB != null) {
			Log.d(TAG, "DB Exists!");
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = context.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	private void insertOp2(SQLiteDatabase _db, List<VoOp2> list) {
		for (VoOp2 vo : list) {
			// Log.d(TAG, String.valueOf(vo.getId()));
			_db.insert(OP2_TABLE_NAME, null, transferContentValues(vo));
		}
	}

	private void insertOp3(SQLiteDatabase _db, List<VoOp3> list) {
		for (VoOp3 vo : list) {
			// Log.d(TAG, String.valueOf(vo.getId()));
			_db.insert(OP3_TABLE_NAME, null, transferContentValues(vo));
		}
	}

	private void insertOp4(SQLiteDatabase _db, List<VoOp4> list) {
		for (VoOp4 vo : list) {
			// Log.d(TAG, String.valueOf(vo.getId()));
			_db.insert(OP4_TABLE_NAME, null, transferContentValues(vo));
		}
	}
	
	public void updateHighScore(String name, int score){
		openDataBase();
		
		ContentValues values = new ContentValues();
		values.put("_id", 2);
		values.put("name", name);
		values.put("score", score);
		
		theDataBase.beginTransaction();
		int rows = theDataBase.update(HIGH_SCORE_TABLE_NAME, values, "_id=?", new String[]{"1"});
		//long rows = theDataBase.insert(HIGH_SCORE_TABLE_NAME, null, values);
		Log.d(TAG, "rawId : " + rows);
		theDataBase.endTransaction();
		close();
	}

	public ContentValues transferContentValues(VoOp2 vo) {
		ContentValues values = new ContentValues();
		values.put("_id", vo.getId());
		values.put("num1", vo.getNum1());
		values.put("op1", vo.getOp1());
		values.put("num2", vo.getNum2());
		values.put("op2", vo.getOp2());
		values.put("num3", vo.getNum3());

		return values;
	}

	public ContentValues transferContentValues(VoOp3 vo) {
		ContentValues values = new ContentValues();
		values.put("_id", vo.getId());
		values.put("num1", vo.getNum1());
		values.put("op1", vo.getOp1());
		values.put("num2", vo.getNum2());
		values.put("op2", vo.getOp2());
		values.put("num3", vo.getNum3());
		values.put("op3", vo.getOp3());
		values.put("num4", vo.getNum4());

		return values;
	}

	public ContentValues transferContentValues(VoOp4 vo) {
		ContentValues values = new ContentValues();
		values.put("_id", vo.getId());
		values.put("num1", vo.getNum1());
		values.put("op1", vo.getOp1());
		values.put("num2", vo.getNum2());
		values.put("op2", vo.getOp2());
		values.put("num3", vo.getNum3());
		values.put("op3", vo.getOp3());
		values.put("num4", vo.getNum4());
		values.put("op4", vo.getOp4());
		values.put("num5", vo.getNum5());

		return values;
	}

	public VoOp2 fetchOp2(int id) {
		VoOp2 vo = null;

		Log.d(TAG, "id : " + id);
		openDataBase();

		Cursor cur = theDataBase.query(true, OP2_TABLE_NAME, new String[] {
				"_id", "num1", "op1", "num2", "op2", "num3" }, "_id=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cur != null) {
			cur.moveToFirst();
			Log.d(TAG, "Count : " + cur.getCount());

			vo = new VoOp2(cur.getInt(0), cur.getInt(1), cur.getInt(2),
					cur.getInt(3), cur.getInt(4), cur.getInt(5));
		}
		cur.close();

		close();
		return vo;
	}

	public VoOp3 fetchOp3(int id) {
		VoOp3 vo = null;

		openDataBase();
		Cursor cur = theDataBase.query(true, OP3_TABLE_NAME, new String[] {
				"_id", "num1", "op1", "num2", "op2", "num3", "op3", "num4" },
				"_id=?", new String[] { String.valueOf(id) }, null, null, null,
				null);
		if (cur != null) {
			cur.moveToFirst();

			vo = new VoOp3(cur.getInt(0), cur.getInt(1), cur.getInt(2),
					cur.getInt(3), cur.getInt(4), cur.getInt(5), cur.getInt(6),
					cur.getInt(7));
		}
		cur.close();

		close();
		return vo;
	}

	public VoOp4 fetchOp4(int id) {
		VoOp4 vo = null;

		openDataBase();

		Cursor cur = theDataBase.query(true, OP4_TABLE_NAME, new String[] {
				"_id", "num1", "op1", "num2", "op2", "num3", "op3", "num4",
				"op4", "num5" }, "_id=?", new String[] { String.valueOf(id) },
				null, null, null, null);
		if (cur != null) {
			cur.moveToFirst();

			vo = new VoOp4(cur.getInt(0), cur.getInt(1), cur.getInt(2),
					cur.getInt(3), cur.getInt(4), cur.getInt(5), cur.getInt(6),
					cur.getInt(7), cur.getInt(8), cur.getInt(9));
		}
		cur.close();

		close();
		return vo;
	}

}
