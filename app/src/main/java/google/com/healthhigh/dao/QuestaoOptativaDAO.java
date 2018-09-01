package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;

import google.com.healthhigh.domain.QuestaoOptativa;

/**
 * Created by Alan on 18/04/2018.
 */

public class QuestaoOptativaDAO extends DAO {

    public static final String TABLE_NAME = "phh_questao_optativa";
    public static final String ID = "id_questao_optativa";
    public static final String DESCRICAO = "s_descricao_questao_optativa";
    public static final String DATA_CRIACAO = "i_data_criacao_questao_optativa";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                    ID + " INTEGER PRIMARY KEY NOT NULL, " +
                    DESCRICAO + " INTEGER NOT NULL," +
                    DATA_CRIACAO + " INTEGER NOT NULL " +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public QuestaoOptativaDAO(Context context) {
        super(context);
    }

    public void inserirQuestaoOptativa(QuestaoOptativa qo){
        ContentValues cv = getContentValues(qo);
        long new_id = insert(TABLE_NAME, cv);
        if(new_id > 0){
            qo.setId(new_id);
        } else {
            qo = null;
        }
    }

    private ContentValues getContentValues(QuestaoOptativa qo) {
        ContentValues cv = new ContentValues();
        cv.put(DESCRICAO, qo.getDescricao());
        cv.put(DATA_CRIACAO, qo.getData_criacao());
        return cv;
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
