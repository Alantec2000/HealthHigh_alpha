package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import google.com.healthhigh.domain.QuestaoOpinativa;

/**
 * Created by Alan on 19/04/2018.
 */

public class QuestionarioQuestaoOpinativaDAO extends DAO {
    public final static String TABLE_NAME = "phh_questionario_questao_opinativa";
    public final static String ID = "id_questionario_questao_opinativa";
    public final static String ID_QUESTAO = "id_questao_opinativa_questionario_questao_opinativa";
    public final static String ID_QUESTIONARIO = "id_questionario_questionario_questao_opinativa";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                ID_QUESTAO + " INTEGER NOT NULL, " +
                ID_QUESTIONARIO + " INTEGER NOT NULL, " +
                "UNIQUE(" + ID_QUESTAO + "," + ID_QUESTIONARIO + ") ON CONFLICT IGNORE, " +
                "FOREIGN KEY(" + ID_QUESTAO + ") REFERENCES " + QuestaoOpinativaDAO.TABLE_NAME + "(" + QuestaoOpinativaDAO.ID + ")" +
                "FOREIGN KEY(" + ID_QUESTIONARIO + ") REFERENCES " + QuestionarioDAO.TABLE_NAME + "(" + QuestionarioDAO.ID + ")" +
                ");";
    }
    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
    public QuestionarioQuestaoOpinativaDAO(Context context) {
        super(context);
    }

    public void inserirQuestionarioQuestaoOpinativa(QuestaoOpinativa qo){
        ContentValues cv = getContenValues(qo);
        long id = insert(TABLE_NAME, cv);
    }

    private ContentValues getContenValues(QuestaoOpinativa qo) {
        ContentValues cv = new ContentValues();
        cv.put(ID_QUESTAO, qo.getId());
        cv.put(ID_QUESTIONARIO, qo.getQuestionario().getId());
        return cv;
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
