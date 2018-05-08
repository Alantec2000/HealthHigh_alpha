package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.Publicacao;

/**
 * Created by Alan on 19/04/2018.
 */

public class PublicacaoDAO extends DAO {
    public final static String TABLE_NAME = "phh_publicacao";
    public final static String ID = "id_publicacao";
    public final static String ID_DESAFIO = "id_desafio_publicacao";
    public final static String DATA_INICIO = "i_data_inicio_publicacao";
    public final static String DATA_FIM = "i_data_fim_publicacao";
    public final static String DATA_CRIACAO = "i_data_criacao_publicacao";
    private Context context;
    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                ID_DESAFIO + " INTEGER NOT NULL, " +
                DATA_INICIO + " INTEGER NOT NULL, " +
                DATA_FIM + " INTEGER NOT NULL, " +
                DATA_CRIACAO + " INTEGER NOT NULL, " +
                "UNIQUE(" + ID + "," + ID_DESAFIO + ") ON CONFLICT IGNORE, " +
                "FOREIGN KEY(" + ID_DESAFIO + ") REFERENCES " + DesafioDAO.TABLE_NAME + "(" + DesafioDAO.ID + ")" +
                ");";
    }
    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
    public PublicacaoDAO(Context context) {
        super(context);
    }

    public Publicacao inserePublicacao(Publicacao p){
        DesafioDAO d_dao = new DesafioDAO(context);

        //Verifica se o desafio existe antes de inserir a publicacao
        if(p.getDesafio() != null){
            Desafio d_aux = d_dao.getDesafio(p.getDesafio().getId());

            if(d_aux != null && d_aux.getId() == p.getDesafio().getId()){
                ContentValues cv = setContentValues(p);
                long new_id = write_db.insert(TABLE_NAME, null, cv);
                if(new_id > 0){
                    p.setId(new_id);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
        return p;
    }

    private ContentValues setContentValues(Publicacao p){
        ContentValues cv = new ContentValues();
        cv.put(ID_DESAFIO, p.getDesafio().getId());
        cv.put(DATA_INICIO, p.getData_inicio());
        cv.put(DATA_FIM, p.getData_fim());
        cv.put(DATA_CRIACAO, p.getData_criacao());
        return cv;
    }

    public static Publicacao getPublicacao(Cursor c) {
        Publicacao p = new Publicacao();
        p.setId(c.getLong(c.getColumnIndex(PublicacaoDAO.ID)));
        p.setData_criacao(c.getLong(c.getColumnIndex(PublicacaoDAO.DATA_CRIACAO)));
        p.setData_inicio(c.getLong(c.getColumnIndex(PublicacaoDAO.DATA_INICIO)));
        p.setData_fim(c.getLong(c.getColumnIndex(PublicacaoDAO.DATA_FIM)));
        return p;
    }

    @Override
    protected void prepareContentReceiver() {

    }
}
