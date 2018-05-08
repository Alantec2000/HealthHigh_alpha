package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;

import google.com.healthhigh.domain.Alternativa;
import google.com.healthhigh.domain.QuestaoAlternativa;

/**
 * Created by Alan on 19/04/2018.
 */

public class AlternativaDAO extends DAO {
    RespostaAlternativaDAO ra_dao;
    public static final String TABLE_NAME = "phh_alternativa";
    public static final String ID = "id_alternativa";
    public static final String ID_QUESTAO = "id_questao_alternativa";
    public static final String DESCRICAO = "s_descricao_alternativa";
    public static final String DATA_CRIACAO = "i_data_criacao_alternativa";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                ID_QUESTAO + " INTEGER NOT NULL, " +
                DATA_CRIACAO + " INTEGER NOT NULL, " +
                DESCRICAO + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + ID_QUESTAO + ") REFERENCES " + QuestaoAlternativaDAO.TABLE_NAME + "("+QuestaoAlternativaDAO.ID+")" +
                ")";
    }
    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
    public AlternativaDAO(Context context) {
        super(context);
        ra_dao = new RespostaAlternativaDAO(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public void inserirAlternativa(Alternativa a) {
        ContentValues cv = getContentValues(a);
        long new_id = write_db.insert(TABLE_NAME, null, cv);
        if(new_id > 0){
            a.setId(new_id);
        } else {
            a = null;
        }
    }

    private ContentValues getContentValues(Alternativa a) {
        ContentValues cv = new ContentValues();
        cv.put(AlternativaDAO.ID_QUESTAO, a.getQuestaoAlternativa().getId());
        cv.put(AlternativaDAO.DESCRICAO, a.getDescricao());
        cv.put(AlternativaDAO.DATA_CRIACAO, a.getData_criacao());
        return cv;
    }
}
