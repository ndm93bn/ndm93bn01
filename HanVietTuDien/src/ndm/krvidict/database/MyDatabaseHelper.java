package ndm.krvidict.database;

import ndm.krvidict.Word;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * Ho tro lam viec voi database
 * 
 * @author ndm93bn
 * 
 */
public class MyDatabaseHelper extends SQLiteAssetHelper {

	/** ten database */
	private static final String DATABASE_NAME = "krvidict.mp3";

	/** database version */
	private static final int DATABASE_VERSION = 1;

	/** helper */
	private static MyDatabaseHelper helper;
	
	//private Context context;

	/**
	 * Ham khoi tao Database Helper
	 * 
	 * @param context
	 */
	public MyDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Get database helper ko can mo va dong
	 * 
	 * @param context
	 * @return
	 */
	public static synchronized MyDatabaseHelper getInstance(Context context) {

		if (helper == null) {
			helper = new MyDatabaseHelper(context);
		}

		return helper;
	}

	// Kiểm tra database rổng
	public int checkDatabaseNull() {

		Cursor mCount = getWritableDatabase().rawQuery(
				"select count(*) from korvidict ", null);

		mCount.moveToFirst();
		// lấy số lượng truy vấn
		int count = mCount.getInt(0);
		return count;

	}

	public Word[] read(String searchTerm) {
		SQLiteDatabase db = this.getWritableDatabase();
		// select query
		String sql = "";
		sql += "SELECT * FROM korvi_dict";
		sql += " WHERE  word  LIKE '" + searchTerm + "%'";
		sql += " ORDER BY word_id ASC";
		sql += " LIMIT 0,10";
		String sql2 = "";
		sql2 += "SELECT * FROM vikor_dict";
		sql2 += " WHERE  word  LIKE '" + searchTerm + "%'";
		sql2 += " ORDER BY word_id ASC";
		sql2 += " LIMIT 0,10";

		// execute the query
		Cursor cursor2 = db.rawQuery(sql2, null);

		int recCount2 = cursor2.getCount();

		// execute the query
		Cursor cursor = db.rawQuery(sql, null);

		int recCount = cursor.getCount();

		Word[] ObjectItemData = new Word[recCount + recCount2 + 1];
		
	    int x = 0; 
	
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {

				Word word = new Word(cursor.getString(cursor
						.getColumnIndex("word")), cursor.getInt(cursor
						.getColumnIndex("word_id")), cursor.getString(cursor
						.getColumnIndex("def")), 1);

				ObjectItemData[x] = word;

				x++;

			} while (cursor.moveToNext());
		}

		// looping through all rows and adding to list
		if (cursor2.moveToFirst()) {
			do {

				Word word = new Word(cursor2.getString(cursor2
						.getColumnIndex("word")), cursor2.getInt(cursor2
						.getColumnIndex("word_id")), cursor2.getString(cursor2
						.getColumnIndex("def")), 0);

				ObjectItemData[x] = word;

				x++;

			} while (cursor2.moveToNext());
		}

		cursor.close();
		cursor2.close();
		db.close();

