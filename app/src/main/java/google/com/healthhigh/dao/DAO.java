package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import google.com.healthhigh.db.CreateDB;

public abstract class DAO extends CreateDB {
    public static final String SQLITE_ERROR = "SQLITE ERROR";
    public final String TABLE_NAME = "";
    protected SQLiteDatabase write_db = null;
    protected SQLiteDatabase read_db = null;//Não tem diferença do write_db, somente na legibilidade do código...
    protected Context context;


    public DAO(Context context) {
        super(context);
        this.context = context;
        write_db = CreateDB.getDBInstance(context).getWritableDatabase();
        read_db = CreateDB.getDBInstance(context).getReadableDatabase();
    }

    public static String createColumns(String[] columns){
        return TextUtils.join(",", columns);
    }

    protected void imprimeErroSQLite(SQLiteException e) {
        Log.e(SQLITE_ERROR, e.getMessage());
        e.printStackTrace();
    }

    protected interface Behavior{
        void setContent(Cursor c);
    }

    protected Cursor executeSelect(String select) {
        return read_db.rawQuery(select, null);
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
        } catch (Exception e){
           Log.e(SQLITE_ERROR, e.getMessage());
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
        long ret = 0;
        try{
            ret = write_db.insert(table_name, null, cv);
        } catch (SQLiteException e){
            imprimeErroSQLite(e);
        }
        return ret;
    }

    protected boolean update(String table_name, ContentValues cv, String where, String[] where_params){
        int rows = 0;
        try{
            rows = write_db.update(table_name, cv, where, where_params);
        } catch (SQLiteException e){
            imprimeErroSQLite(e);
        }
        return (rows > 0);
    }

    protected void setContent(Cursor c, List<Object> o){}

    protected void setContent(Cursor c){}

    protected abstract void prepareContentReceiver();
}
