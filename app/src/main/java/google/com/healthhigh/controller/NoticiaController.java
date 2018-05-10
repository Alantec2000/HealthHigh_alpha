package google.com.healthhigh.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import google.com.healthhigh.dao.DAO;
import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.dao.DesafioNoticiaDAO;
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
                        InteracaoNoticia i_n = getInteracaoDesafioAtual(n, d_a);
                        n.setInteracao_noticia(i_n);
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

    private InteracaoNoticia getInteracaoDesafioAtual(@NonNull Noticia n, @NonNull Desafio d) {
        InteracaoNoticia i_n = new InteracaoNoticia();
        String select =
                "SELECT * FROM " + InteracaoNoticiaDAO.TABLE_NAME + " as intn " +
                "INNER JOIN " + NoticiaDAO.TABLE_NAME + " as n ON " +
                "n." + NoticiaDAO.ID + " = intn." + InteracaoNoticiaDAO.ID_NOTICIA + " " +
                "INNER JOIN " + DesafioNoticiaDAO.TABLE_NAME + " as dn ON " + " " +
                "dn." + DesafioNoticiaDAO.ID_NOTICIA + " = n." + NoticiaDAO.ID + " " +
                "INNER JOIN " + DesafioDAO.TABLE_NAME + " as d ON " +
                "d." + DesafioDAO.ID + " = dn." + DesafioNoticiaDAO.ID_DESAFIO + " " +
                "WHERE n." + NoticiaDAO.ID + " = " + String.valueOf(n.getId()) + " AND " +
                        "d." + DesafioDAO.ID + " = " + String.valueOf(d.getId()) + " AND " +
                        "intn." + InteracaoNoticiaDAO.ID_PUBLICACAO + " = " + String.valueOf(d.getPublicacao().getId());
        Cursor c = executeSelect(select);
        try {
            if(c.moveToFirst()){
                do{
                  i_n.setId(c.getLong(c.getColumnIndex(InteracaoNoticiaDAO.ID)));
                  i_n.setTempo_leitura(c.getLong(c.getColumnIndex(InteracaoNoticiaDAO.TEMPO_LEITURA)));
                  i_n.setData_criacao(c.getLong(c.getColumnIndex(InteracaoNoticiaDAO.DATA_CRIACAO)));
                  i_n.setData_visualizacao(c.getLong(c.getColumnIndex(InteracaoNoticiaDAO.DATA_VISUALIZACAO)));
                  i_n.setInteracao_desafio(d.getInteracao_desafio());
                  i_n.setPublicacao(d.getPublicacao());
                  i_n.setNoticia(n);
                } while (c.moveToNext());
            } else {
                i_n = inserirNovaInteracao(n, d);
            }
        } catch (SQLiteException e){
            i_n = null;
            imprimeErroSQLite(e);
        } catch (Exception e) {
            i_n = null;
            e.printStackTrace();
        }
        return i_n;
    }

    public void setNoticiaLida(@NonNull Noticia noticia) {
        noticia.setData_visualizacao(DataHelper.now());
        n_d.atualizarNoticia(noticia);
    }
    public void setInteracaoNoticiaLida(@NonNull InteracaoNoticia interacao_noticia) {
        interacao_noticia.setData_visualizacao(DataHelper.now());
        i_n_d.atualizaInteracaoNoticia(interacao_noticia);
    }

    public InteracaoNoticia inserirNovaInteracao(Noticia noticia, Desafio d) throws Exception {
        InteracaoNoticia i_n = new InteracaoNoticia();
        i_n.setData_criacao(DataHelper.now());
        i_n.setData_visualizacao(0);
        i_n.setTempo_leitura(0);
        i_n.setNoticia(noticia);
        if(d.getPublicacao() == null){
            throw new Exception("Desafio informado não está publicado!");
        }
        i_n.setPublicacao(d.getPublicacao());
        noticia.setInteracao_noticia(i_n);
        i_n_d.inserirInteracaoNoticia(i_n);
        return i_n;
    }

    public InteracaoNoticia atualizarInteracaoNoticia(InteracaoNoticia i_n) {
        if(i_n != null && i_n.getPublicacao() != null && i_n.getNoticia() != null && i_n.getNoticia().getDesafio_atual() != null){
            i_n_d.atualizaInteracaoNoticia(i_n);
        }
        return i_n;
    }
}