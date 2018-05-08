package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.List;
import java.util.Map;

import google.com.healthhigh.domain.Alternativa;
import google.com.healthhigh.domain.Questao;
import google.com.healthhigh.domain.QuestaoAlternativa;
import google.com.healthhigh.domain.Questionario;
import google.com.healthhigh.domain.TipoQuestao;

/**
 * Created by Alan on 22/10/2017.
 */

public class QuestaoAlternativaDAO extends DAO {
    public static final String TABLE_NAME = "phh_questao_alternativa";
    public static final String ID = "id_questao_alternativa";
    public static final String DESCRICAO = "s_descricao_questao_alternativa";
    public static final String DATA_CRIACAO = "i_data_criacao_questao_alternativa";
    public QuestaoAlternativaDAO(Context context) {
        super(context);
    }

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                DESCRICAO + " TEXT NOT NULL, " +
                DATA_CRIACAO + " INTEGER NOT NULL " +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static TipoQuestao getQuestao(Cursor c){
        QuestaoAlternativa q = new QuestaoAlternativa();
        q.setId(c.getLong(c.getColumnIndex(ID)));
        q.setDescricao(c.getString(c.getColumnIndex(DESCRICAO)));
        return q;
    };

    public void insereQuestaoAlternativa(QuestaoAlternativa qa){
        long new_id = 0;
        ContentValues cv = createInstertQuestionario(qa);
        try {
            new_id = write_db.insertOrThrow(TABLE_NAME, null, cv);
        } catch (SQLiteException e){
            Log.e(SQLITE_ERROR, e.getMessage());
        }
        if(new_id > 0){
            qa.setId(new_id);
        } else {
            qa = null;
        }
    }

    private ContentValues createInstertQuestionario(QuestaoAlternativa qa){
        ContentValues cv = new ContentValues();
        cv.put(DESCRICAO, qa.getDescricao());
        cv.put(DATA_CRIACAO, qa.getData_criacao());
        return cv;
    }


    @Override
    protected void prepareContentReceiver() {

    }

    public static QuestaoAlternativa getQuestaoAlternativa(Cursor c_qa) {
        QuestaoAlternativa qa = new QuestaoAlternativa();
        qa.setId(c_qa.getLong(c_qa.getColumnIndex(QuestaoAlternativaDAO.ID)));
        qa.setDescricao(c_qa.getString(c_qa.getColumnIndex(QuestaoAlternativaDAO.DESCRICAO)));
        qa.setData_criacao(c_qa.getLong(c_qa.getColumnIndex(QuestaoAlternativaDAO.DATA_CRIACAO)));
        return qa;
    }

    public static QuestaoAlternativa getQuestaoAlternativa(Cursor c_qa, Questionario q) {
        QuestaoAlternativa qa = new QuestaoAlternativa();
        qa.setId(c_qa.getLong(c_qa.getColumnIndex(QuestaoAlternativaDAO.ID)));
        qa.setDescricao(c_qa.getString(c_qa.getColumnIndex(QuestaoAlternativaDAO.DESCRICAO)));
        qa.setData_criacao(c_qa.getLong(c_qa.getColumnIndex(QuestaoAlternativaDAO.DATA_CRIACAO)));
        qa.setQuestionario(q);
        return qa;
    }
}
