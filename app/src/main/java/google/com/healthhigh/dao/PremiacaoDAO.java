package google.com.healthhigh.dao;

import android.content.Context;

public class PremiacaoDAO extends DAO {
    public static final String
            TABLE_NAME = "hhwm_premiacao",
            ID = "id_premiacao",
            DATA_CRIACAO = "i_data_criacao_premiacao",
            DATA_VISUALIZACAO = "i_data_visualizacao",
            NOME = "s_nome_premiacao",
            DESCRICAO = "s_descricao_premiacao";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER NOT NULL PRIMARY KEY, " +
                DATA_CRIACAO + " INTEGER NOT NULL, " +
                DATA_VISUALIZACAO + " INTEGER NOT NULL DEFAULT 0, " +
                NOME + " TEXT NOT NULL, " +
                DESCRICAO + " TEXT NOT NULL " +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public PremiacaoDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
