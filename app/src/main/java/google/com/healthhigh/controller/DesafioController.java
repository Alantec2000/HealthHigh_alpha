package google.com.healthhigh.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.dao.DAO;
import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.dao.DesafioQuestionarioDAO;
import google.com.healthhigh.dao.InteracaoDesafioDAO;
import google.com.healthhigh.dao.PublicacaoDAO;
import google.com.healthhigh.dao.QuestionarioDAO;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.Interacao;
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

    public void iniciarDesafio(Desafio d) {
        if(d.getStatus() == Desafio.PENDENTE){
            if(verificarDesafioAceitoNoMomento(d).size() == 0){
                d.getInteracao_desafio().setData_aceito(DataHelper.now());
                d.getInteracao_desafio().setRealizando_no_momento(true);
                InteracaoDesafioDAO i_d_d = new InteracaoDesafioDAO(context);
                boolean iniciou = false;
                try {
                    iniciou = i_d_d.atualizarInteracaoDesafio(d.getInteracao_desafio());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(iniciou){
                    MessageDialog.showMessage(context, "Desafio iniciado com sucesso!", "Sucesso: ");
                } else {
                    MessageDialog.showMessage(context, "Não foi possível iniciar o desafio!", "Erro ao iniciar desafio");
                }
            } else {
                MessageDialog.showMessage(context, "Já existe um desafio em execução no momento!", "Erro:");
            }
        } else if(d.getStatus() == Desafio.NAO_PUBLICADO){
            MessageDialog.showMessage(context, "Não foi possível iniciar o desafio!", "Erro:");
        }
    }

    public void cancelarDesafio(Desafio d) {
        if(d.getPublicacao() != null){
            d.getInteracao_desafio().setData_cancelamento(DataHelper.now());
            d.getInteracao_desafio().setRealizando_no_momento(false);
            InteracaoDesafioDAO i_d_d = new InteracaoDesafioDAO(context);
            boolean iniciou = false;
            try {
                iniciou = i_d_d.atualizarInteracaoDesafio(d.getInteracao_desafio());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(iniciou){
                MessageDialog.showMessage(context, "Desafio cancelado!", "Sucesso:");
            } else {
                MessageDialog.showMessage(context, "Não foi possível cancelar o desafio!", "Erro:");
            }
        } else {
            MessageDialog.showMessage(context, "Não foi possível cancelar o desafio!", "Erro:");
        }
    }

    public List<Desafio> verificarDesafioAceitoNoMomento(Desafio d) {
        String select =
                "SELECT * FROM " + InteracaoDesafioDAO.TABLE_NAME + " as i_d " +
                "INNER JOIN " + DesafioDAO.TABLE_NAME + " as d ON " +
                " d." + DesafioDAO.ID + " = i_d." + InteracaoDesafioDAO.ID_DESAFIO + " " +
                "WHERE i_d." + InteracaoDesafioDAO.REALIZANDO + " = 1 AND " + InteracaoDesafioDAO.ID_DESAFIO + " <> " + String.valueOf(d.getId()) + " ;";
        DesafioBehavior d_b = new DesafioBehavior(context) {
            @Override
            public void setContent(Cursor c) {
                long id_d = c.getLong(c.getColumnIndex(DesafioDAO.ID));
                Desafio d = DesafioDAO.getDesafio(c);
                desafios.put(id_d, d);
            }
        };
        getSelectQueryContent(select, d_b);
        return new ArrayList<>(d_b.getDesafios().values());
    }

    public boolean verificarDesafioConcluido(Desafio d) {
        boolean concluido = false;
        if(d.getInteracao_desafio() != null){
            int n_metas = d.getMetas_list().size(), n_metas_concluidas = 0;
            QuestionarioController q_c = new QuestionarioController(context);
            for(TipoMeta m : d.getMetas_list()){
                boolean concluida = false;
                switch (m.getTipo()){
                    case TipoMeta.QUESTIONARIO:
                        concluida = q_c.validar_respostas((Questionario) m);
                        break;
                    case TipoMeta.NOTICIA:
                        Noticia n = (Noticia) m;
                        concluida = (n.getInteracao_noticia() != null &&
                                     n.getInteracao_noticia().getTempo_leitura() > 0);
                    break;
                    case TipoMeta.ATIVIDADE:
                        break;
                    case TipoMeta.EVENTO:
                        break;
                }

                if(concluida){
                    n_metas_concluidas++;
                }
            }
            concluido = n_metas_concluidas >= n_metas;
        }
        return concluido;
    }

    public boolean concluirDesafio(InteracaoDesafio d) {
        Log.i("DesafioController", "Desafio Concluído");
        d.setDataConclusao(DataHelper.now());
        d.setStatus(InteracaoDesafio.CONCLUIDO);
        d.setRealizando_no_momento(false);
        InteracaoDesafioDAO i_d = new InteracaoDesafioDAO(context);
        return i_d.atualizarInteracaoDesafio(d);
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
        String select =
                "SELECT * FROM " + DesafioDAO.TABLE_NAME + " as d " +
                "INNER JOIN " + PublicacaoDAO.TABLE_NAME + " as p ON" +
                    " p." + PublicacaoDAO.ID_DESAFIO + " = d." + DesafioDAO.ID + "" +
                " INNER JOIN " + InteracaoDesafioDAO.TABLE_NAME + " id ON " +
                    "id." + InteracaoDesafioDAO.ID_DESAFIO + " = d." + DesafioDAO.ID + " AND " +
                    " p." + PublicacaoDAO.ID + " = id." + InteracaoDesafioDAO.ID_PUBLICACAO + "" +
                " WHERE " + DataHelper.now() + " BETWEEN p." + PublicacaoDAO.DATA_INICIO + " AND " + PublicacaoDAO.DATA_FIM + " AND " +
                    "id." + InteracaoDesafioDAO.REALIZANDO + " = 1 LIMIT 1;";
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
                    desafio.setInteracao_desafio(i_d);
                }
                desafio.setMetas_list(getMetasDesafio(desafio));
            }
        };
        getSelectQueryContent(select, d_b);
        return d_b.getDesafio();
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

    public List<TipoMeta> getMetasDesafio(Desafio d) {
        List<TipoMeta> metas = new ArrayList<>();
        QuestionarioController q_c = new QuestionarioController(context);
        metas.addAll(q_c.getQuestionariosDesafio(d));
        NoticiaController n_c = new NoticiaController(context);
        metas.addAll(n_c.getNoticiasDesafio(d));
        return metas;
    }
}
