package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;

import google.com.healthhigh.domain.InteracaoNoticia;

public class InteracaoNoticiaDAO extends DAO {
    public static final String
            TABLE_NAME = "hhmw_interacao_noticia",
            ID = "id_interacao_noticia",
            ID_PUBLICACAO = "id_publicacao_interacao_noticia",
            ID_NOTICIA = "id_noticia_interacao_noticia",
            DATA_VISUALIZACAO = "i_data_visualizacao_interacao_noticia",
            DATA_CRIACAO = "i_data_criacao_interacao_noticia",
            TEMPO_LEITURA = "i_tempo_leitura_interacao_noticia";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + InteracaoNoticiaDAO.TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY NOT NULL, " +
                    ID_NOTICIA + " INTEGER NOT NULL, " +
                    ID_PUBLICACAO + " INTEGER NOT NULL, " +
                    DATA_CRIACAO + " INTEGER NOT NULL, " +
                    DATA_VISUALIZACAO + " INTEGER DEFAULT 0, " +
                    TEMPO_LEITURA + " INTEGER DEFAULT 0," +
                    "UNIQUE(" + ID_NOTICIA + ", " + ID_PUBLICACAO + "), " +
                    "FOREIGN KEY(" + InteracaoNoticiaDAO.ID_PUBLICACAO + ") REFERENCES " + PublicacaoDAO.TABLE_NAME + "(" + PublicacaoDAO.ID + ")," +
                    "FOREIGN KEY(" + InteracaoNoticiaDAO.ID_NOTICIA+ ") REFERENCES " + NoticiaDAO.TABLE_NAME + "(" + NoticiaDAO.ID + ")" +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public InteracaoNoticiaDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public boolean atualizaInteracaoNoticia(InteracaoNoticia interacao_noticia) {
        ContentValues cv = getContentValues(interacao_noticia);
        boolean rows = update(TABLE_NAME, cv, ID + "=?", new String[]{String.valueOf(interacao_noticia.getId())});
        return rows;
    }

    private ContentValues getContentValues(InteracaoNoticia interacao_noticia) {
        ContentValues cv = new ContentValues();
        if(interacao_noticia.getIdMeta() != 0){
            cv.put(ID_NOTICIA, interacao_noticia.getNoticia().getId());
        }
        if(interacao_noticia.getIdPublicacao() != 0){
            cv.put(ID_PUBLICACAO, interacao_noticia.getIdPublicacao());
        }
        cv.put(DATA_CRIACAO, interacao_noticia.getData_criacao());
        cv.put(DATA_VISUALIZACAO, interacao_noticia.getData_visualizacao());
        cv.put(TEMPO_LEITURA, interacao_noticia.getTempo_leitura());
        return cv;
    }

    public void inserirInteracaoNoticia(InteracaoNoticia i_n) {
        ContentValues cv = getContentValues(i_n);
        long id = insert(TABLE_NAME, cv);
        if(id > 0){
            i_n.setId(id);
        } else {
            i_n = null;
        }
    }
}
