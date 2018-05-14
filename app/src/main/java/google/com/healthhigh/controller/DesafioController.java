package google.com.healthhigh.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
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
import google.com.healthhigh.domain.Noticia;
import google.com.healthhigh.domain.Publicacao;
import google.com.healthhigh.domain.Questionario;
import google.com.healthhigh.domain.TipoMeta;
import google.com.healthhigh.utils.DataHelper;
import google.com.healthhigh.utils.MessageDialog;

/**
 * Created by Alan on 18/04/2018.
 * Responsável por encapsular a lógica associada aos desafios, seja no get do banco, construção de metas, etc....
 */

public class DesafioController extends DAO {
    private static String tag = "DesafioController";
    private DesafioDAO d_d;

    public DesafioController(Context context) {
        super(context);
        d_d = new DesafioDAO(context);
    }

    @Override
    protected void prepareContentReceiver() {

    }

    private abstract class DesafioBehavior implements Behavior {
        protected final Context context;
        protected Desafio desafio;
        protected Map<Long, Desafio> desafios;

        public DesafioBehavior(Context context) {
            this.context = context;
            resetContent();
        }

        private void resetContent() {
            desafio = new Desafio();
            desafios = new TreeMap<>();
        }

        public Context getContext() {
            return context;
        }

        public Desafio getDesafio() {
            return desafio;
        }

        public Map<Long, Desafio> getDesafios() {
            return desafios;
        }
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
        String select =
                "SELECT * FROM " + InteracaoDesafioDAO.TABLE_NAME + " as id " +
                " INNER JOIN " + DesafioDAO.TABLE_NAME + " as d ON " +
                " d." + DesafioDAO.ID + " = id." + InteracaoDesafioDAO.ID_DESAFIO + " " +
                " INNER JOIN " + PublicacaoDAO.TABLE_NAME + " as p ON " +
                " p." + PublicacaoDAO.ID + " = id." + InteracaoDesafioDAO.ID_PUBLICACAO + "" +
                " WHERE " + PublicacaoDAO.ID + " = " + String.valueOf(p.getId());

