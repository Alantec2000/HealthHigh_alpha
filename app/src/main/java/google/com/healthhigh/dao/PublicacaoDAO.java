package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.InteracaoAtividade;
import google.com.healthhigh.domain.Publicacao;
import google.com.healthhigh.utils.DataHelper;

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

    public Publicacao get(Desafio d) {
        Publicacao p = null;
        if(d != null){
            String select = "SELECT * FROM " + TABLE_NAME + " as p " +
                    "INNER JOIN " + DesafioDAO.TABLE_NAME + " as d ON " +
                    "d." + DesafioDAO.ID + " = p." + PublicacaoDAO.ID_DESAFIO + " " +
                    "WHERE " + DesafioDAO.ID + " = " + String.valueOf(d.getId()) + " AND " +
                    String.valueOf(DataHelper.now()) + " BETWEEN " + DATA_INICIO + " AND " + DATA_FIM;
            select += ";";
            PublicacaoBehavior p_b = new PublicacaoBehavior() {
                @Override
                public void setContent(Cursor c) {
                    publicacao = get(c);
                }
            };
            getSelectQueryContent(select, p_b);
            p = p_b.publicacao;
        }
        return p;
    }

    private abstract static class PublicacaoBehavior implements Behavior {
        protected Publicacao publicacao;
        protected Map<Long, Publicacao> publicacoes;

        public PublicacaoBehavior() {
            publicacao = null;
            publicacoes = new TreeMap<>();
        }
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

    public static Publicacao get(Cursor c) {
        Publicacao p = new Publicacao();
        p.setId(getLong(c, ID));
        p.setData_criacao(getLong(c, DATA_CRIACAO));
        p.setData_inicio(getLong(c, DATA_INICIO));
        p.setData_fim(getLong(c, DATA_FIM));
        return p;
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public Publicacao get(InteracaoAtividade i_a) {
        String select = "SELECT * FROM " + TABLE_NAME + " as p " +
                "INNER JOIN " + InteracaoAtividadeDAO.TABLE_NAME + " as ia  ON " +
                "ia." + InteracaoAtividadeDAO.ID_PUBLICACAO + " = p." + PublicacaoDAO.ID + " " +
                "WHERE p." + InteracaoAtividadeDAO.ID + " = " + String.valueOf(i_a);

        PublicacaoBehavior p_b = new PublicacaoBehavior() {
            @Override
            public void setContent(Cursor c) {
                publicacao = get(c);
            }
        };
        getSelectQueryContent(select, p_b);
        return p_b.publicacao;
    }
}
