package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import google.com.healthhigh.db.CreateDB;
import google.com.healthhigh.domain.Item;
import google.com.healthhigh.domain.Meta;
import google.com.healthhigh.utils.Toaster;

/**
 * Created by Alan on 10/06/2017.
 */

public class ItemXMetaDAO extends DAO{
    private List<Item> itens;
    public static String
            TABLE_NAME = "phh_itemxmeta",
            ID = "i_id",
            ID_META = "i_id_meta",
            ID_ITEM = "i_id_item";

    public ItemXMetaDAO(Context context) {
        super(context);
    }

    @Override
    protected void setContent(Cursor c) {

    }

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                ID_META + " INTEGER NOT NULL, " +
                ID_ITEM + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + ID_META + ") REFERENCES " + MetaDAO.TABLE_NAME + "(" + MetaDAO.ID + ")"+
                "FOREIGN KEY (" + ID_ITEM + ") REFERENCES " + ItemDAO.TABLE_NAME + " ("+ ItemDAO.ID + ")" +
                ")";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    @Override
    protected void prepareContentReceiver() {
        itens = new ArrayList<>();
    }

    public List<Item> obterItemXmeta(String select) {
        getSelectQueryContent(select, new Behavior() {
            @Override
            public void setContent(Cursor c) {
                Item i = ItemDAO.getItem(c);
                if(!c.isNull(c.getColumnIndex(MetaDAO.ID)) && c.getColumnIndex(MetaDAO.ID) != -1){
                    Meta m = MetaDAO.getMeta(c);
                    i.addMeta(m);
                }
            }
        });
        return itens;
    }

    protected ContentValues createInsertItem(Item i, Meta m) {
        ContentValues cv = new ContentValues();
        cv.put(ID_ITEM, i.getId());
        cv.put(ID_META, m.getId());
        return cv;
    }
}
