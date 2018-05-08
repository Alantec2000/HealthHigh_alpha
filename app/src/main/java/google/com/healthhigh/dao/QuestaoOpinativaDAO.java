package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;

import google.com.healthhigh.domain.QuestaoOpinativa;

/**
 * Created by Alan on 19/04/2018.
 */

public class QuestaoOpinativaDAO extends DAO {
    public final static String TABLE_NAME = "phh_questao_opinativa";
    public final static String ID = "id_questao_opinativa";
    public final static String DESCRICAO = "s_descricao_questao_opinativa";
    public final static String DATA_CRIACAO = "i_data_criacao_questao_opinativa";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                DESCRICAO + " TEXT NOT NULL, " +
                DATA_CRIACAO + " INTEGER NOT NULL " +
            ");";
    }
    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
    public QuestaoOpinativaDAO(Context context) {
        super(context);
    }

    public void insereQuestaoOpinativa(QuestaoOpinativa qo){
        ContentValues cv = getContentValues(qo);
        long new_id = write_db.insert(TABLE_NAME, null, cv);
        if(new_id > 0){
            qo.setId(new_id);
        } else {
            qo = null;
        }
    }

    private ContentValues getContentValues(QuestaoOpinativa qo) {
        ContentValues cv = new ContentValues();
        cv.put(DESCRICAO, qo.getDescricao());
        cv.put(DATA_CRIACAO, qo.getData_criacao());
        return cv;
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
