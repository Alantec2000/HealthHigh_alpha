package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.List;

import google.com.healthhigh.domain.Questao;
import google.com.healthhigh.domain.QuestaoOptativa;
import google.com.healthhigh.domain.Questionario;

/**
 * Created by Alan on 18/04/2018.
 */

public class Questionario_QuestaoOptativaDAO extends DAO{

    public Questionario questionario;
    public List<Questionario> lista_questionarios;
    public List<Questao> lista_questoes;
    public Questao questao;

    public static final String TABLE_NAME = "phh_questionario_questao_optativa";
    public static final String ID = "id_questionario_questao_optativa";
    public static final String ID_QUESTIONARIO = "i_id_questionario";
    public static final String ID_QUESTAO = "i_id_questao_optativa";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY NOT NULL," +
                ID_QUESTAO + " INTEGER NOT NULL," +
                ID_QUESTIONARIO + " INTEGER NOT NULL, " +
                "UNIQUE(" + ID_QUESTAO + "," + ID_QUESTIONARIO + ") ON CONFLICT IGNORE, " +
                "FOREIGN KEY(" + ID_QUESTAO + ") REFERENCES " + QuestaoOptativaDAO.TABLE_NAME + "(" + QuestaoAlternativaDAO.ID + ")" +
                "FOREIGN KEY(" + ID_QUESTIONARIO + ") REFERENCES " + QuestionarioDAO.TABLE_NAME + "(" + QuestionarioDAO.ID + ")" +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public Questionario_QuestaoOptativaDAO(Context context) {
        super(context);
    }

    public void inserirQuestionarioQuestaoOptativa(QuestaoOptativa qo){
        ContentValues cv = getContentValues(qo);
        long new_id = insert(TABLE_NAME, cv);
    }

    private ContentValues getContentValues(QuestaoOptativa qo) {
        ContentValues cv = new ContentValues();
        cv.put(ID_QUESTAO, qo.getId());
        cv.put(ID_QUESTIONARIO, qo.getQuestionario().getId());
        return cv;
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
