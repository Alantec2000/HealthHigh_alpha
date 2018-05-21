package google.com.healthhigh.dao;

import android.content.Context;

public class InteracaoAtividadeDAO extends DAO {
    public static final String
            TABLE_NAME = "hhwm_interacao_atividade",
            ID = "id_interacao_atividade",
            ID_ATIVIDADE = "i_id_atividade_interacao_atividade",
            ID_PUBLICACAO = "i_id_publicacao_interacao_atividade",
            DATA_INICIO = "i_data_inicio_interacao_atividade",
            DATA_CRIACAO = "i_data_criacao_interacao_atividade",
            DATA_CONCLUSAO = "i_data_conclusao_interacao_atividade";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER NOT NULL PRIMARY KEY, " +
                ID_ATIVIDADE + " INTEGER NOT NULL, " +
                ID_PUBLICACAO + " INTEGER NOT NULL, " +
                DATA_CRIACAO + " INTEGER NOT NULL, " +
                DATA_INICIO + " INTEGER NOT NULL, " +
                DATA_CONCLUSAO + " INTEGER NOT NULL DEFAULT 0, " +
                "UNIQUE(" + ID_PUBLICACAO + ", " + ID_ATIVIDADE + ")" +
                "FOREIGN KEY(" + ID_ATIVIDADE + ") REFERENCES " + AtividadeDAO.TABLE_NAME + "(" + AtividadeDAO.ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + ID_PUBLICACAO + ") REFERENCES " + PublicacaoDAO.TABLE_NAME + "(" + PublicacaoDAO.ID + ") ON DELETE CASCADE " +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public InteracaoAtividadeDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
