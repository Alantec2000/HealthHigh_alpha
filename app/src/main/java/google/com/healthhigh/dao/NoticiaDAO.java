package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;

import google.com.healthhigh.domain.Noticia;

/**
 * Created by Alan on 24/04/2018.
 */

public class NoticiaDAO extends DAO {
    public final static String
            TABLE_NAME = "phh_noticia",
            ID = "id_noticia",
            TITULO = "s_titulo_noticia",
            CORPO = "s_corpo_noticia",
            DATA_VISUALIZACAO = "i_data_visualizacao_noticia",
            DATA_CRIACAO = "i_data_criacao_noticia";

    public static String getCreateTableString() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY NOT NULL," +
                    TITULO + " TEXT NOT NULL DEFAULT ''," +
                    CORPO + " TEXT NOT NULL DEFAULT 'TITULO', " +
                    DATA_CRIACAO + " INTEGER NOT NULL DEFAULT 0, " +
                    DATA_VISUALIZACAO + " INTEGER DEFAULT 0" +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public NoticiaDAO(Context context) {
        super(context);
    }

    public void inserirNoticia(Noticia n){
        ContentValues cv = getContentValues(n);
        long id_n = 0;
        try{

        } catch(SQLiteException e) {
            imprimeErroSQLite(e);
        } finally {
            if(id_n > 0) {
                n.setId(id_n);
            } else {
                n = null;
            }
        }
    }

    private ContentValues getContentValues(Noticia n) {
        ContentValues cv = new ContentValues();
        if(n.getId() > 0){
            cv.put(ID, n.getId());
        }
        cv.put(CORPO, n.getCorpo());
        cv.put(TITULO, n.getTitulo());
        return cv;
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
