package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

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

    public static Noticia getNoticia(Cursor c){
        Noticia n = new Noticia();
        n.setId(c.getLong(c.getColumnIndex(ID)));
        n.setTitulo(c.getString(c.getColumnIndex(TITULO)));
        n.setCorpo(c.getString(c.getColumnIndex(CORPO)));
        n.setData_visualizacao(c.getLong(c.getColumnIndex(DATA_VISUALIZACAO)));
        n.setData_criacao(c.getLong(c.getColumnIndex(DATA_CRIACAO)));
        return n;
    }

    private ContentValues getContentValues(Noticia n) {
        ContentValues cv = new ContentValues();
        if(n.getId() > 0){
            cv.put(ID, n.getId());
        }
        cv.put(CORPO, n.getCorpo());
        cv.put(TITULO, n.getTitulo());
        cv.put(DATA_CRIACAO, n.getData_criacao());
        cv.put(DATA_VISUALIZACAO, n.getData_visualizacao());
        return cv;
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public void inserirNoticia(Noticia n){
        ContentValues cv = getContentValues(n);
        long id = insert(TABLE_NAME, cv);
        if(id > 0){
            n.setId(id);
        } else {
            n = null;
        }
    }

    public boolean atualizarNoticia(Noticia n) {
        ContentValues cv = getContentValues(n);
        boolean rows = update(TABLE_NAME, cv, ID + " = ?", new String[]{String.valueOf(n.getId())});
        return rows;
    }
}
