package com.kyview.util;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kyview.util.AdViewReqManager.ReqInfoItem;

public class DBOpenHelper extends SQLiteOpenHelper {
	protected static final String DBNAME_REQ = "reqinfo.db";

	private static final int VERSION = 1;

	public DBOpenHelper(Context context, String dbName) {
		super(context, dbName, null, VERSION);
	}

	@Override
	public synchronized void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS variable"
				+ "(id integer primary key,"
				+ "name varchar(100), value varchar(1000))");
	}

	@Override
	public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS variable");
		
		onCreate(db);
	}
	
	public synchronized void testVariable() {
		addVariable(0, "test_0", "test_value_0");
		addVariable(1, "test_0", "test_value_1");
		
		ArrayList<String> values = getVariable("test_0");
		for (String val:values)
			AdViewUtil.logInfo(val);
		
		delVariable("test_0");
		values = getVariable("test_0");
	}
	
	public synchronized void addVariable(int id, String name, String value) {
		addVariable(id, name, value, false);
	}
	
	public synchronized int getMaxId() {
		int nRet = 0;
		SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select max(id) from variable", new String[]{});
        while(cursor.moveToNext()){
            nRet = cursor.getInt(0);
            break;
        }
        cursor.close();
        db.close();
		return nRet;
	}
	
	public synchronized void addVariable(int id, String name, String value, boolean makeUnique) {
        SQLiteDatabase db = getWritableDatabase();
    	if (makeUnique) {
    		db.execSQL("delete from variable where name=?", new Object[]{name});
    	}
        db.beginTransaction();
        try{
            db.execSQL("insert into variable(id, name, value) values(?, ?, ?)",
            		new Object[]{Integer.valueOf(id), name, value});
            db.setTransactionSuccessful();
        }finally{
            db.endTransaction();
        }
        db.close();		
	}
	
	public synchronized void delVariable(int id) {
		delVariable(Integer.valueOf(id), null, null);
	}
	
	public synchronized void delVariable(String name) {
		delVariable(null, name, null);
	}
	
    public synchronized void delVariable(Integer id, String name, String value){
        SQLiteDatabase db = getWritableDatabase();
        
        if (null != id)
        	db.execSQL("delete from variable where id=?", new Object[]{id});
        else if (null != name && null != value)
        	db.execSQL("delete from variable where name=? and value=?", new Object[]{name, value});
        else if (null != name)
        	db.execSQL("delete from variable where name=?", new Object[]{name});
        else if (null != value)
        	db.execSQL("delete from variable where value=?", new Object[]{value});
        
        db.close();
    }
    
    public synchronized ArrayList<ReqInfoItem> getReqInfoItems(){
        SQLiteDatabase db = getReadableDatabase();
        
        Cursor cursor = db.rawQuery("select id,name,value from variable", new String[]{});
        ArrayList<ReqInfoItem> data = new ArrayList<ReqInfoItem>();
        while(cursor.moveToNext()){
        	try {
        		ReqInfoItem item = new ReqInfoItem();
        		item.mId = cursor.getInt(0);
        		item.mDataTime = Long.parseLong(cursor.getString(1));
        		item.mDataArr = new JSONArray(cursor.getString(2));
        		data.add(item);
        	} catch (Exception e) {
        		AdViewUtil.logError("", e);
        	}
        }
        cursor.close();
        db.close();
        return data;
    }
	
    public synchronized ArrayList<String> getVariable(String name){
        SQLiteDatabase db = getReadableDatabase();
        
        Cursor cursor = db.rawQuery("select value from variable where name=?", new String[]{name});
        ArrayList<String> data = new ArrayList<String>();
        while(cursor.moveToNext()){
            data.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return data;
    }
}