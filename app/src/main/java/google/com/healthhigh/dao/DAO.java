package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import google.com.healthhigh.db.CreateDB;

public abstract class DAO {
    public static final String SQLITE_ERROR = "SQLITE ERROR";
    public final String TABLE_NAME = "";
    final CreateDB db;
    protected final Context context;


    public DAO(Context context) {
        db = CreateDB.getDBInstance(context);
        this.context = context;
    }

    public SQLiteDatabase getReadableDatabase(){
        return db.getWrite();
    }

    public SQLiteDatabase getWritableDatabase(){
        return db.getWrite();
    }

    public static String createColumns(String[] columns){
        return TextUtils.join(",", columns);
    }

    protected void imprimeErroSQLite(SQLiteException e) {
        Log.e(SQLITE_ERROR, e.getMessage());
        e.printStackTrace();
    }

    public static interface Behavior{
        void setContent(Cursor c);
    }

    protected Cursor executeSelect(String select) {
        Cursor c = getReadableDatabase().rawQuery(select, null);
        return c;
    }

    protected void getSelectQueryContent(String select, Behavior behavior) {
        prepareContentReceiver();
        Cursor c = executeSelect(select);
        try {
            if (c.moveToFirst()) {
                do {
                    behavior.setContent(c);
                } while (c.moveToNext());
            }
        } catch (SQLiteException e){
           imprimeErroSQLite(e);
        } finally {
            c.close();
        }
    }

    protected void getSelectQueryContent(String select) {
        prepareContentReceiver();
        Cursor c = executeSelect(select);
        try {
            if (c.moveToFirst()) {
                do {
                    setContent(c);
                } while (c.moveToNext());
            }
        } catch (Exception e){
           Log.e(SQLITE_ERROR, e.getMessage());
        } finally {
            c.close();
        }
    }

    protected long insert(String table_name, ContentValues cv){
        synchronized (this){
            long ret = 0;
            try{
                ret = getWritableDatabase().insert(table_name, null, cv);
            } catch (SQLiteException e){
                imprimeErroSQLite(e);
            }
            return ret;
        }
    }

    protected boolean update(String table_name, ContentValues cv, String where, String[] where_params){
        synchronized (this){
            int rows = 0;
            try{
                rows = getWritableDatabase().update(table_name, cv, where, where_params);
            } catch (SQLiteException e){
                imprimeErroSQLite(e);
            }
            return (rows > 0);
        }
    }

    protected void setContent(Cursor c, List<Object> o){}

    protected void setContent(Cursor c){}

    protected abstract void prepareContentReceiver();

    public static int getInt(Cursor c, String col) {
        return !c.isNull(c.getColumnIndex(col)) ? c.getInt(c.getColumnIndex(col)) : 0;
    }

    public static long getLong(Cursor c, String col) {
        return !c.isNull(c.getColumnIndex(col)) ? c.getLong(c.getColumnIndex(col)) : 0;
    }

    public static String getString(Cursor c, String col) {
        return !c.isNull(c.getColumnIndex(col)) ? c.getString(c.getColumnIndex(col)) : "";
    }

    public interface Container<T> {
        public void add(T o);
        public void remove(long o);
        public T get(long id);
    }
}
