package google.com.healthhigh.dao;

import android.content.Context;

/**
 * Created by Alan on 25/04/2018.
 */

public class DesafioQuestionarioDAO extends DAO {
    public static final String
            TABLE_NAME = "phh_desafio_questionario",
            ID = "id_desafio_questionario",
            ID_DESAFIO = "id_desafio_desafio_questionario",
            ID_QUESTIONARIO = "id_questionario_desafio_questionario",
            DATA_CRIACAO = "i_data_criacao_desafio_questionario";
    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                ID_DESAFIO + " INTEGER NOT NULL, " +
                ID_QUESTIONARIO + " INTEGER NOT NULL, " +
                DATA_CRIACAO + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + ID_QUESTIONARIO + ") REFERENCES " + QuestionarioDAO.TABLE_NAME + "("+ QuestionarioDAO.ID + ")," +
                "FOREIGN KEY(" + ID_DESAFIO + ") REFERENCES " + DesafioDAO.TABLE_NAME + "(" + DesafioDAO.ID + ")" +
                ")";
    }



    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public DesafioQuestionarioDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
