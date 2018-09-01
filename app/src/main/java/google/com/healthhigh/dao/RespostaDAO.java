package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import google.com.healthhigh.domain.Resposta;

/**
 * Created by Alan on 22/10/2017.
 */

public class RespostaDAO extends DAO {
    public static final String TABLE_NAME = "phh_resposta";
    public static final String ID = "id_resposta";
    public static final String ID_QUESTIONARIO = "i_id_questionario";
    public static final String ID_QUESTAO = "i_id_questao";
    public static final String RESPOSTA_QUANTITATIVA = "i_resposta_quantitativa";
    public static final String RESPOSTA_QUALITATIVA = "i_resposta_qualitativa";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY NOT NULL," +
                ID_QUESTAO + " INTEGER NOT NULL, " +
                ID_QUESTIONARIO + " INTEGER NOT NULL, " +
                RESPOSTA_QUANTITATIVA + " INTEGER NOT NULL DEFAULT 1," +
                RESPOSTA_QUALITATIVA + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + ID_QUESTAO + ") REFERENCES " + QuestaoAlternativaDAO.TABLE_NAME + "(" + QuestaoAlternativaDAO.ID + ")" +
                "FOREIGN KEY(" + ID_QUESTIONARIO + ") REFERENCES " + QuestionarioDAO.TABLE_NAME + "(" + QuestionarioDAO.ID + ")" +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public RespostaDAO(Context context) {
        super(context);
    }

    public static Resposta getResposta(Cursor c){
        Resposta r = new Resposta();
        r.setId(c.getLong(c.getColumnIndex(ID)));
        r.setQuantitativo(c.getShort(c.getColumnIndex(RESPOSTA_QUANTITATIVA)));
        r.setQualitativo(c.getString(c.getColumnIndex(RESPOSTA_QUALITATIVA)));
        return r;
    }

    public void insertResposta(Resposta r){
        insert(TABLE_NAME, createInstert(r));
    }

    private ContentValues createInstert(Resposta r){
        ContentValues cv = new ContentValues();
        cv.put(ID_QUESTAO, r.getQuestao().getId());
        cv.put(ID_QUESTIONARIO, r.getQuestionario().getId());
        cv.put(RESPOSTA_QUALITATIVA, r.getQualitativo());
        cv.put(RESPOSTA_QUANTITATIVA, r.getQuantitativo());
        return cv;
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
