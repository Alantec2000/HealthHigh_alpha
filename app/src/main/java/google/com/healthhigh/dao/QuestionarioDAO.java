package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import google.com.healthhigh.domain.Meta;
import google.com.healthhigh.domain.Questionario;

/**
 * Created by Alan on 22/10/2017.
 */

public class QuestionarioDAO extends DAO {
    /* Content Receivers */
    private Questionario questionario;
    private List<Questionario> lista_questionarios;
    private List<Meta> lista_questionarios_meta;

    public static final String TABLE_NAME = "phh_questionario";
    public static final String ID = "id_questionario";
    public static final String ID_DESAFIO = "id_desafio_questionario";
    public static final String TITULO = "s_titulo_questionario";
    public static final String DATA_VISUALIZACAO = "i_data_visualizacao_questionario";
    public static final String DATA_CRIACAO = "i_data_criacao_questionario";
    public static final String DESCRICAO = "s_descricao_questionario";

    public QuestionarioDAO(Context context) {
        super(context);
    }

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY," +
//                ID_DESAFIO + " INTEGER NOT NULL" +
                DATA_VISUALIZACAO + " INTEGER, " +
                TITULO + " TEXT NOT NULL, " +
                DESCRICAO + " TEXT NOT NULL, " +
                DATA_CRIACAO + " INTEGER NOT NULL" +
//                "FOREIGN KEY(" + ID_DESAFIO + ") REFERENCES " + DesafioDAO.TABLE_NAME + "(" + DesafioDAO.ID + ")" +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public List<Questionario> getQuestionarios() {
        /* Obtém lista de questionários cadastrados */
        String sql = "SELECT * FROM " + TABLE_NAME + ";";

        getSelectQueryContent(sql, new Behavior() {
            @Override
            public void setContent(Cursor c) {
                lista_questionarios.add(getQuestionario(c));
            }
        });

        return lista_questionarios;
    }

    public List<Questionario> getQuestionarios(int limit) {
        /* Obtém lista de questionários cadastrados */
        String limit_statement = limit > 0 ? " LIMIT " + Integer.toString(limit) : "";
        String sql = "SELECT * FROM " + TABLE_NAME + limit_statement + ";";
        lista_questionarios = new ArrayList<>();
        getSelectQueryContent(sql, new Behavior() {
            @Override
            public void setContent(Cursor c) {
                lista_questionarios.add(getQuestionario(c));
            }
        });

        return lista_questionarios;
    }

    public static Questionario getQuestionario(Cursor c){
        Questionario q = new Questionario();
        q.setId(c.getLong(c.getColumnIndex(ID)));
        q.setTitulo(c.getString(c.getColumnIndex(TITULO)));
        q.setDescricao(c.getString(c.getColumnIndex(DESCRICAO)));
        q.setData_visualizacao(c.getLong(c.getColumnIndex(DATA_VISUALIZACAO)));
        return q;
    };

    public void insereQuestionario(Questionario q){
        long id = write_db.insert(TABLE_NAME, null, getContentValues(q));
        if(id > 0){
            q.setId(id);
        } else {
            q = null;
        }
    }

    private ContentValues getContentValues(Questionario q){
        ContentValues cv = new ContentValues();
        cv.put(TITULO, q.getTitulo());
        cv.put(DESCRICAO, q.getDescricao());
        cv.put(DATA_CRIACAO, q.getData_criacao());
        cv.put(DATA_VISUALIZACAO, q.getData_visualizacao());
        return cv;
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public boolean atualizaQuestionario(Questionario q) {
        ContentValues cv = getContentValues(q);
        int rows_updated = 0;
        try{
            rows_updated = write_db.update(TABLE_NAME, cv, ID + "=?", new String[]{String.valueOf(q.getId())});
        }catch (SQLiteException e) {
            imprimeErroSQLite(e);
        }

        return rows_updated > 0;
    }
}
