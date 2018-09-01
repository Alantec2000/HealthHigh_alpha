package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;

import google.com.healthhigh.domain.QuestaoOptativa;
import google.com.healthhigh.domain.RespostaOptativa;

/**
 * Created by Alan on 18/04/2018.
 */

public class RespostaOptativaDAO extends DAO{
    public final static String TABLE_NAME = "phh_resposta_optativa";
    public final static String ID = "id_resposta_optativa";
    public final static String ID_QUESTAO = "id_questao_optativa_resposta_optativa";
    public final static String OPCAO = "i_opcao_resposta_optativa";
    public final static String ID_PUBLICACAO = "id_publicacao_resposta_optativa";
    public final static String DATA_CRIACAO = "i_data_criacao_resposta_optativa";

    public RespostaOptativaDAO(Context context) {
        super(context);
    }

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                OPCAO + " INTEGER NOT NULL, " +
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

    @Override
    protected void prepareContentReceiver() {

    }

    public void inserirResposta(RespostaOptativa ropt) {
        ContentValues cv = getContentValues(ropt);
        long new_id = insert(TABLE_NAME, cv);;
        if(new_id > 0) {
            ropt.setId(new_id);
        } else {
            ropt = null;
        }
    }

    public boolean atualizarResposta(RespostaOptativa ropt) {
        ContentValues cv = getContentValues(ropt);
        return update(TABLE_NAME, cv, ID+"="+String.valueOf(ropt.getId()), null);
    }

    private ContentValues getContentValues(RespostaOptativa ropt){
        ContentValues cv = new ContentValues();
        if(ropt.getId() > 0){
            cv.put(ID, ropt.getId());
        }
        cv.put(OPCAO, ropt.getOpcao());
        cv.put(ID_QUESTAO, ropt.getQuestao().getId());
        if(ropt.getPublicacao() != null){
            cv.put(ID_PUBLICACAO, ropt.getPublicacao().getId());
        } else {
            cv.putNull(ID_PUBLICACAO);
        }
        cv.put(DATA_CRIACAO, ropt.getData_de_resposta());
        return cv;
    }
}
