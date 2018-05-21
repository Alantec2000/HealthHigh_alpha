package google.com.healthhigh.dao;

import android.content.Context;

public class DesafioAtividadeDAO extends DAO {
    public static final String
            TABLE_NAME = "hhwm_desafio_atividade",
            ID = "id_desafio_atividade",
            ID_DESAFIO = "i_id_desafio_desafio_atividade",
            ID_ATIVIDADE = "i_id_atividade_desafio_atividade";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER NOT NULL PRIMARY KEY, " +
                ID_DESAFIO + " INTEGER NOT NULL, " +
                ID_ATIVIDADE + " INTEGER NOT NULL, " +
                "UNIQUE(" + ID_ATIVIDADE + ", " + ID_DESAFIO + "), " +
                "FOREIGN KEY(" + ID_DESAFIO + ") REFERENCES " + DesafioDAO.TABLE_NAME + "(" + DesafioDAO.ID + "), " +
                "FOREIGN KEY(" + ID_ATIVIDADE + ") REFERENCES " + AtividadeDAO.TABLE_NAME + "(" + AtividadeDAO.ID + ") " +
                ");";
    }

    public DesafioAtividadeDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
