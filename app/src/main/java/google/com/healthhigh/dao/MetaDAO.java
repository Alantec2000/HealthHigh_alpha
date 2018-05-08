package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.TreeMap;

import google.com.healthhigh.domain.Meta;

public class MetaDAO extends DAO{
    static String
            TABLE_NAME = "phh_meta",
            ID = "i_id",
            NOME = "s_nome",
            DESCRICAO = "s_descricao",
            TIPO = "i_tipo",
            QUANTIDADE = "i_qtde",
            TEMPO = "i_tempo",
            DATA = "i_data";
    private TreeMap<Long, Meta> metas;
    private Meta meta;

    public MetaDAO(Context context) {
        super(context);
    }

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                NOME + " TEXT NOT NULL, " +
                DESCRICAO + " TEXT NOT NULL DEFAULT '', " +
                TIPO + " INTEGER NOT NULL DEFAULT 0, " +
                QUANTIDADE + " INTEGER NOT NULL DEFAULT 0, " +
                TEMPO + " INTEGER NOT NULL, " +
                DATA + " INTEGER NOT NULL);";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public TreeMap<Long, Meta> getMetas(){
        String sql = "SELECT * FROM " + TABLE_NAME + ";";
        getSelectQueryContent(sql, new Behavior() {
            @Override
            public void setContent(Cursor c) {
                Meta m = MetaDAO.getMeta(c);
                metas.put(m.getId(), m);
            }
        });
        return metas;
    }

    public static Meta getMeta(Cursor c){
        Meta m = new Meta();
        m.setId(c.getInt(c.getColumnIndex(MetaDAO.ID)));
        m.setNome(c.getString(c.getColumnIndex(MetaDAO.NOME)));
        m.setDescricao(c.getString(c.getColumnIndex(MetaDAO.DESCRICAO)));
        m.setTipo(c.getInt(c.getColumnIndex(MetaDAO.TIPO)));
        m.setQtde(c.getInt(c.getColumnIndex(MetaDAO.QUANTIDADE)));
        m.setData(c.getLong(c.getColumnIndex(MetaDAO.DATA)));
        m.setTempo(c.getLong(c.getColumnIndex(MetaDAO.TEMPO)));
        return m;
    }

    public void insereMeta(Meta m){
        ContentValues cv = getContentValues(m);
        write_db.insert(TABLE_NAME, null, cv);
    }

    private ContentValues getContentValues(Meta m){
        ContentValues cv = new ContentValues();
        cv.put(NOME, m.getNome());
        cv.put(TIPO, m.getTipo());
        cv.put(DATA, m.getData());
        cv.put(DESCRICAO, m.getDescricao());
        cv.put(QUANTIDADE, m.getQtde());
        cv.put(TEMPO, m.getTempo());
        return cv;
    }

    @Override
    protected void prepareContentReceiver() {
        metas = new TreeMap<Long, Meta>();
        meta = new Meta();
    }
}
