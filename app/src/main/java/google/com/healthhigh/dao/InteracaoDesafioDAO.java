package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import google.com.healthhigh.domain.InteracaoDesafio;

/**
 * Created by Alan on 25/04/2018.
 * Armazena data de inicio e fim ao aceitar um desafio
 * Em dada publicacao
 * Verifica se é uma nova publicacao pela data de visualização
 */

public class InteracaoDesafioDAO extends DAO {
    public static final String
            TABLE_NAME = "phh_interacao_desafio",
            ID = "id_interacao_desafio",
            ID_DESAFIO = "id_desafio_interacao_desafio",
            ID_PUBLICACAO = "id_publicacao_interacao_desafio",
            DATA_CRIACAO = "i_data_criacao_interacao_desafio",
            DATA_CONCLUSAO = "i_data_conclusao_interacao_desafio",
            DATA_CANCELADO = "i_data_cancelado_interacao_desafio",
            DATA_VISUALIZACAO = "i_data_visualizacao_interacao_desafio",
            DATA_ACEITO = "i_data_aceito",
            REALIZANDO = "i_realizando_desafio";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                ID_DESAFIO + " INTEGER NOT NULL, " +
                ID_PUBLICACAO + " INTEGER NOT NULL, " +
                REALIZANDO + " INTEGER NOT NULL DEFAULT 1, " +
                DATA_CRIACAO + " INTEGER NOT NULL," +
                DATA_VISUALIZACAO + " INTEGER, " +
                DATA_ACEITO + " INTEGER, " +
                DATA_CANCELADO + " INTEGER, " +
                DATA_CONCLUSAO + " INTEGER, " +
                "UNIQUE(" + ID_DESAFIO + ", " + ID_PUBLICACAO + "), " +
                "FOREIGN KEY(" + ID_DESAFIO + ") REFERENCES " + DesafioDAO.TABLE_NAME + "(" + DesafioDAO.ID + ")," +
                "FOREIGN KEY(" + ID_PUBLICACAO + ") REFERENCES " + PublicacaoDAO.TABLE_NAME + "(" + PublicacaoDAO.ID + ")" +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public InteracaoDesafioDAO(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public static InteracaoDesafio getInteracaoDesafio(Cursor c) {
        InteracaoDesafio i_d = new InteracaoDesafio();
        i_d.setData_aceito(c.getLong(c.getColumnIndex(InteracaoDesafioDAO.DATA_VISUALIZACAO)));
        i_d.setData_aceito(c.getLong(c.getColumnIndex(InteracaoDesafioDAO.DATA_ACEITO)));
        i_d.setDataConclusao(c.getLong(c.getColumnIndex(InteracaoDesafioDAO.DATA_CONCLUSAO)));
        i_d.setData_cancelamento(c.getLong(c.getColumnIndex(InteracaoDesafioDAO.DATA_CANCELADO)));
        i_d.setRealizando_no_momento(c.getShort(c.getColumnIndex(InteracaoDesafioDAO.REALIZANDO)) > 0);
        i_d.setId(c.getLong(c.getColumnIndex(InteracaoDesafioDAO.ID)));
        return i_d;
    }

    public void insertNovaInteracao(InteracaoDesafio i_d, ContentValues cv) {
        long new_id = write_db.insert(TABLE_NAME, null, cv);
        if(new_id > 0){
            i_d.setId(new_id);
        } else {
            i_d = null;
        }
    }

    public boolean atualizarInteracaoDesafio(InteracaoDesafio i_d) {
        boolean result = false;
        try {
            ContentValues cv = getContentValues(i_d);
            result = update(TABLE_NAME, cv, ID + " = ?", new String[]{String.valueOf(i_d.getId())});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private ContentValues getContentValues(InteracaoDesafio i_d) throws Exception {
        ContentValues cv = new ContentValues();
        if(i_d.getIdDesafio() == 0) throw new Exception("Interacao sem desafio associado");
        cv.put(ID_DESAFIO, i_d.getIdDesafio());
        if(i_d.getIdPublicacao() == 0) throw new Exception("Interacao sem publicação associada");
        cv.put(ID_PUBLICACAO, i_d.getIdPublicacao());
        cv.put(DATA_ACEITO, i_d.getData_aceito());
        cv.put(DATA_CANCELADO, i_d.getData_cancelamento());
        cv.put(DATA_VISUALIZACAO, i_d.getData_visualizacao());
        cv.put(DATA_CRIACAO, i_d.getData_criacao());
        cv.put(REALIZANDO, i_d.estaRealizando());
        return cv;
    }
}
