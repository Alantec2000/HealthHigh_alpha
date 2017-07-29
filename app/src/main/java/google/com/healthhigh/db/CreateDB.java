package google.com.healthhigh.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import google.com.healthhigh.dao.ColaboradorDAO;
import google.com.healthhigh.dao.DesafioXMetaDAO;
import google.com.healthhigh.dao.ItemDAO;
import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.dao.ItemXMetaDAO;
import google.com.healthhigh.dao.MetaDAO;

public class CreateDB extends SQLiteOpenHelper{
    private static String NOME_BANCO = "healthHigh";
    private static int VERSAO = 2;
    private static CreateDB db = null;

    public static CreateDB getDBInstance(Context context){
        if(db == null)
            db = new CreateDB(context);
        return db;
    }

    public CreateDB(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(ColaboradorDAO.getCreateTableString());
        db.execSQL(DesafioDAO.getCreateTableString());
        db.execSQL(DesafioXMetaDAO.getCreateTableString());
        db.execSQL(MetaDAO.getCreateTableString());
        db.execSQL(ItemDAO.getCreateTableString());
        db.execSQL(ItemXMetaDAO.getCreateTableString());
    }

    private void dropTables(SQLiteDatabase db) {
        db.execSQL(DesafioDAO.getDropTableString());
        db.execSQL(DesafioXMetaDAO.getDropTableString());
        db.execSQL(ItemDAO.getDropTableString());
        db.execSQL(ItemXMetaDAO.getDropTableString());
        db.execSQL(MetaDAO.getDropTableString());
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
