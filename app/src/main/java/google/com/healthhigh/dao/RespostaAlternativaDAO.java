package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import google.com.healthhigh.domain.Publicacao;
import google.com.healthhigh.domain.RespostaAlternativa;

/**
 * Created by Alan on 19/04/2018.
 */

public class RespostaAlternativaDAO extends DAO {

    public final static String TABLE_NAME = "phh_resposta_alternativa";
    public final static String ID = "id_resposta_alternativa";
    public final static String ID_ALTERNATIVA = "id_alternativa_resposta_alternativa";
    public final static String ID_PUBLICACAO = "id_publicacao_resposta_alternativa";
    public final static String DATA_CRIACAO = "i_data_criacao_resposta_alternativa";
    public final static String SELECIONADO = "i_selecionado_resposta_alternativa";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY NOT NULL, " +
                    ID_ALTERNATIVA + " INTEGER NOT NULL, " +
                    ID_PUBLICACAO + " INTEGER, " +
                    DATA_CRIACAO + " INTEGER NOT NULL, " +
                    SELECIONADO + " INTEGER NOT NULL, " +
                    "UNIQUE(" + ID + "," + ID_PUBLICACAO + "," + ID_ALTERNATIVA + ") ON CONFLICT IGNORE, " +
                    "FOREIGN KEY(" + ID_ALTERNATIVA + ") REFERENCES " + AlternativaDAO.TABLE_NAME + "(" + AlternativaDAO.ID + "), " +
                    "FOREIGN KEY(" + ID_PUBLICACAO + ") REFERENCES " + PublicacaoDAO.TABLE_NAME + "(" + AlternativaDAO.ID + ") " +
                ");";
    }
    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
    public RespostaAlternativaDAO(Context context) {
        super(context);
    }

    public void inserirRespostaAlternativa(RespostaAlternativa ra){
        ContentValues cv = getContentValues(ra);
        long new_id = 0;
        try{
            new_id = write_db.insertOrThrow(TABLE_NAME, null, cv);
        }catch (SQLiteException e) {
            imprimeErroSQLite(e);
        } finally {
            if(new_id > 0) {
                ra.setId(new_id);
            } else {
                ra = null;
            }
        }
    }
    public boolean atualizarRespostaAlternativa(RespostaAlternativa ra){
        ContentValues cv = getContentValues(ra);
        int rows_updated = 0;
        try{
            rows_updated = write_db.update(TABLE_NAME, cv, ID+"="+String.valueOf(ra.getId()), null);
        }catch (SQLiteException e) {
            imprimeErroSQLite(e);
        }

        return rows_updated > 0;
    }

    private ContentValues getContentValues(RespostaAlternativa ra) {
        ContentValues cv = new ContentValues();
        if(ra.getId() > 0){
            cv.put(ID, ra.getId());
        }
        cv.put(ID_ALTERNATIVA, ra.getAlternativa().getId_alternativa());
        if(ra.getPublicacao() != null && ra.getPublicacao().getId() > 0){
            cv.put(ID_PUBLICACAO, ra.getPublicacao().getId());
        } else {
            cv.putNull(ID_PUBLICACAO);
        }
        cv.put(DATA_CRIACAO, ra.getData_de_resposta());
        cv.put(SELECIONADO, ra.isSelecionada());
        return cv;
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
