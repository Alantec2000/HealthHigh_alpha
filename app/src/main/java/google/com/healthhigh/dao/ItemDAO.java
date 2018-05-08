package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import google.com.healthhigh.domain.Item;
public class ItemDAO extends DAO {
    private Context context = null;
    private List<Item> itens;
    public static final String
            TABLE_NAME = "phh_item",
            ID = "i_id",
            NOME = "s_nome",
            DESCRICAO = "s_descricao",
            TIPO = "i_tipo",
            XP = "i_experiencia",
            IMG = "b_imagem",
            DATA = "i_data";

    public ItemDAO(Context context) {
        super(context);
    }

    public static String getTableName() {
        return TABLE_NAME;
    }
    public static String getNOME() {
        return NOME;
    }
    public static String getTIPO() {
        return TIPO;
    }
    public static String getXP() {
        return XP;
    }
    public static String getIMG() {
        return IMG;
    }

    public static String getCreateTableString() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                NOME + " TEXT NOT NULL, " +
                DESCRICAO + " TEXT NOT NULL, " +
                TIPO + " INTEGER NOT NULL DEFAULT 1, " +
                XP + " INTEGER NOT NULL, " +
                DATA + " INTEGER NOT NULL, " +
                IMG  + " BLOB DEFAULT NULL);";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    static Item getItem(Cursor c){
        Item i = new Item();
        i.setId(c.getInt(c.getColumnIndex(ID)));
        i.setNome(c.getString(c.getColumnIndex(NOME)));
        i.setDescricao(c.getString(c.getColumnIndex(DESCRICAO)));
        i.setImg(c.getBlob(c.getColumnIndex(IMG)));
        i.setTipo(c.getInt(c.getColumnIndex(TIPO)));
        i.setXp(c.getInt(c.getColumnIndex(XP)));
        i.setData(c.getLong(c.getColumnIndex(DATA)));
        return i;
    }

    public Item getItem(String select){
        getSelectQueryContent(select);
        Iterator iT = itens.iterator();
        Item i = null;
        if(iT.hasNext()){
            i =(Item) iT.next();
        }
        return i;
    }

    public List<Item> getItens(String select) {
        getSelectQueryContent(select);
        return itens;
    }

    public void insereItem(Item i) {
        ContentValues cv =  createInsertItem(i);
        write_db.insert(TABLE_NAME, null, cv);
    }

    @Override
    protected void setContent(Cursor c) {
        Item i = getItem(c);
        itens.add(i);
    }

    @Override
    protected void prepareContentReceiver() {
        itens = new ArrayList<>();
    }

    private ContentValues createInsertItem(Item i) {
        ContentValues cv = new ContentValues();
        cv.put(NOME, i.getNome());
        cv.put(DESCRICAO, i.getDescricao());
        cv.put(TIPO, i.getTipo());
        cv.put(XP, i.getXp());
        cv.put(IMG, i.getImg());
        cv.put(DATA, "1497571337930");// <-- Valor temporário até eu aprender a usar datas nessa desgraça
        return cv;
    }
}
