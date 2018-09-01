package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import google.com.healthhigh.domain.Premiacao;

public class PremiacaoDAO extends DAO {
    public static final String
            TABLE_NAME = "hhwm_premiacao",
            ID = "id_premiacao",
            DATA_CRIACAO = "i_data_criacao_premiacao",
            DATA_VISUALIZACAO = "i_data_visualizacao",
            NOME = "s_nome_premiacao",
            PONTUACAO = "i_experiencia_premiacao";

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER NOT NULL PRIMARY KEY, " +
                DATA_CRIACAO + " INTEGER NOT NULL, " +
                DATA_VISUALIZACAO + " INTEGER NOT NULL DEFAULT 0, " +
                NOME + " TEXT NOT NULL, " +
                PONTUACAO + " INTEGER NOT NULL " +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public PremiacaoDAO(Context context) {
        super(context);
    }

    public int inserirPremiacoes(List<Premiacao> premiacoes) {
        int result = 0;
        for(Premiacao premiacao : premiacoes){
            if(inserir(premiacao)) result++;
        }
        return result;
    }

    public boolean inserir(Premiacao p) {
        synchronized (this){
            ContentValues cv = getContentValues(p);
            long id = insert(TABLE_NAME, cv);
            p.setId(id);
            return id > 0;
        }
    }

    private ContentValues getContentValues(Premiacao p) {
        ContentValues cv = new ContentValues();
        if(p.getId() > 0){
            cv.put(ID, p.getId());
        }
        cv.put(NOME, p.getNome());
        cv.put(PONTUACAO, p.getExperiencia());
        cv.put(DATA_CRIACAO, p.getData_criacao());
        cv.put(DATA_VISUALIZACAO, p.getData_visualizacao());
        return cv;
    }

    public Map<Long, Premiacao> getPremiacoes() {
        String select = "Select * from " + TABLE_NAME +";";
        PremiacaoBehavior p_b = new PremiacaoBehavior() {
            @Override
            public void setContent(Cursor c) {
                long id = getLong(c, ID);
                if(!premiacoes.containsKey(id)){
                    premiacoes.put(id, getPremiacao(c));
                }
            }
        };
        getSelectQueryContent(select, p_b);
        return p_b.premiacoes;
    }

    private Premiacao getPremiacao(Cursor c) {
        Premiacao p = new Premiacao();
        p.setId(getLong(c, ID));
        p.setData_criacao(getLong(c, DATA_CRIACAO));
        p.setData_visualizacao(getLong(c, DATA_VISUALIZACAO));
        p.setExperiencia(getInt(c, PONTUACAO));
        p.setNome(getString(c, NOME));
        return p;
    }

    public Premiacao getPremiacaoId(long id) {
        if(id > 0){
            String select = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = " + String.valueOf(id);
            PremiacaoBehavior p_b = new PremiacaoBehavior() {
                @Override
                public void setContent(Cursor c) {
                    if(getLong(c, ID) > 0){
                        premiacao = getPremiacao(c);
                    }
                }
            };
            getSelectQueryContent(select, p_b);
            return (p_b.premiacao.getId() > 0 ? p_b.premiacao : null);
        } else {
            return null;
        }
    }

    private abstract class PremiacaoBehavior implements Behavior {
        protected Map<Long,Premiacao> premiacoes;
        protected Premiacao premiacao;

        public PremiacaoBehavior() {
            premiacoes = new TreeMap<>();
            premiacao = new Premiacao();
        }
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public Set<Long> getIdPremiacoes() {
        final Set<Long> premiacoes_set = new HashSet<>();
        String select = "SELECT " + ID + " FROM " + TABLE_NAME;
        PremiacaoBehavior p_b = new PremiacaoBehavior() {
            @Override
            public void setContent(Cursor c) {
                long id = getLong(c, ID);
                premiacoes_set.add(id);
            }
        };
        getSelectQueryContent(select, p_b);
        return premiacoes_set;
    }
}
