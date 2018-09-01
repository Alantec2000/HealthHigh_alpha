package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import google.com.healthhigh.db.CreateDB;
import google.com.healthhigh.domain.Atividade;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.utils.DataHelper;

public class DesafioDAO extends DAO {
    private static final String QUANTIDADE = "i_quantidade";
    private static final String DATA_CONCLUSAO = "s_data_conclusao";
    private static final String STATUS = "i_status"; // 1 - pendente, 2 - visualizado, 3 - em execução, 4 - finalizado, 5 - encerrado
    private Desafio desafio;
    public static String
            TABLE_NAME = "phh_desafio",
            ID = "id_desafio",
            PREMIACAO = "id_premiacao_desafio",
            TITULO = "s_titulo_desafio",
            DESCRICAO = "s_descricao_desafio",
            TENTATIVAS = "i_tentativas",
            TIPO = "i_tipo",
            ACEITO = "b_aceito",
            DATA_VISUALIZACAO = "i_data_visualizacao_desafio",
            DATA_CRIACAO = "s_data_criacao_desafio",
            DATA_ACEITO = "s_data_aceito_desafio";
    private List<Desafio> listaDesafios;
    private SQLiteDatabase write_db = null;

    public DesafioDAO(Context context) {
        super(context);
        CreateDB db = CreateDB.getDBInstance(context);
        write_db = db.getWritableDatabase();
    }

