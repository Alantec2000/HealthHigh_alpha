package google.com.healthhigh.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import google.com.healthhigh.dao.DAO;
import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.dao.DesafioQuestionarioDAO;
import google.com.healthhigh.dao.InteracaoDesafioDAO;
import google.com.healthhigh.dao.PublicacaoDAO;
import google.com.healthhigh.dao.QuestionarioDAO;
import google.com.healthhigh.domain.Atividade;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.InteracaoDesafio;
import google.com.healthhigh.domain.Meta;
import google.com.healthhigh.domain.Noticia;
import google.com.healthhigh.domain.Premiacao;
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
    private DesafioDAO d_dao;
    private PremiacaoController p_c;
    private AtividadeController a_c;

    public DesafioController(Context context) {
        super(context);
        d_dao = new DesafioDAO(context);
    }

    public void atualizar(Desafio d) {
        if(d != null){
            d_dao.update(d);
        }
    }

    public abstract class DesafioBehavior implements DAO.Behavior {
        protected Desafio desafio;
        protected Map<Long, Desafio> desafios;

        DesafioBehavior(Context context) {
            desafio = new Desafio();
            desafios = new TreeMap<>();
        }
    }

    @Override
    protected void prepareContentReceiver() {

    }

    public void iniciarDesafio(Desafio d) {
        if(d.getStatus() == Desafio.PENDENTE){
            if(verificarDesafioAceitoNoMomento(d).size() == 0){
                InteracaoDesafio id = d.getInteracao_desafio();
                id.setDesafio(d);
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
                    MessageDialog.showMessage(context, "Não foi possível iniciarPressionado o desafio!", "Erro ao iniciarPressionado desafio");
                }
            } else {
                MessageDialog.showMessage(context, "Já existe um desafio em execução no momento!", "Erro:");
            }
        } else if(d.getStatus() == Desafio.NAO_PUBLICADO){
            MessageDialog.showMessage(context, "Não foi possível iniciarPressionado o desafio!", "Erro:");
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
        return new ArrayList<>(d_b.desafios.values());
    }

    public boolean verificarDesafioConcluido(Desafio d) {
        boolean concluido = false;
        if(d.getInteracao_desafio() != null){
            int n_metas = d.getMetas().size(), n_metas_concluidas = 0;
            QuestionarioController q_c = new QuestionarioController(context);
            for(TipoMeta m : d.getMetas()){
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
                        Atividade a = (Atividade) m;
                        concluida = a.isConcluida();
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

    public Map<Long, Desafio> getDesafiosAssociados(Atividade a) {
        Map<Long, Desafio> desafios = new TreeMap<>();
        if(a != null) {
            desafios = d_dao.get(a);
        }
        return desafios;
    }

    public void inserirDesafios(List<Desafio> desafios) {
        PremiacaoController p_c = new PremiacaoController(context);
        Map<Long, Premiacao> premiacoes = p_c.getPremiacoes();
        PublicacaoController pu_c = new PublicacaoController(context);
        for (Desafio d: desafios) {
            d_dao.getWritableDatabase().beginTransaction();
            try {
                if(d.getPremiacao() != null){
                    if(!premiacoes.containsKey(d.getPremiacao().getId())){
                        if(p_c.inserirPremiacao(d.getPremiacao())){
                            premiacoes.put(d.getPremiacao().getId(), d.getPremiacao());
                        } else {
                            throw new Exception("Premiação do desafio " + d.getTitulo() + " Não foi inserida!");
                        }
                    }
                    if(premiacoes.containsKey(d.getPremiacao().getId())) {
                        if(inserirMetas(d, premiacoes) && d_dao.insereDesafio(d)){
                            if(!associarAtividades(d.getAtividades(), d)){
                                throw new Exception("As atividades do desafio " + d.getTitulo() + " Não foram associadas!");
                            }
                        } else {
                            throw new Exception("As atividades do desafio " + d.getTitulo() + " Não foram associadas!");
                        }
                    } else {
                        throw new Exception("Premiação do desafio " + d.getTitulo() + " Não foi encontrada!");
                    }
                } else {
                    Log.e("inserirDesafios","Premiação do desafio " + d.getTitulo() + " Não informada!");
                }
                d_dao.getWritableDatabase().setTransactionSuccessful();
            } catch (SQLiteException e) {
                imprimeErroSQLite(e);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                d_dao.getWritableDatabase().endTransaction();
            }

            if(d.getPublicacoes().size() > 0){
                pu_c.inserirPublicacoes(d, d.getPublicacoes());
            } else {
                pu_c.inserirPublicacaoTeste(d);
            }
        }
    }


    private boolean associarAtividades(List<Atividade> atividades, Desafio d) {
        int atividades_associadas = 0;
        for(Atividade a : atividades){
            if(a_c.associarDesafio(a, d)){
                atividades_associadas++;
            } else {
                break;
            }
        }
        return atividades_associadas == atividades.size();
    }

    private boolean inserirMetas(Desafio d, Map<Long, Premiacao> premiacoes) {
        int n_metas = d.getAtividades().size();
        int n_inseridas = 0;
        a_c = getAtividadeController();
        Map<Long, Atividade> atividades = a_c.getAtividades();
        for(Atividade a : d.getAtividades()){
            if(!atividades.containsKey(a.getId())){
                if(a.getPremiacao() != null || a.getId_premiacao() > 0){
                    //Vendo se existe a premiação na lista
                    if(!premiacoes.containsKey(a.getId_premiacao())){
                        if(p_c.inserirPremiacao(premiacoes.get(a.getId_premiacao()))){
                            premiacoes.put(a.getPremiacao().getId(), a.getPremiacao());
                        } else {
                            break;
                        }
                    }
                    if(premiacoes.containsKey(a.getId_premiacao())){
                        if(a_c.inserir(a)) n_inseridas++;
                        else break;
                    }
                } else {
                    break;
                }
            } else {
                n_inseridas++;
            }
        }
        return n_inseridas >= n_metas;
    }
    private AtividadeController getAtividadeController() {
        if(a_c == null)
            a_c = new AtividadeController(context);
        return a_c;
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
                        p = PublicacaoDAO.get(c);
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
            i_d.setPublicacao(p);
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
                    p = PublicacaoDAO.get(c);
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
                    desafio.setPublicacao(p);
                }
                if(p != null){
                    InteracaoDesafio i_d = getInteracaoPublicacaoAtual(p, desafio);
                    desafio.setInteracao_desafio(i_d);
                    i_d.setPublicacao(p);
                }
                desafio.setMetas(getMetasDesafio(desafio));
            }
        };
        getSelectQueryContent(select, d_b);
        return d_b.desafio;
    }

    private Publicacao getPublicacaoAnterior(Desafio d) {
        Publicacao p = null;
        if(d != null){
            String select =
                    "SELECT * FROM " + PublicacaoDAO.TABLE_NAME + " " +
                    "WHERE " + PublicacaoDAO.ID_DESAFIO + " = " + String.valueOf(d.getId()) + " " +
                    "GROUP BY " + PublicacaoDAO.ID_DESAFIO + " " +
                    "HAVING " + PublicacaoDAO.ID + " = MAX(" + PublicacaoDAO.ID + ");";
            Cursor c = executeSelect(select);
            try {
                if(c.moveToFirst()){
                    do{
                        p = PublicacaoDAO.get(c);
                    }while(c.moveToNext());
                }
            } catch (SQLiteException e){
                imprimeErroSQLite(e);
            }
        }
        return p;
    }

    public Set<Long> getDesafios(){
        Set<Long> desafios = d_dao.getDesafios();
        return desafios;
    }

    public Map<Long, Desafio> getMapDesafios(){
        return d_dao.getMapDesafios();
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
                        Publicacao p = PublicacaoDAO.get(c);
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
        AtividadeController a_c = new AtividadeController(context);
        metas.addAll(a_c.getAtividades(d).values());
        return metas;
    }
}
