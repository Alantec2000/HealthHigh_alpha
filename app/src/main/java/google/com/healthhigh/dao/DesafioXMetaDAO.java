package google.com.healthhigh.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.Meta;
import google.com.healthhigh.utils.Toaster;

/**
 * Created by @author Alan on 23/07/2017.
 */

public class DesafioXMetaDAO extends DAO {
    private final String TAG = "DesafioXMetaDAO";

    /*Content receivers*/
    private Desafio desafio;
    private Meta m;
    private TreeMap<Long, Desafio> listaDesafios = null;
    private List<Meta> listaMetas = null;

    /*Colunas*/
    public static String TABLE_NAME = "phh_desafio_meta";
    public static String ID = "id_desafio_meta";
    public static String ID_DESAFIO = "id_desafio_desafio_meta";
    public static String ID_META = "i_id_meta_desafio_meta";

    public DesafioXMetaDAO(Context context) {
        super(context);
    }

    public static String getCreateTableString(){
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY NOT NULL, " +
                ID_DESAFIO + " INTEGER NOT NULL, " +
                ID_META + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + ID_DESAFIO + ") REFERENCES " + DesafioDAO.TABLE_NAME + "(" + DesafioDAO.ID + ")" +
                "FOREIGN KEY(" + ID_META + ") REFERENCES " + MetaDAO.TABLE_NAME + "(" + MetaDAO.ID + ")" +
                ");";
    }

    public static String getDropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

    public List<Meta> getDesafiosXMetas() {
        /* Obtém desafios que estejam associados ao menos a uma meta */
        String sql = "SELECT * FROM " + TABLE_NAME + " as dM " +
                     ";";

        getSelectQueryContent(sql, new Behavior() {
            @Override
            public void setContent(Cursor c) {
                Toaster.toastShortMessage(context, c.getCount());
            }
        });

        return listaMetas;
    }

    private TreeMap<Long, Desafio> getDesafiosAssocMetas() {
        /* Obtém desafios que estejam associados ao menos a uma meta */
        String sql = "SELECT * FROM " + TABLE_NAME + " as dM " +
                     "INNER JOIN " + DesafioDAO.TABLE_NAME + " as d " +
                        "dM." + ID_DESAFIO + " = d." + DesafioDAO.ID + ";";

        getSelectQueryContent(sql, new Behavior() {
            @Override
            public void setContent(Cursor c) {
                Desafio d = DesafioDAO.getDesafio(c);
                listaDesafios.put(d.getId(), d);
            }
        });

        return listaDesafios;
    }

    public Desafio getDesafioMetas(long id) {
        /* Obtem um desafio e suas metas */
        desafio = null;
        String sql = "SELECT * FROM " + DesafioDAO.TABLE_NAME + " AS d " +
                     "LEFT JOIN " + TABLE_NAME + " AS dM " +
                        "ON d." + DesafioDAO.ID + " = dM." + ID_DESAFIO + " " +
                     "LEFT JOIN " + MetaDAO.TABLE_NAME + " AS m " +
                        "ON dM." + ID_META + " = m." + MetaDAO.ID + " " +
                     "WHERE d." + DesafioDAO.ID + " = " + id + ";";

        getSelectQueryContent(sql, new Behavior() {
            @Override
            public void setContent(Cursor c) {
                if(desafio == null && !c.isNull(c.getColumnIndex(DesafioDAO.ID))){
                    desafio = DesafioDAO.getDesafio(c);
                }
                if(!c.isNull(c.getColumnIndex(DesafioXMetaDAO.ID_META))){
                    Meta m = MetaDAO.getMeta(c);
                    listaMetas.add(m);
                }
            }
        });

        if(desafio != null)
            desafio.setMetas_TO_OLD(listaMetas);

        return desafio;
    }

    public List<Meta> getMetasFromDesafio(Desafio d) {
        /* Obtem a lista de metas de um desafio */
        String sql = "SELECT * FROM " + TABLE_NAME + " as dM " +
                     "INNER JOIN " + MetaDAO.TABLE_NAME + " as m " +
                        "dM." + ID_META + " = m." + MetaDAO.ID + " " +
                     "WHERE d." + DesafioDAO.ID + " = " + d.getId() + ";";

        getSelectQueryContent(sql, new Behavior() {
            @Override
            public void setContent(Cursor c) {
                Meta m = MetaDAO.getMeta(c);
                listaMetas.add(m);
            }
        });
        return listaMetas;
    }

    private TreeMap<Long, Desafio> getDesafioXMeta() {
        String sql = "SELECT * FROM " + TABLE_NAME + " as dM " +
                     "INNER JOIN " + MetaDAO.TABLE_NAME + " as m " +
                        "ON dM." + ID_META + " = " + MetaDAO.ID + " " +
                     "INNER JOIN " + DesafioDAO.TABLE_NAME + " as d AS d " +
                        "ON d." + DesafioDAO.ID + " = dM." + ID_DESAFIO +";";

        getSelectQueryContent(sql, new Behavior() {
            @Override
            public void setContent(Cursor c) {
                Desafio d = null;
                Meta m = null;
                long id_desafio = c.getLong(c.getColumnIndex(DesafioXMetaDAO.ID_DESAFIO));

                try {
                    d = listaDesafios.get(id_desafio);
                } catch (Exception e){
                    Log.e(TAG, "Desafio " + Long.toString(id_desafio) + " não encontrado ainda!");
                }

                if(d == null){
                    d = DesafioDAO.getDesafio(c);
                }
                d.addMeta(m);
                listaDesafios.put(d.getId(), d);
            }
        });
        return listaDesafios;
    }

    public void insereDesafioAssocMeta(Desafio d, Meta m){
        ContentValues cv = getContentValues(d, m);
        insert(TABLE_NAME, cv);
    }

    private ContentValues getContentValues(Desafio d, Meta m){
        ContentValues cv = new ContentValues();
        cv.put(ID_DESAFIO, d.getId());
        cv.put(ID_META, m.getId());
        return cv;
    }

    @Override
    protected void prepareContentReceiver() {
        listaMetas = new ArrayList<Meta>();
        listaDesafios = new TreeMap<Long, Desafio>();
    }

    @Override
    protected void setContent(Cursor c) {
    }
}
