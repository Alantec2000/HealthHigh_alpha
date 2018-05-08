package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import google.com.healthhigh.domain.QuestaoAlternativa;
import google.com.healthhigh.domain.Questionario;

/**
 * Created by Alan on 22/04/2018.
 */

public class QuestionarioQuestaoAlternativaDAO extends DAO {
    public static final String TABLE_NAME = "phh_questionario_questao_alternativa";
    public static final String ID = "id_questionario_questao_alternativa";
    public static final String ID_QUESTIONARIO = "id_questionario_questionario_questao_alternativa";
    public static final String ID_QUESTAO = "id_questao_questionario_questao_alternativa";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                ID_QUESTAO + " INTEGER NOT NULL, " +
                ID_QUESTIONARIO + " INTEGER NOT NULL, " +
                "UNIQUE(" + ID_QUESTAO + "," + ID_QUESTIONARIO + ") ON CONFLICT IGNORE, " +
                "FOREIGN KEY(" + ID_QUESTAO + ") REFERENCES " + QuestaoAlternativaDAO.TABLE_NAME + "(" + QuestaoAlternativaDAO.ID + ")" +
                "FOREIGN KEY(" + ID_QUESTIONARIO + ") REFERENCES " + QuestionarioDAO.TABLE_NAME + "(" + QuestionarioDAO.ID + ")" +
                ");";
    }

    public QuestionarioQuestaoAlternativaDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public void insereAssociacaoQuestaoAlternativaQuestionario(QuestaoAlternativa qa){
        write_db.beginTransaction();
        try {
            ContentValues cv = getContentValues(qa);
            long new_id = write_db.insertOrThrow(TABLE_NAME, null, cv);
            write_db.setTransactionSuccessful();
        } catch (SQLiteException e){
            Log.e(SQLITE_ERROR, e.getMessage());
        } finally {
            write_db.endTransaction();
        }
    }

    private ContentValues getContentValues(QuestaoAlternativa qa) {
        ContentValues cv = new ContentValues();
        cv.put(ID_QUESTAO, qa.getId());
        cv.put(ID_QUESTIONARIO, qa.getQuestionario().getId());
        return cv;
    }
}