    public static String getCreateTableString() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                PREMIACAO + " INTEGER NOT NULL, " +
                TITULO + " TEXT NOT NULL, " +
                DESCRICAO + " TEXT NOT NULL, " +
                ACEITO + " INTEGER DEFAULT 0, " +
                QUANTIDADE + " INTEGER DEFAULT 1," +
                STATUS + " INTEGER DEFAULT 1," +
                TIPO + " INTEGER NOT NULL DEFAULT 1, " +
                DATA_VISUALIZACAO + " INTEGER DEFAULT '', " +
                DATA_ACEITO + " INTEGER DEFAULT '', " +
                DATA_CRIACAO + " INTEGER NOT NULL, " +
                DATA_CONCLUSAO + " INTEGER DEFAULT '', " +
                TENTATIVAS + " INTEGER DEFAULT 0," +
                "FOREIGN KEY(" + PREMIACAO + ") REFERENCES " + PremiacaoDAO.TABLE_NAME + "(" + PremiacaoDAO.ID + ")" +
                ");";
    }

    public static String getDropTableString() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static Desafio getDesafio(Cursor c){
        Desafio d = new Desafio();
        d.setId(getLong(c, ID));
        d.setDescricao(getString(c, DESCRICAO));
        d.setTentativas(getInt(c, TENTATIVAS));
        d.setTitulo(getString(c, TITULO));
        d.setQuantidade(getInt(c, QUANTIDADE));
        d.setStatus(getInt(c, STATUS));
        d.setTipo(getInt(c, TIPO));
        d.setData_aceito(getLong(c, DATA_ACEITO));
        d.setData_criacao(getLong(c, DATA_CRIACAO));
        d.setData_conclusao(getLong(c, DATA_CONCLUSAO));
        d.setData_visualizacao(getLong(c, DATA_VISUALIZACAO));
        return d;
    }

    @Override
    protected void setContent(Cursor c) {
        Desafio d = getDesafio(c);
        listaDesafios.add(d);
    }

    @Override
    protected void prepareContentReceiver() {
        listaDesafios = new ArrayList<Desafio>();
    }

    public List<Desafio> getDesafiosList(int limit){
        String LIMIT = "";
        if(limit > 0){
            LIMIT = " LIMIT " + limit;
        }
        getSelectQueryContent("SELECT * FROM " + DesafioDAO.TABLE_NAME + " " +
                              "ORDER BY " + DesafioDAO.ID + " " + LIMIT + ";");
        return listaDesafios;
    }

    public Desafio getDesafio(long id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = " + id + " LIMIT 1;";
        desafio = null;
        getSelectQueryContent(sql, new Behavior() {
            @Override
            public void setContent(Cursor c) {
                desafio = DesafioDAO.getDesafio(c);
            }
        });
        return desafio;
    }

    public boolean insereDesafio(Desafio d) {
        ContentValues cv = getContentValues(d);
        long id = insert(TABLE_NAME, cv);
        d.setId(id);
        return id > 0;
    }

    private ContentValues getContentValues(Desafio d) {
        ContentValues cv = new ContentValues();
        if(d.getId() > 0){
            cv.put(ID, d.getId());
        }
        if(d.getPremiacao() != null  && d.getPremiacao().getId() > 0){
            cv.put(PREMIACAO, d.getPremiacao().getId());
        }
        cv.put(TITULO, d.getTitulo());
        cv.put(DESCRICAO, d.getDescricao());
        cv.put(TENTATIVAS, d.getTentativas());
        cv.put(ACEITO, 0);
        cv.put(QUANTIDADE, d.getQuantidade());
        cv.put(TIPO, d.getTipo());
        cv.put(DATA_CRIACAO, d.getData_criacao());
        cv.put(DATA_ACEITO, "");
        cv.put(DATA_VISUALIZACAO, d.getData_visualizacao());
        return cv;
    }

    public void updateStatus(Desafio d, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        if(status == Desafio.CONCLUIDO){
            cv.put(DATA_CONCLUSAO, DataHelper.now());
        }
        write_db.update(TABLE_NAME, cv, ID + " = ?", new String[] { Long.toString(d.getId()) });
    }

    public Set<Long> getDesafios() {
        final Set<Long> desafio_set = new HashSet<>();
        String select = "SELECT " + ID + " FROM " + TABLE_NAME;
        DesafioBehavior d_b = new DesafioBehavior() {
            @Override
            public void setContent(Cursor c) {
                long id = getLong(c, ID);
                desafio_set.add(id);
            }
        };
        getSelectQueryContent(select, d_b);
        return desafio_set;
    }

    public Map<Long, Desafio> getMapDesafios() {
        final Map<Long, Desafio> desafio_map = new TreeMap<>();
        String select = "SELECT " + ID + " FROM " + TABLE_NAME;
        DesafioBehavior d_b = new DesafioBehavior() {
            @Override
            public void setContent(Cursor c) {
                long id = getLong(c, ID);
                if(!desafio_map.containsKey(id)){
                    desafio_map.put(id, getDesafio(c));
                }
            }
        };
        getSelectQueryContent(select, d_b);
        return desafio_map;
    }

    public boolean update(Desafio d) {
        ContentValues cv = getContentValues(d);
        return update(TABLE_NAME, cv, ID + " = ?", new String[] {String.valueOf(d.getId())});
    }

    private static abstract class DesafioBehavior implements Behavior {
        protected Desafio desafio;
        protected Map<Long, Desafio> desafios;
        public DesafioBehavior(){
            desafio = null;
            desafios = new TreeMap<>();
        }
    }

    public Map<Long, Desafio> get(Atividade a) {
        Map<Long, Desafio> desafios = null;
        if(a != null){
            String select = "SELECT * FROM " + TABLE_NAME + " as d " +
                    "INNER JOIN " + DesafioAtividadeDAO.TABLE_NAME + " as ad ON " +
                    "ad." + DesafioAtividadeDAO.ID_DESAFIO + " = d." + ID + " " +
                    "INNER JOIN " + AtividadeDAO.TABLE_NAME + " as a ON " +
                    "a." + AtividadeDAO.ID + " = ad." + DesafioAtividadeDAO.ID_DESAFIO + " " +
                    "WHERE a." + AtividadeDAO.ID + " = " + String.valueOf(a.getId());
            DesafioBehavior d_b = new DesafioBehavior() {
                @Override
                public void setContent(Cursor c) {
                    long id = getLong(c, ID);
                    if(!desafios.containsKey(id)){
                        Desafio d = getDesafio(c);
                        desafios.put(id, d);
                    }
                }
            };
            getSelectQueryContent(select, d_b);
            desafios = d_b.desafios;
        }
        return desafios;
    }
}
