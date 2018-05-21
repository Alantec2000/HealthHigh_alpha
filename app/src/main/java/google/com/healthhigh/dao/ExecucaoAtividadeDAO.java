package google.com.healthhigh.dao;

import android.content.Context;

public class ExecucaoAtividadeDAO extends DAO {
    public static final String
            TABLE_NAME = "hhwm_execucao_atividade",
            ID = "id_execucao_atividade",
            ID_INTERACAO_ATIVIDADE = "i_id_interacao_atividade_execucao_atividade",
            DATA_INICIO = "i_data_inicio_execucao_atividade",
            DATA_FIM = "i_data_fim_execucao_atividade",
            NUMERO_PASSOS = "i_numero_passos";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER NOT NULL PRIMARY KEY, " +
                ID_INTERACAO_ATIVIDADE + " INTEGER DEFAULT NULL, " +
                DATA_INICIO + " INTEGER NOT NULL, " +
                DATA_FIM + " INTEGER DEFAULT NULL, " +
                NUMERO_PASSOS + " INTEGER NOT NULL DEFAULT 0, " +
                "FOREIGN KEY(" + ID_INTERACAO_ATIVIDADE + ") REFERENCES " + InteracaoAtividadeDAO.TABLE_NAME + "(" + InteracaoAtividadeDAO.ID + ") ON DELETE SET NULL " +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public ExecucaoAtividadeDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