        Cursor c = executeSelect(select);
        try {
            if (c.moveToFirst()) {
                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                do {
                    i_d = InteracaoDesafioDAO.getInteracaoDesafio(c);
                    i_d.setDesafio(d);
                    i_d.setPublicacao(p);
                } while (c.moveToNext());
            } else {
                i_d = setNovaInteracaoDesafioVazia(d, p);
            }
        } catch (SQLiteException e) {
            imprimeErroSQLite(e);
        } finally {
            c.close();
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

    public Desafio getDesafio(Desafio d){
        Desafio desafio = new Desafio();
        String select = "SELECT * FROM " + DesafioDAO.TABLE_NAME + " " +
                "WHERE " + DesafioDAO.ID + " = " + String.valueOf(d.getId());
        DesafioBehavior d_b = new DesafioBehavior(context) {
            @Override
            public void setContent(Cursor c) {
                if(desafio.getId() == 0){
                    desafio = DesafioDAO.getDesafio(c);
                }
                Publicacao p = getPublicacaoAtual(desafio);
                if(p == null){
                    p = getPublicacaoAnterior(desafio);
                }
                if(p != null){
                    InteracaoDesafio i_d = getInteracaoPublicacaoAtual(p, desafio);
                }
                desafio.setMetas_list(getMetasDesafio(desafio));
            }
        };
        return desafio;
    }

    private Publicacao getPublicacaoAnterior(Desafio d) {
        String select = "SELECT MAX(" + PublicacaoDAO.ID + ") as last_p, * FROM " + PublicacaoDAO.TABLE_NAME + " " +
                "WHERE " + PublicacaoDAO.ID_DESAFIO + " = " + String.valueOf(d.getId());

        Cursor c = executeSelect(select);
        try {
            if(c.moveToFirst()){
                do{
                    Publicacao p = PublicacaoDAO.getPublicacao(c);
                }while(c.moveToNext());
            }
        } catch (SQLiteException e){
            imprimeErroSQLite(e);
        }

        return null;
    }

    public List<Desafio> getDesafios(long id) {
        List<Desafio> desafios;
        Map<Long, Desafio> desafios_map = new TreeMap<>();
        String select =
                "SELECT * FROM " + DesafioDAO.TABLE_NAME + " as d " +

                // Obtém publicações do desafio
                "LEFT JOIN " + PublicacaoDAO.TABLE_NAME + " as p ON " +
                "p." + PublicacaoDAO.ID_DESAFIO + " = d." + DesafioDAO.ID + " " +

                "LEFT JOIN " + InteracaoDesafioDAO.TABLE_NAME + " as intd ON " +
                "intd." + InteracaoDesafioDAO.ID_DESAFIO + " = d." + DesafioDAO.ID + " AND " +
                "intd." + InteracaoDesafioDAO.ID_PUBLICACAO + " = p." + PublicacaoDAO.ID;
        if(id > 0) {
            select += " WHERE " + DesafioDAO.ID + " = " + String.valueOf(id);
        }
        long now = DataHelper.now();
        Cursor c = executeSelect(select);
        try{
            if(c.moveToFirst()){
                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                do {
                    Desafio d;
                    long d_id = c.getLong(c.getColumnIndex(DesafioDAO.ID));
                    if(desafios_map.containsKey(d_id)){
                        d = desafios_map.get(d_id);
                    } else {
                        d = DesafioDAO.getDesafio(c);
                        desafios_map.put(d_id, d);
                    }

                    if(!c.isNull(c.getColumnIndex(PublicacaoDAO.ID))){
                        // Obtenho a publicação e verifico se ela é a última inserida.
                        // Se for, então ela substituirá a que estiver associada ao desafio na iteração atual
                        Publicacao p = PublicacaoDAO.getPublicacao(c);
                        if(d.getPublicacao() == null || (d.getPublicacao() != null && d.getPublicacao().getData_criacao() < p.getData_criacao())){
                            d.setPublicacao(p);
                            InteracaoDesafio i_d;
                            if(!c.isNull(c.getColumnIndex(InteracaoDesafioDAO.ID))){
                                i_d = InteracaoDesafioDAO.getInteracaoDesafio(c);
                            } else {
                                i_d = setNovaInteracaoDesafioVazia(d, p);
                            }
                            if(i_d != null){
                                d.setInteracao_desafio(i_d);
                            }
                        }
                    }
                }while (c.moveToNext());
            }
        } catch (SQLiteException e){
            imprimeErroSQLite(e);
        } finally {
            c.close();
            desafios = new ArrayList<>(desafios_map.values());
        }
        return desafios;
    }

    public Desafio getDesafio(long id) {
        Desafio d = new Desafio();
        if(id > 0){
            List<Desafio> desafios = getDesafios(id);
            for(Desafio d_aux : desafios){
                if(d_aux.getId() == id){
                    d = d_aux;
                    break;
                }
            }
        }
        return d;
    }

    public Map<Long, TipoMeta> getMetasDesafio(Desafio d) {
        Map<Long, TipoMeta> metas = new TreeMap<Long, TipoMeta>();
        QuestionarioController q_c = new QuestionarioController(context);
        List<Questionario> questionarios = q_c.getQuestionariosDesafio(d);
        for(Questionario q : questionarios)
            metas.put(q.getId(), q);
        NoticiaController n_c = new NoticiaController(context);
        List<Noticia> noticias = n_c.getNoticiasDesafio(d);
        for(Noticia n : noticias)
            metas.put(n.getId(), n);
        /*
        AtividadeController a_c = new AtividadeController(context);
        List<Atividade> atividades = a_c.getAtividadesDesafio(d);
        * */
        return metas;
    }
}
