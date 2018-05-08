package google.com.healthhigh.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.activities.RealizandoDesafioActivity;
import google.com.healthhigh.dao.DAO;
import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.dao.DesafioQuestionarioDAO;
import google.com.healthhigh.dao.DesafioXMetaDAO;
import google.com.healthhigh.dao.InteracaoDesafioDAO;
import google.com.healthhigh.dao.PublicacaoDAO;
import google.com.healthhigh.dao.QuestionarioDAO;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.InteracaoDesafio;
import google.com.healthhigh.domain.Meta;
import google.com.healthhigh.domain.Publicacao;
import google.com.healthhigh.domain.Questionario;
import google.com.healthhigh.utils.DataHelper;
import google.com.healthhigh.utils.MessageDialog;

/**
 * Created by Alan on 18/04/2018.
 * Responsável por encapsular a lógica associada aos desafios, seja no get do banco, construção de metas, etc....
 */

public class DesafioController extends DAO {
    private static String tag = "DesafioController";

    public DesafioController(Context context) {
        super(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public static Desafio obterDesafioMetas(Context c, long id){
        DesafioDAO d_dao = new DesafioDAO(c);
        Desafio d = d_dao.getDesafio(id);
        if(d != null && d.getId() > 0){
            Log.i(tag, "Desafio Obtido com sucesso");
            DesafioXMetaDAO dm_dao = new DesafioXMetaDAO(c);
            ArrayList<Meta> metas = getMetasDoDesafio(c, d);
        }
        return d;
    }

    private static ArrayList<Meta> getMetasDoDesafio(Context c, Desafio d){
        ArrayList<Meta> metas = new ArrayList<Meta>();
        QuestionarioDAO q_dao = new QuestionarioDAO(c);

        return null;
    }

    public Publicacao getPublicacaoAtual(Desafio d) {
//        1525061301676
        Publicacao p = null;
        if(d != null) {
            long now = DataHelper.now();
            String select = "SELECT * FROM " + PublicacaoDAO.TABLE_NAME + " WHERE " +
                    String.valueOf(now) + " BETWEEN " + PublicacaoDAO.DATA_INICIO + " AND " + PublicacaoDAO.DATA_FIM + " AND " +
                    PublicacaoDAO.ID_DESAFIO + " = " + String.valueOf(d.getId());
            Cursor c = executeSelect(select);
            try {
                if (c.moveToFirst()) {
                    Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                    do {
                        p = PublicacaoDAO.getPublicacao(c);
                        p.setDesafio(d);
                        d.setPublicacao(p);
                    } while (c.moveToNext());
                }
            } catch (SQLiteException e) {
                imprimeErroSQLite(e);
            } finally {
                c.close();
            }
        }
        return p;
    }

    public InteracaoDesafio getInteracaoPublicacaoAtual(Publicacao p, Desafio d){
        InteracaoDesafio i_d = null;
        if(p != null && p.getId() > 0){
            String select =
                    "SELECT * FROM " + InteracaoDesafioDAO.TABLE_NAME + " as id " +
                    " INNER JOIN " + DesafioDAO.TABLE_NAME + " as d ON " +
                    " d." + DesafioDAO.ID + " = id." + InteracaoDesafioDAO.ID_DESAFIO + " " +
                    " INNER JOIN " + PublicacaoDAO.TABLE_NAME + " as p ON " +
                    " p." + PublicacaoDAO.ID + " = id." + InteracaoDesafioDAO.ID_PUBLICACAO + "";

            Cursor c = executeSelect(select);
            try {
                if (c.moveToFirst()) {
                    Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                    do {
                        i_d = InteracaoDesafioDAO.getInteracaoDesafio(c);
                        i_d.setDesafio(d);
                        i_d.setPublicacao(p);
                    } while (c.moveToNext());
                }
            } catch (SQLiteException e) {
                imprimeErroSQLite(e);
            } finally {
                c.close();
            }
        }
        return i_d;
    }

    public InteracaoDesafio setNovaInteracaoDesafioVazia(Desafio d, Publicacao p) {
        InteracaoDesafio i_d = null;
        if((d != null && d.getId() > 0) && (p != null && p.getId() > 0)){
            i_d = new InteracaoDesafio();
            InteracaoDesafioDAO i_d_dao = new InteracaoDesafioDAO(context);
            ContentValues cv = new ContentValues();
            cv.put(InteracaoDesafioDAO.ID_DESAFIO, d.getId());
            cv.put(InteracaoDesafioDAO.ID_PUBLICACAO, p.getId());
            cv.put(InteracaoDesafioDAO.DATA_CRIACAO, DataHelper.now());
            cv.put(InteracaoDesafioDAO.REALIZANDO, false);
            i_d_dao.insertNovaInteracao(i_d, cv);
        }
        return i_d;
    }

    public Desafio getDesafioAtual() {
        Desafio d = null;
        String select = "SELECT * FROM " + DesafioDAO.TABLE_NAME + " as d " +
                "INNER JOIN " + PublicacaoDAO.TABLE_NAME + " as p ON" +
                " p." + PublicacaoDAO.ID_DESAFIO + " = d." + DesafioDAO.ID + "" +
                " INNER JOIN " + InteracaoDesafioDAO.TABLE_NAME + " id ON " +
                "id." + InteracaoDesafioDAO.ID_DESAFIO + " = d." + DesafioDAO.ID + " AND " +
                " p." + PublicacaoDAO.ID + " = id." + InteracaoDesafioDAO.ID_PUBLICACAO + "" +
                " WHERE " + InteracaoDesafioDAO.REALIZANDO + " = 1";
        Cursor c = executeSelect(select);
        try {
            if (c.moveToFirst()) {
                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                do {
                    d = DesafioDAO.getDesafio(c);
                    Publicacao p;
                    p = PublicacaoDAO.getPublicacao(c);
                    d.setPublicacao(p);
                    InteracaoDesafio i_d = InteracaoDesafioDAO.getInteracaoDesafio(c);
                    d.setInteracao_desafio(i_d);
                    /*i_d = InteracaoDesafioDAO.getInteracaoDesafio(c);
                    i_d.setDesafio(d);
                    i_d.setPublicacao(p);*/
                } while (c.moveToNext());
            }
        } catch (SQLiteException e) {
            imprimeErroSQLite(e);
        } finally {
            c.close();
        }
        return d;
    }

    public Map<Long, Desafio> getDesafiosAssociados(Questionario q) {
        Map<Long, Desafio> d_as = new TreeMap<>();
        if(q != null){
            String select = "SELECT * FROM " + DesafioDAO.TABLE_NAME + " as d " +
                    "INNER JOIN " + DesafioQuestionarioDAO.TABLE_NAME + " as dq ON" +
                    " dq." + DesafioQuestionarioDAO.ID_DESAFIO + " = d." + DesafioDAO.ID + "" +
                    " INNER JOIN " + QuestionarioDAO.TABLE_NAME + " q ON " +
                    "q." + QuestionarioDAO.ID + " = dq." + DesafioQuestionarioDAO.ID_QUESTIONARIO + "" +
                    " WHERE q." + QuestionarioDAO.ID + " = " + String.valueOf(q.getId());
            Cursor c = executeSelect(select);
            try {
                if(c.moveToFirst()){
                    do {
                        Desafio d = DesafioDAO.getDesafio(c);
                        Publicacao p = getPublicacaoAtual(d);
                        d.setPublicacao(p);
                        if(p != null){
                            d.setInteracao_desafio(getInteracaoPublicacaoAtual(p, d));
                        }
                        d_as.put(d.getId(), d);
                    }while (c.moveToNext());
                }
            } catch (SQLiteException e){
                imprimeErroSQLite(e);
            } finally {
                c.close();
            }
        }
        return d_as;
    }
}