		return ObjectItemData;

	}

	public Word getWordById(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		// select query
		String sql = "";
		sql += "SELECT * FROM korvi_dict";
		sql += " WHERE  word_id  = '" + id + "'";
		sql += " ORDER BY word_id ASC";
		sql += " LIMIT 0,1";
		String sql2 = "";
		sql2 += "SELECT * FROM vikor_dict";
		sql2 += " WHERE  word_id  = '" + id + "'";
		sql2 += " ORDER BY word_id ASC";
		sql2 += " LIMIT 0,1";

		// execute the query
		Cursor cursor2 = db.rawQuery(sql2, null);

		// execute the query
		Cursor cursor = db.rawQuery(sql, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {

			Word word = new Word(
					cursor.getString(cursor.getColumnIndex("word")),
					cursor.getInt(cursor.getColumnIndex("word_id")),
					cursor.getString(cursor.getColumnIndex("def")), 1);
			word.pron = cursor.getString(cursor.getColumnIndex("word"));

			return word;
		}

		// looping through all rows and adding to list
		if (cursor2.moveToFirst()) {

			Word word = new Word(cursor2.getString(cursor2
					.getColumnIndex("word"))
					+ " "
					+ cursor2.getString(cursor2.getColumnIndex("pron")),
					cursor2.getInt(cursor2.getColumnIndex("word_id")),
					cursor2.getString(cursor2.getColumnIndex("def")), 0);
			word.pron = cursor2.getString(cursor2.getColumnIndex("pron"));

			return word;

		}

		cursor.close();
		cursor2.close();

		db.close();
		return null;

	}
	
	public Word getWord(String  w) {
		SQLiteDatabase db = this.getWritableDatabase();
		// select query
		String sql = "";
		sql += "SELECT * FROM korvi_dict";
		sql += " WHERE  word  LIKE '" + w + "'";
		sql += " ORDER BY word_id ASC";
		sql += " LIMIT 0,1";
		String sql2 = "";
		sql2 += "SELECT * FROM vikor_dict";
		sql2 += " WHERE  word  LIKE '" + w + "'";
		sql2 += " ORDER BY word_id ASC";
		sql2 += " LIMIT 0,1";

		// execute the query
		Cursor cursor2 = db.rawQuery(sql2, null);

		// execute the query
		Cursor cursor = db.rawQuery(sql, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {

			Word word = new Word(
					cursor.getString(cursor.getColumnIndex("word")),
					cursor.getInt(cursor.getColumnIndex("word_id")),
					cursor.getString(cursor.getColumnIndex("def")), 1);
			word.pron = cursor.getString(cursor.getColumnIndex("word"));

			return word;
		}

		// looping through all rows and adding to list
		if (cursor2.moveToFirst()) {

			Word word = new Word(cursor2.getString(cursor2
					.getColumnIndex("word"))
					+ " "
					+ cursor2.getString(cursor2.getColumnIndex("pron")),
					cursor2.getInt(cursor2.getColumnIndex("word_id")),
					cursor2.getString(cursor2.getColumnIndex("def")), 0);
			word.pron = cursor2.getString(cursor2.getColumnIndex("pron"));

			return word;

		}

		cursor.close();
		cursor2.close();

		db.close();
		return null;

	}

	public String traTu(Word word) {
		 
		// select query
		SQLiteDatabase db = this.getWritableDatabase();

		if (word.type == 1) {
			String sql = "";
			sql += "SELECT * FROM korvi_def";
			sql += " WHERE  word_id  = '" + word.id + "'";

			// execute the query
			Cursor cursor = db.rawQuery(sql, null);

			StringBuilder str = new StringBuilder();
			str.append("<head><style type='text/css'>ul {list-style: none;margin-left: 5px;  padding-left: 1.2em;  text-indent: -1.2em;}"
					+ "li:before {  content: '►'; display: block; float: left; width: 1.2em; color:#f1c40f;} li{margin-bottom:10px}</style></head>");
			str.append("<div style= 'background-color:#fff; padding:6px;margin:0px; border-radius:5px'>");
			str.append("<div style='color:#3498db; font-size:20px'><b>Ý nghĩa:</b><hr></div>");

			if (cursor.moveToFirst()) {
				int i = 0;
				do {
					i++;
					str.append("<div style='color:#d35400; font-size:18px'><b>"
							+ i
							+ ". "
							+ cursor.getString(cursor
									.getColumnIndex("def_type"))
							+ cursor.getString(cursor.getColumnIndex("def"))
							+ "</b></div>");
					String s = "SELECT * FROM korvi_dex WHERE def_id = "
							+ cursor.getInt(cursor.getColumnIndex("_id"));
					Cursor cursor_korvi_def = db.rawQuery(s, null);
					if (cursor_korvi_def.moveToFirst()) {
						str.append("<ul>");
						do {
							String s2 = "SELECT * FROM korvi_ex WHERE _id = "
									+ cursor_korvi_def.getInt(cursor_korvi_def
											.getColumnIndex("ex_id"));
							Cursor cursor_korvi_ex = db.rawQuery(s2, null);
							if (cursor_korvi_ex.moveToFirst()) {
								str.append("<li><span style='color:green'>"
										+ cursor_korvi_ex.getString(cursor_korvi_ex
												.getColumnIndex("kor_sen"))
										+ "</span><br>"
										+ cursor_korvi_ex.getString(cursor_korvi_ex
												.getColumnIndex("vi_sen"))
										+ "</li>");

							}
							cursor_korvi_ex.close();

						} while (cursor_korvi_def.moveToNext());

						str.append("</ul>");
						cursor_korvi_def.close();
					}

				} while (cursor.moveToNext());
				cursor.close();

				String sqlex = "";
				sqlex += "SELECT * FROM korvi_ex";
				sqlex += " WHERE  kor_sen  like '" + word.objectName + "%'";

				// execute the query
				Cursor cursorex = db.rawQuery(sqlex, null);
				if (cursorex.moveToFirst()) {
					str.append("<br><div style='color:#3498db; font-size:20px'><b>Ví dụ:</b><hr></div>");
					int k = 0;
					str.append("<ul>");
					do {
						str.append("<li><span style='color:green'>"
								+ cursorex.getString(cursorex
										.getColumnIndex("kor_sen"))
								+ "</span><br> "
								+ cursorex.getString(cursorex
										.getColumnIndex("vi_sen"))
								+ "</br></li>");

					} while (cursorex.moveToNext());
					str.append("</ul>");
				}

				cursorex.close();

				str.append("</div>");
				return str.toString();

			} else
				return null;
		}

		// tra tu viet han
		else {
			String sql = "";
			sql += "SELECT * FROM vikor_def";
			sql += " WHERE  word_id  = '" + word.id + "'";

			// execute the query
			Cursor cursor = db.rawQuery(sql, null);

			StringBuilder str = new StringBuilder();
			str.append("<head><style type='text/css'>ul {list-style: none;margin-left: 5px;  padding-left: 1.2em;  text-indent: -1.2em;}"
					+ "li:before {  content: '►'; display: block; float: left; width: 1.2em; color:#f1c40f;} li{margin-bottom:10px}</style></head>");
			str.append("<div style= 'background-color:#fff; padding:6px;margin:0px; border-radius:5px'>");
			str.append("<div style='color:#3498db; font-size:20px'><b>Ý nghĩa:</b><hr></div>");

			if (cursor.moveToFirst()) {
				int i = 0;
				do {
					i++;
					str.append("<div style='color:#d35400; font-size:18px'><b>"
							+ i
							+ ". "
							+ cursor.getString(cursor
									.getColumnIndex("def_type"))
							+ cursor.getString(cursor.getColumnIndex("def"))
							+ "</b></div>");

				} while (cursor.moveToNext());
				cursor.close();

				String sqlex = "";
				sqlex += "SELECT * FROM korvi_ex";
				sqlex += " WHERE  vi_sen  like '" + word.objectName + "%'";

				// execute the query
				Cursor cursorex = db.rawQuery(sqlex, null);
				if (cursorex.moveToFirst()) {
					str.append("<br><div style='color:#3498db; font-size:20px'><b>Ví dụ:</b><hr></div>");
					int k = 0;
					str.append("<ul>");
					do {
						str.append("<li><span style='color:green'>"
								+ cursorex.getString(cursorex
										.getColumnIndex("vi_sen"))
								+ "</span><br> "
								+ cursorex.getString(cursorex
										.getColumnIndex("kor_sen"))
								+ "</br></li>");

					} while (cursorex.moveToNext());
					str.append("</ul>");
				}

				cursorex.close();

				str.append("</div>");
				return str.toString();

			} else
				return null;

		}

	}

}
