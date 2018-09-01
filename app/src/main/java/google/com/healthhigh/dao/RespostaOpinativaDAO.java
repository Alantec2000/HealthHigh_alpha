package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;

import google.com.healthhigh.domain.QuestaoOpinativa;
import google.com.healthhigh.domain.RespostaOpinativa;

/**
 * Created by Alan on 19/04/2018.
 */

public class RespostaOpinativaDAO extends DAO {
    public final static String TABLE_NAME = "phh_resposta_opinativa";
    public final static String ID = "id_resposta_optativa";
    public final static String ID_QUESTAO = "id_questao_opinativa_resposta_opinativa";
    public final static String OPINIAO = "s_opiniao_resposta_opinativa";
    public final static String ID_PUBLICACAO = "i_id_publicacao_resposta_opinativa";
    public final static String DATA_CRIACAO = "i_data_criacao_resposta_opinativa";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                OPINIAO + " TEXT NOT NULL, " +
                ID_QUESTAO + " INTEGER NOT NULL, " +
                ID_PUBLICACAO + " INTEGER, " +
                DATA_CRIACAO + " INTEGER, " +
                "UNIQUE(" + ID + "," + ID_QUESTAO + "," + ID_PUBLICACAO + ") ON CONFLICT REPLACE, " +
                "FOREIGN KEY(" + ID_QUESTAO + ") REFERENCES " + QuestaoOptativaDAO.TABLE_NAME + "(" + QuestaoOptativaDAO.ID + "), " +
                "FOREIGN KEY(" + ID_PUBLICACAO + ") REFERENCES " + PublicacaoDAO.TABLE_NAME + "(" + AlternativaDAO.ID + ") " +
                ");";
    }
    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
    public RespostaOpinativaDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public void inserirResposta(RespostaOpinativa ropn) {
        ContentValues cv = getContentValues(ropn);
        long new_id = insert(TABLE_NAME, cv);
        if(new_id > 0) {
            ropn.setId(new_id);
        } else {
            ropn = null;
        }
    }

    public boolean atualizarResposta(RespostaOpinativa ropn) {
        ContentValues cv = getContentValues(ropn);
        return update(TABLE_NAME, cv, ID+"="+String.valueOf(ropn.getId()), null);
    }

    private ContentValues getContentValues(RespostaOpinativa ropn){
        ContentValues cv = new ContentValues();
        if(ropn.getId() > 0){
            cv.put(ID, ropn.getId());
        }
        if(ropn.getPublicacao() != null && ropn.getPublicacao().getId() > 0){
            cv.put(ID_PUBLICACAO, ropn.getPublicacao().getId());
        } else {
            cv.putNull(ID_PUBLICACAO);
        }
        cv.put(ID_QUESTAO, ropn.obterQuestaoRespostas().getId());
        cv.put(DATA_CRIACAO, ropn.getData_de_resposta());
        cv.put(OPINIAO, ropn.getOpiniao());
        return cv;
    }
}
