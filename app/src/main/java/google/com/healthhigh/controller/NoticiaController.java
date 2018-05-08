package google.com.healthhigh.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import google.com.healthhigh.dao.DAO;
import google.com.healthhigh.dao.InteracaoNoticiaDAO;
import google.com.healthhigh.dao.NoticiaDAO;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.InteracaoNoticia;
import google.com.healthhigh.domain.Noticia;
import google.com.healthhigh.utils.DataHelper;

/**
 * Created by Alan on 24/04/2018.
 */

public class NoticiaController extends DAO {
    private Context context;
    private NoticiaDAO n_d;
    private InteracaoNoticiaDAO i_n_d;
    private DesafioController d_c;

    public NoticiaController(Context context) {
        super(context);
        n_d = new NoticiaDAO(context);
        i_n_d = new InteracaoNoticiaDAO(context);
        d_c = new DesafioController(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public Noticia obterNoticia(long id){
        Noticia n = new Noticia();
        if(id > 0){
            List<Noticia> noticias = this.obterListaNoticias(id);
            for(Noticia q_aux : noticias){
                if(q_aux.getId() == id){
                    n = q_aux;
                    break;
                }
            }
        }
        return n;
    }

    public List<Noticia> obterListaNoticias(long id){
        List<Noticia> noticias = new ArrayList<>();
        String select = "SELECT * FROM " + NoticiaDAO.TABLE_NAME + "";
        if(id > 0) {
            select += " WHERE " + NoticiaDAO.ID + " = " + String.valueOf(id) + ";";
        } else {
            select += ";";
        }
        Desafio d_a = d_c.getDesafioAtual();
        Cursor c = executeSelect(select);
        try {
            if (c.moveToFirst()) {
                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                do {
                    Noticia n = new Noticia();
                    n.setId(c.getLong(c.getColumnIndex(NoticiaDAO.ID)));
                    n.setCorpo(c.getString(c.getColumnIndex(NoticiaDAO.CORPO)));
                    n.setTitulo(c.getString(c.getColumnIndex(NoticiaDAO.TITULO)));
                    n.setData_criacao(c.getLong(c.getColumnIndex(NoticiaDAO.DATA_CRIACAO)));
                    n.setData_visualizacao(c.getLong(c.getColumnIndex(NoticiaDAO.DATA_VISUALIZACAO)));
                    if(d_a != null){
                        InteracaoNoticia i_n = getInteracaoDesafioAtual();
                        n.setDesafio_atual(d_a);
                    }
                    noticias.add(n);
                } while (c.moveToNext());
            }
        } catch (SQLiteException e) {
            imprimeErroSQLite(e);
        } finally {
            c.close();
        }
        return noticias;
    }

    private InteracaoNoticia getInteracaoDesafioAtual() {
        InteracaoNoticia i_n = new InteracaoNoticia();
        return i_n;
    }

    public void setNoticiaLida(@NonNull InteracaoNoticia interacao_noticia) {
        interacao_noticia.setData_visualizacao(DataHelper.now());
        i_n_d.atualizaInteracaoNoticia(interacao_noticia);
    }
}
