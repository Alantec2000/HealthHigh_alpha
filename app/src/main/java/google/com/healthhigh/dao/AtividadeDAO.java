package google.com.healthhigh.dao;

import android.content.Context;
import android.text.TextUtils;

import java.util.Map;

import google.com.healthhigh.domain.Atividade;
import google.com.healthhigh.domain.Desafio;

public class AtividadeDAO extends DAO {
    public static final String
            TABLE_NAME = "hhwm_atividade",
            ID = "id_atividade",
            ID_PREMIACAO= "i_id_premiacao_atividade",
            DATA_CRIACAO = "i_data_criacao_atividade",
            NOME = "s_nome_atividade",
            DESCRICAO = "s_descricao_atividade",
            DATA_VISUALIZACAO = "i_data_visualizacao_atividade",
            TEMPO_TOTAL = "i_tempo_total_atividade",
            TOTAL_PASSOS = " i_total_passos_atividade";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + "INTEGER NOT NULL PRIMARY KEY" +
                ID_PREMIACAO + " INTEGER NULL, " +
                DATA_CRIACAO + " INTEGER DEFAULT 0" +
                NOME + " TEXT NOT NULL, " +
                DESCRICAO + " TEXT NOT NULL, " +
                DATA_VISUALIZACAO + " INTEGER DEFAULT 0, " +
                TEMPO_TOTAL + " INTEGER NOT NULL, " +
                TOTAL_PASSOS + " INTEGER NOT NULL " +
                "FOREIGN KEY(" + ID_PREMIACAO + ") REFERENCES " + PremiacaoDAO.TABLE_NAME + "(" + PremiacaoDAO.ID + ")" +
                ")";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public AtividadeDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }

    // Função: Obter uma lista de atividades a partir do desafio que está sendo realizado no momento
    public Map<Long, Atividade> getAtividades(Desafio d) {
        String select =
                "SELECT a.*, d.* FROM " + TABLE_NAME + " as a " +
                "INNER JOIN " + DesafioAtividadeDAO.TABLE_NAME + " as da ON " +
                    "da." + DesafioAtividadeDAO.ID_ATIVIDADE + " = a." + AtividadeDAO.ID + " " +
                "INNER JOIN " + DesafioDAO.TABLE_NAME + " as d ON " +
                    "d." + DesafioDAO.ID + " = da." + DesafioAtividadeDAO.ID_DESAFIO;
        if(d != null){
            select += " WHERE d." + DesafioDAO.ID + " = " + String.valueOf(d.getId());
        }
        return null;
    }
}
