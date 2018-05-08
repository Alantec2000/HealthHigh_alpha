package google.com.healthhigh.dao;

import android.content.Context;

public class DesafioNoticiaDAO extends DAO {
    private static final String
            TABLE_NAME = "hhwm_desafio_noticia",
            ID = "id_desafio_noticia",
            ID_NOTICIA = "i_id_noticia_desafio_noticia",
            ID_DESAFIO = "i_id_desafio_desafio_noticia",
            DATA_CRIACAO = "i_data_criacao";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY NOT NULL, " +
                    ID_NOTICIA + " INTEGER NOT NULL, " +
                    ID_DESAFIO + " INTEGER NOT NULL, " +
                    DATA_CRIACAO + " INTEGER NOT NULL, " +
                    "UNIQUE("+ID_DESAFIO+","+ID_NOTICIA+")," +
                    "FOREIGN KEY(" + ID_NOTICIA + ") REFERENCES " + NoticiaDAO.TABLE_NAME + " (" + NoticiaDAO.ID + ")," +
                    "FOREIGN KEY(" + ID_DESAFIO + ") REFERENCES " + DesafioDAO.TABLE_NAME + " (" + DesafioDAO.ID + ")" +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public DesafioNoticiaDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
