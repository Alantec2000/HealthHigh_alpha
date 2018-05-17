package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import google.com.healthhigh.domain.InteracaoQuestionario;

/**
 * Created by Alan on 25/04/2018.
 */

public class InteracaoQuestionarioDAO extends DAO {
    public final static String
            TABLE_NAME = "phh_interacao_questionario", TN_A = "iq",
            ID = "id_interacao_questionario",
            ID_QUESTIONARIO = "id_questionario_interacao_questionario",
            ID_PUBLICACAO = "id_publicacao_interacao_questionario",
            DATA_VISUALIZACAO = "i_data_visualizacao_interacao_questionario",
            DATA_INICIO = "i_data_inicio_interacao_questionario",
            DATA_TERMINO = "i_data_termino_interacao_questionario",
            DATA_CRIACAO = "i_data_criacao_interacao_questionario";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                ID_QUESTIONARIO + " INTEGER NOT NULL, " +
                ID_PUBLICACAO + " INTEGER, " +
                DATA_VISUALIZACAO + " INTEGER, " +
                DATA_INICIO + " INTEGER, " +
                DATA_TERMINO + " INTEGER," +
                DATA_CRIACAO + " INTEGER NOT NULL, " +
                "UNIQUE(" + ID_QUESTIONARIO + ", " + ID_PUBLICACAO + "), " +
                "FOREIGN KEY(" + ID_QUESTIONARIO + ") REFERENCES " + QuestionarioDAO.TABLE_NAME + "(" + QuestionarioDAO.ID + ")," +
                "FOREIGN KEY(" + ID_PUBLICACAO + ") REFERENCES " + PublicacaoDAO.TABLE_NAME + "(" + PublicacaoDAO.ID + ")" +
            ");";
    }

    public static String getDropTableString() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public InteracaoQuestionarioDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public static InteracaoQuestionario getInteracaoQuestionario(Cursor c) {
        InteracaoQuestionario i_q = new InteracaoQuestionario();
        i_q.setData_criacao(c.getLong(c.getColumnIndex(InteracaoQuestionarioDAO.DATA_CRIACAO)));
        i_q.setData_inicio(c.getLong(c.getColumnIndex(InteracaoQuestionarioDAO.DATA_INICIO)));
        i_q.setData_termino(c.getLong(c.getColumnIndex(InteracaoQuestionarioDAO.DATA_TERMINO)));
        i_q.setData_visualizacao(c.getLong(c.getColumnIndex(InteracaoQuestionarioDAO.DATA_VISUALIZACAO)));
        i_q.setId(c.getLong(c.getColumnIndex(InteracaoQuestionarioDAO.ID)));
        return i_q;
    }

    public void insereInteracaoQuestionario(ContentValues cv, InteracaoQuestionario i_q) {
        long new_id = 0;
        try {
            new_id = write_db.insertOrThrow(TABLE_NAME, null, cv);
        } catch (SQLiteException e){
            imprimeErroSQLite(e);
        }
        if(new_id > 0){
            i_q.setId(new_id);
        } else {
            i_q = null;
        }
    }

    public boolean atualizaQuestionario(InteracaoQuestionario i_q) {
        ContentValues cv = getContentValues(i_q);
        int rows = 0;
        try {
            rows = write_db.update(TABLE_NAME, cv, ID + "=?",new String[]{String.valueOf(i_q.getId())});
        } catch (SQLiteException e){
            imprimeErroSQLite(e);
        }

        return rows > 0;

    }

    private ContentValues getContentValues(InteracaoQuestionario i_q) {
        ContentValues cv = new ContentValues();
        cv.put(ID_QUESTIONARIO, i_q.getQuestionario().getId());
        cv.put(ID_PUBLICACAO, i_q.getPublicacao().getId());
        cv.put(DATA_CRIACAO, i_q.getData_criacao());
        cv.put(DATA_INICIO, i_q.getData_inicio());
        cv.put(DATA_TERMINO, i_q.getData_termino());
        cv.put(DATA_VISUALIZACAO, i_q.getData_visualizacao());
        return cv;
    }
}
