package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import google.com.healthhigh.db.CreateDB;
import google.com.healthhigh.domain.Desafio;

public class DesafioDAO extends DAO {
    private static final String QUANTIDADE = "i_quantidade";
    private static final String DATA_CONCLUSAO = "s_data_conclusao";
    private static final String STATUS = "i_status"; // 1 - pendente, 2 - visualizado, 3 - em execução, 4 - finalizado, 5 - encerrado
    private Desafio desafio;
    public static String
            TABLE_NAME = "phh_desafio",
            ID = "id_desafio",
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
                TENTATIVAS + " INTEGER DEFAULT 0);";
    }

    public static String getDropTableString() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public static Desafio getDesafio(Cursor c){
        Desafio d = new Desafio();
        d.setId(c.getInt(c.getColumnIndex(ID)));
        d.setDescricao(c.getString(c.getColumnIndex(DESCRICAO)));
        d.setTentativas(c.getInt(c.getColumnIndex(TENTATIVAS)));
        d.setTitulo(c.getString(c.getColumnIndex(TITULO)));
        d.setQuantidade(c.getInt(c.getColumnIndex(QUANTIDADE)));
        d.setStatus(c.getInt(c.getColumnIndex(STATUS)));
        d.setTipo(c.getInt(c.getColumnIndex(TIPO)));
        d.setData_aceito(c.getLong(c.getColumnIndex(DATA_ACEITO)));
        d.setData_criacao(c.getLong(c.getColumnIndex(DATA_CRIACAO)));
        d.setData_conclusao(c.getLong(c.getColumnIndex(DATA_CONCLUSAO)));
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
        getSelectQueryContent("SELECT * FROM " + DesafioDAO.getTableName() + " " +
                              "ORDER BY " + DesafioDAO.getID() + " " + LIMIT + ";");
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

    public void insereDesafio(Desafio d) {
        write_db.insert(TABLE_NAME, null, createInsertDesafio(d));
    }

    private ContentValues createInsertDesafio(Desafio d) {
        ContentValues cv = new ContentValues();
        cv.put(TITULO, d.getTitulo());
        cv.put(DESCRICAO, d.getDescricao());
        cv.put(TENTATIVAS, d.getTentativas());
        cv.put(ACEITO, 0);
        cv.put(QUANTIDADE, d.getQuantidade());
        cv.put(TIPO, d.getTipo());
        cv.put(DATA_CRIACAO, d.getData_criacao());
        cv.put(DATA_ACEITO, "");
        return cv;
    }

    public void updateStatus(Desafio d, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        if(status == Desafio.CONCLUIDO){
            cv.put(DATA_CONCLUSAO, System.currentTimeMillis()/1000);
        }
        write_db.update(TABLE_NAME, cv, ID + " = ?", new String[] { Long.toString(d.getId()) });
    }

    public static String getID() {
        return ID;
    }

    public static String getTITULO() {
        return TITULO;
    }

    public static String getDESCRICAO() {
        return DESCRICAO;
    }

    public static String getTENTATIVAS() {
        return TENTATIVAS;
    }

    public static String getTIPO() {
        return TIPO;
    }

    public static String getACEITO() {
        return ACEITO;
    }

    public static String getDataCriacao() {
        return DATA_CRIACAO;
    }

    public static String getDataAceito() {
        return DATA_ACEITO;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }
}
