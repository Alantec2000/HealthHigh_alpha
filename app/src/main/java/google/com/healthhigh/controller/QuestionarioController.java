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

import google.com.healthhigh.dao.AlternativaDAO;
import google.com.healthhigh.dao.DAO;
import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.dao.DesafioQuestionarioDAO;
import google.com.healthhigh.dao.InteracaoQuestionarioDAO;
import google.com.healthhigh.dao.PublicacaoDAO;
import google.com.healthhigh.dao.QuestaoAlternativaDAO;
import google.com.healthhigh.dao.QuestaoOpinativaDAO;
import google.com.healthhigh.dao.QuestaoOptativaDAO;
import google.com.healthhigh.dao.QuestionarioDAO;
import google.com.healthhigh.dao.QuestionarioQuestaoAlternativaDAO;
import google.com.healthhigh.dao.QuestionarioQuestaoOpinativaDAO;
import google.com.healthhigh.dao.Questionario_QuestaoAlternativaDAO;
import google.com.healthhigh.dao.Questionario_QuestaoOptativaDAO;
import google.com.healthhigh.dao.RespostaAlternativaDAO;
import google.com.healthhigh.dao.RespostaOpinativaDAO;
import google.com.healthhigh.dao.RespostaOptativaDAO;
import google.com.healthhigh.dao.TipoQuestaoDAO;
import google.com.healthhigh.domain.Alternativa;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.domain.InteracaoDesafio;
import google.com.healthhigh.domain.InteracaoQuestionario;
import google.com.healthhigh.domain.Publicacao;
import google.com.healthhigh.domain.QuestaoAlternativa;
import google.com.healthhigh.domain.QuestaoOpinativa;
import google.com.healthhigh.domain.QuestaoOptativa;
import google.com.healthhigh.domain.Questionario;
import google.com.healthhigh.domain.RespostaAlternativa;
import google.com.healthhigh.domain.RespostaOpinativa;
import google.com.healthhigh.domain.RespostaOptativa;
import google.com.healthhigh.domain.TipoQuestao;
import google.com.healthhigh.utils.DataHelper;
import google.com.healthhigh.utils.MessageDialog;

/**
 * Created by Alan on 19/04/2018.
 */

public class QuestionarioController extends DAO {
    private TipoQuestaoDAO tq_dao;
    private InteracaoQuestionarioDAO i_q_dao;
    private QuestionarioDAO q_dao;
    public QuestionarioController(Context context) {
        super(context);
        tq_dao = new TipoQuestaoDAO(context);
        q_dao = new QuestionarioDAO(context);
        i_q_dao = new InteracaoQuestionarioDAO(context);
    }

    @Override
    protected void prepareContentReceiver() {}

    public static Questionario getQuestionario(Cursor c){
        Questionario q = new Questionario();
        q.setId(c.getLong(c.getColumnIndex(QuestionarioDAO.ID)));
        q.setDescricao(c.getString(c.getColumnIndex(QuestionarioDAO.DESCRICAO)));
        q.setTitulo(c.getString(c.getColumnIndex(QuestionarioDAO.TITULO)));
        q.setData_criacao(c.getLong(c.getColumnIndex(QuestionarioDAO.DATA_CRIACAO)));
        q.setData_visualizacao(c.getLong(c.getColumnIndex(QuestionarioDAO.DATA_VISUALIZACAO)));
        return q;
    }

    public Questionario getQuestionario(long id){
        Questionario q = new Questionario();
        if(id > 0){
            List<Questionario> questionarios = this.getQuestionarios(id);
            for(Questionario q_aux : questionarios){
                if(q_aux.getId() == id){
                    q = q_aux;
                    break;
                }
            }
        }
        return q;
    }

    public List<Questionario> getQuestionarios(long id_questionario) {
        List<Questionario> questionarios = new ArrayList<>();
        String select =
                "SELECT * FROM " + QuestionarioDAO.TABLE_NAME + " as q " +
                // Trecho que verifica se o questionario está associado com um desafio
                " LEFT JOIN " + DesafioQuestionarioDAO.TABLE_NAME + " as dq ON " +
                    "dq." + DesafioQuestionarioDAO.ID_QUESTIONARIO + " = q." + QuestionarioDAO.ID +
                " LEFT JOIN " + DesafioDAO.TABLE_NAME + " as d ON " +
                    " d." + DesafioDAO.ID + " = dq." + DesafioQuestionarioDAO.ID_DESAFIO;
        if(id_questionario > 0){
            select += " WHERE q." + QuestionarioDAO.ID + " = " + String.valueOf(id_questionario) + ";";
        } else {
            select += ";";
        }
        questionarios = getQuestionariosCursor(select);
        return questionarios;
    }

    private List<Questionario> getQuestionariosCursor(String select) {
        Map<Long, Questionario> questionarioMap = new TreeMap<>();
        List<Questionario> questionarios;
        DesafioController d_c = new DesafioController(this.context);
        Desafio d_a = d_c.getDesafioAtual();
        try (Cursor c = executeSelect(select)) {
            if (c.moveToFirst()) {
                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                do {
                    long id_q = c.getLong(c.getColumnIndex(QuestionarioDAO.ID));
                    Questionario q;
                    if (questionarioMap.containsKey(id_q)) {
                        q = questionarioMap.get(id_q);
                    } else {
                        q = getQuestionario(c);
                        questionarioMap.put(id_q, q);
                    }
                    if(d_a != null){
                        Map<Long, Desafio> d_as = d_c.getDesafiosAssociados(q);
                        if(d_as.containsKey(d_a.getId())) {
                            q.setDesafio_atual(d_a);
                        }
                        if(d_a.getInteracao_desafio() != null && d_a.getInteracao_desafio().isRealizando_no_momento()){
                            InteracaoQuestionario i_q = getInteracaoQuestionarioAtual(q, d_a.getInteracao_desafio());
                            q.setInteracao_questionario(i_q);
                        }
                        q.setDesafios_associados(d_as);
                    }

                    // Bloco obtém as questões do questionário se elas ainda não foram obtidas
                    if (q.getL_questoes() == null) {
                        q.setL_questoes(new ArrayList<TipoQuestao>());
                        Map<Long, QuestaoAlternativa> questoes_alternativa = getQuestaoAlternativaCursor(q);
                        Map<Long, QuestaoOptativa> questoes_optativas = getQuestaoOptativaCursor(q);
                        Map<Long, QuestaoOpinativa> questoes_opinativas = getQuestaoOpinativaCursor(q);

                        List<TipoQuestao> questoes = q.getL_questoes();
                        for (Long id_qa : questoes_alternativa.keySet()) {
                            questoes.add(questoes_alternativa.get(id_qa));
                        }
                        for (Long id_qopt : questoes_optativas.keySet()) {
                            questoes.add(questoes_optativas.get(id_qopt));
                        }
                        for (Long id_qopn : questoes_opinativas.keySet()) {
                            questoes.add(questoes_opinativas.get(id_qopn));
                        }
                    }
                } while (c.moveToNext());
            }
        } catch (SQLiteException e) {
            imprimeErroSQLite(e);
        }
        questionarios = new ArrayList<>(questionarioMap.values());
        return questionarios;
    }

    private InteracaoQuestionario insereNovaInteracaoVazia(Questionario q, InteracaoDesafio i_d) {
        InteracaoQuestionario i_q = new InteracaoQuestionario();
        InteracaoQuestionarioDAO i_q_dao = new InteracaoQuestionarioDAO(context);
        ContentValues cv = new ContentValues();
        cv.put(InteracaoQuestionarioDAO.ID_PUBLICACAO, i_d.getPublicacao().getId());
        cv.put(InteracaoQuestionarioDAO.ID_QUESTIONARIO, q.getId());
        cv.put(InteracaoQuestionarioDAO.DATA_CRIACAO, DataHelper.now());
        i_q_dao.insereInteracaoQuestionario(cv, i_q);
        return i_q;
    }

    private InteracaoQuestionario getInteracaoQuestionarioAtual(Questionario q, InteracaoDesafio i_d) {
        InteracaoQuestionario i_q = null;
        if(i_d != null && i_d.getId() > 0){
            String select =
                    "SELECT * FROM " + InteracaoQuestionarioDAO.TABLE_NAME +
                    " WHERE " + InteracaoQuestionarioDAO.ID_PUBLICACAO + " = " + String.valueOf(i_d.getPublicacao().getId()) +
                    " AND " + InteracaoQuestionarioDAO.ID_QUESTIONARIO + " = " + String.valueOf(q.getId());
            Cursor c = executeSelect(select);
            if(c.getCount() > 0){
                try {
                    if (c.moveToFirst()) {
                        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                        do {
                            i_q = new InteracaoQuestionario();
                            i_q = InteracaoQuestionarioDAO.getInteracaoQuestionario(c);
                            i_q.setInteracao_desafio(i_d);
                            i_q.setQuestionario(q);
                        }while (c.moveToNext());
                    }
                } catch (SQLiteException e) {
                    imprimeErroSQLite(e);
                }
            } else {
                i_q = insereNovaInteracaoVazia(q,i_d);
            }
        }
        return i_q;
    }

    private Map<Long, QuestaoOptativa> getQuestaoOptativaCursor(Questionario q) {
        Map<Long, QuestaoOptativa> questoes_optativas = new TreeMap<>();
        String inner_join_condition = "";
        if(q.getDesafio_atual() != null && q.getDesafio_atual().getPublicacao() != null){
            inner_join_condition = " AND ropt." + RespostaOptativaDAO.ID_PUBLICACAO + " = " + String.valueOf(q.getDesafio_atual().getPublicacao().getId());
        }
        String select_questoes =
                "SELECT * FROM " + QuestaoOptativaDAO.TABLE_NAME + " as qopt " +
                        " INNER JOIN " + Questionario_QuestaoOptativaDAO.TABLE_NAME + " as qqopt ON " +
                        " qqopt." + Questionario_QuestaoOptativaDAO.ID_QUESTAO + " = qopt." + QuestaoOptativaDAO.ID +
                        " LEFT JOIN " + RespostaOptativaDAO.TABLE_NAME + " as ropt ON " +
                        " ropt." + RespostaOptativaDAO.ID_QUESTAO + " = qopt." + QuestaoOptativaDAO.ID +
                        " WHERE qqopt." + Questionario_QuestaoOptativaDAO.ID_QUESTIONARIO + " = " + String.valueOf(q.getId());
        try (Cursor c_opt = executeSelect(select_questoes)) {
            if (c_opt.moveToFirst()) {
                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c_opt));
                do {
                    long id_qopt = c_opt.getLong(c_opt.getColumnIndex(QuestaoOptativaDAO.ID));
                    QuestaoOptativa qopt;
                    if (questoes_optativas.containsKey(id_qopt)) {
                        qopt = questoes_optativas.get(id_qopt);
                    } else {
                        qopt = new QuestaoOptativa();
                        qopt.setId(id_qopt);
                        qopt.setDescricao(c_opt.getString(c_opt.getColumnIndex(QuestaoOptativaDAO.DESCRICAO)));
                        qopt.setData_criacao(c_opt.getLong(c_opt.getColumnIndex(QuestaoOptativaDAO.DATA_CRIACAO)));
                        qopt.setQuestionario(q);
                        questoes_optativas.put(qopt.getId(), qopt);
                    }
                    if (!c_opt.isNull(c_opt.getColumnIndex(RespostaOptativaDAO.ID))) {
                        RespostaOptativa r = new RespostaOptativa();
                        r.setId(c_opt.getLong(c_opt.getColumnIndex(RespostaOptativaDAO.ID)));
                        r.setOpcao(c_opt.getShort(c_opt.getColumnIndex(RespostaOptativaDAO.OPCAO)) > 0);
                        r.setData_de_resposta(c_opt.getLong(c_opt.getColumnIndex(RespostaOptativaDAO.DATA_CRIACAO)));
                        r.setQuestao(qopt);
                        if(q.getDesafio_atual() != null){
                            r.setPublicacao(q.getDesafio_atual().getPublicacao());
                        }
                        qopt.setResposta(r);
                    }
                } while (c_opt.moveToNext());
            }
        } catch (SQLiteException e) {
            imprimeErroSQLite(e);
        }
        return questoes_optativas;
    }

    private Map<Long, QuestaoAlternativa> getQuestaoAlternativaCursor(Questionario q) {
        Map<Long, QuestaoAlternativa> questoes_alternativa = new TreeMap<>();
        String inner_join_condition = "";
        if(q.getDesafio_atual() != null && q.getDesafio_atual().getPublicacao() != null){
            inner_join_condition = " AND ra." + RespostaAlternativaDAO.ID_PUBLICACAO + " = " + String.valueOf(q.getDesafio_atual().getPublicacao().getId());
        }
        String select_questoes =
                "SELECT * FROM " + QuestaoAlternativaDAO.TABLE_NAME + " as qa " +
                        " INNER JOIN " + Questionario_QuestaoAlternativaDAO.TABLE_NAME + " as qqa ON " +
                        " qa." + QuestaoAlternativaDAO.ID + " = qqa." + QuestionarioQuestaoAlternativaDAO.ID_QUESTAO +
                        " INNER JOIN " + QuestionarioDAO.TABLE_NAME + " q ON " +
                        " q." + QuestionarioDAO.ID + " = qqa." + QuestionarioQuestaoAlternativaDAO.ID_QUESTIONARIO +
                        " INNER JOIN " + AlternativaDAO.TABLE_NAME + " as a ON " +
                        " a." + AlternativaDAO.ID_QUESTAO + " = qa." + QuestaoAlternativaDAO.ID + " " +
                        " LEFT JOIN " + RespostaAlternativaDAO.TABLE_NAME + " ra ON " +
                            " ra." + RespostaAlternativaDAO.ID_ALTERNATIVA + " = a." + AlternativaDAO.ID + " " + inner_join_condition +
                        " WHERE qqa." + Questionario_QuestaoAlternativaDAO.ID_QUESTIONARIO + " = " + String.valueOf(q.getId());
        try (Cursor c_qa = executeSelect(select_questoes)) {
            if (c_qa.moveToFirst()) {
                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c_qa));
                do {
                    long id_qa = c_qa.getLong(c_qa.getColumnIndex(QuestaoAlternativaDAO.ID));
                    QuestaoAlternativa qa;
                    if (questoes_alternativa.containsKey(id_qa)) {
                        qa = questoes_alternativa.get(id_qa);
                    } else {
                        qa = QuestaoAlternativaDAO.getQuestaoAlternativa(c_qa, q);
                        questoes_alternativa.put(id_qa, qa);
                    }

                    Map<Long, Alternativa> alternativas = qa.getAlternativas();
                    long id_a = c_qa.getLong(c_qa.getColumnIndex(AlternativaDAO.ID));
                    Alternativa a;
                    if (alternativas.containsKey(id_a)) {
                        a = alternativas.get(id_a);
                    } else {
                        a = new Alternativa();
                        a.setId(id_a);
                        a.setDescricao(c_qa.getString(c_qa.getColumnIndex(AlternativaDAO.DESCRICAO)));
                        a.setData_criacao(c_qa.getLong(c_qa.getColumnIndex(AlternativaDAO.DATA_CRIACAO)));
                        a.setQuestaoAlternativa(qa);
                        alternativas.put(a.getId_alternativa(), a);
                    }
                    if (!c_qa.isNull(c_qa.getColumnIndex(RespostaAlternativaDAO.ID))) {
                        RespostaAlternativa r = new RespostaAlternativa();
                        r.setId(c_qa.getLong(c_qa.getColumnIndex(RespostaAlternativaDAO.ID)));
                        r.setSelecionado(c_qa.getShort(c_qa.getColumnIndex(RespostaAlternativaDAO.SELECIONADO)) > 0);
                        r.setData_de_resposta(c_qa.getLong(c_qa.getColumnIndex(RespostaAlternativaDAO.DATA_CRIACAO)));
                        r.setAlternativa(a);
                        if(q.getDesafio_atual() != null){
                            r.setPublicacao(q.getDesafio_atual().getPublicacao());
                        }
                        a.setResposta(r);
                    }
                } while (c_qa.moveToNext());
            }
        } catch (SQLiteException e) {
            imprimeErroSQLite(e);
        }

        return questoes_alternativa;
    }

    private Map<Long,QuestaoOpinativa> getQuestaoOpinativaCursor(Questionario q) {
        Map<Long, QuestaoOpinativa> questoes_opinativas = new TreeMap<>();
        String inner_join_condition = "";
        if(q.getDesafio_atual() != null && q.getDesafio_atual().getPublicacao() != null){
            inner_join_condition = " AND ropn." + RespostaOpinativaDAO.ID_PUBLICACAO + " = " + String.valueOf(q.getDesafio_atual().getPublicacao().getId());
        }
        String select_questoes =
                "SELECT * FROM " + QuestaoOpinativaDAO.TABLE_NAME + " as qopn " +
                " INNER JOIN " + QuestionarioQuestaoOpinativaDAO.TABLE_NAME + " as qqopn ON " +
                " qqopn." + QuestionarioQuestaoOpinativaDAO.ID_QUESTAO + " = qopn." + QuestaoOpinativaDAO.ID +
                " LEFT JOIN " + RespostaOpinativaDAO.TABLE_NAME + " as ropn ON " +
                " ropn." + RespostaOpinativaDAO.ID_QUESTAO + " = qopn." + QuestaoOpinativaDAO.ID + inner_join_condition +
                " WHERE qqopn." + QuestionarioQuestaoOpinativaDAO.ID_QUESTIONARIO + " = " + String.valueOf(q.getId());
        Cursor c = executeSelect(select_questoes);
        try {
            if (c.moveToFirst()) {
                Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(c));
                do {
                    long id_qopn = c.getLong(c.getColumnIndex(QuestaoOpinativaDAO.ID));
                    QuestaoOpinativa qopn;
                    if (questoes_opinativas.containsKey(id_qopn)) {
                        qopn = questoes_opinativas.get(id_qopn);
                    } else {
                        qopn = new QuestaoOpinativa();
                        qopn.setId(id_qopn);
                        qopn.setDescricao(c.getString(c.getColumnIndex(QuestaoOpinativaDAO.DESCRICAO)));
                        qopn.setData_criacao(c.getLong(c.getColumnIndex(QuestaoOpinativaDAO.DATA_CRIACAO)));
                        qopn.setQuestionario(q);
                        questoes_opinativas.put(qopn.getId(), qopn);
                    }
                    if (!c.isNull(c.getColumnIndex(RespostaOpinativaDAO.ID))) {
                        RespostaOpinativa r = new RespostaOpinativa();
                        r.setId(c.getLong(c.getColumnIndex(RespostaOpinativaDAO.ID)));
                        r.setOpiniao(c.getString(c.getColumnIndex(RespostaOpinativaDAO.OPINIAO)));
                        r.setData_de_resposta(c.getLong(c.getColumnIndex(RespostaOpinativaDAO.DATA_CRIACAO)));
                        r.setQuestao(qopn);
                        if(q.getDesafio_atual() != null){
                            r.setPublicacao(q.getDesafio_atual().getPublicacao());
                        }
                        qopn.setResposta(r);
                    }
                } while (c.moveToNext());
            }
        } catch (SQLiteException e) {
            imprimeErroSQLite(e);
        } finally {
            c.close();
        }
        return questoes_opinativas;
    }

    private RespostaOpinativa getRespostaOpinativa(Cursor c) {
        RespostaOpinativa ropn = new RespostaOpinativa();
        ropn.setId(c.getLong(c.getColumnIndex(RespostaOpinativaDAO.ID)));
        ropn.setData_de_resposta(c.getLong(c.getColumnIndex(RespostaOpinativaDAO.DATA_CRIACAO)));
        ropn.setOpiniao(c.getString(c.getColumnIndex(RespostaOpinativaDAO.OPINIAO)));
        return null;
    }

    private RespostaOptativa getRespostaOptativa(Cursor c) {
        RespostaOptativa ropt = new RespostaOptativa();
        ropt.setId(c.getLong(c.getColumnIndex(RespostaOptativaDAO.ID)));
        ropt.setData_de_resposta(c.getLong(c.getColumnIndex(RespostaOptativaDAO.DATA_CRIACAO)));
        ropt.setOpcao(c.getShort(c.getColumnIndex(RespostaOptativaDAO.OPCAO)) > 0);
        return ropt;
    }

    private Alternativa getAlternativa(Cursor c) {
        Alternativa a = new Alternativa();
        a.setData_criacao(c.getLong(c.getColumnIndex(AlternativaDAO.DATA_CRIACAO)));
        a.setId(c.getLong(c.getColumnIndex(AlternativaDAO.ID)));
        a.setDescricao(c.getString(c.getColumnIndex(AlternativaDAO.DESCRICAO)));
        return a;
    }

    public RespostaAlternativa getRespostaAlternativa(Cursor c) {
        RespostaAlternativa ra = new RespostaAlternativa();
        ra.setId(c.getLong(c.getColumnIndex(RespostaAlternativaDAO.ID)));
        ra.setData_de_resposta(c.getLong(c.getColumnIndex(RespostaAlternativaDAO.DATA_CRIACAO)));
        return ra;
    }

    public boolean validar_respostas(Questionario q) {
        boolean ret = false;
        int num_questoes = q.getL_questoes().size(), num_questoes_respondidas = 0;
        for(TipoQuestao questao : q.getL_questoes()){
            switch (questao.getTipoQuestao()){
                case QuestaoOpinativa.QUESTAO_OPINATIVA:
                    if(questao.getResposta() != null){
                        RespostaOpinativa ropn = (RespostaOpinativa) questao.getResposta();
                        if(ropn.getOpiniao() != null && !ropn.getOpiniao().trim().isEmpty()){
                            num_questoes_respondidas++;
                        }
                    } else {
                        MessageDialog.showMessage(context, "Informe uma resposta para a questão opinativa!", "Mensagem opinativa vazia!");
                    }
                    break;
                case QuestaoOptativa.QUESTAO_OPTATIVA:
                    if(questao.getResposta() != null) {
                        num_questoes_respondidas++;
                    } else {
                        RespostaOptativa respostaOptativa = new RespostaOptativa();
                        respostaOptativa.setOpcao(false);
                        respostaOptativa.setQuestao((QuestaoOptativa) questao);
                        respostaOptativa.setData_de_resposta(System.currentTimeMillis()/1000);
                        questao.setResposta((RespostaOptativa) respostaOptativa);
                    }
                    break;
                case QuestaoAlternativa.QUESTAO_ALTERNATIVA:
                    QuestaoAlternativa qa = (QuestaoAlternativa)questao;
                    if(qa.getAlternativas() != null){
                        int num_alternativas = qa.getAlternativas().size(), num_alternativas_respondidas=0;
                        for(long a_id : qa.getAlternativas().keySet()){
                           Alternativa a = qa.getAlternativas().get(a_id);
                           if(a.getResposta() == null) {
                               RespostaAlternativa ra = new RespostaAlternativa();
                               ra.setSelecionado(false);
                               ra.setData_de_resposta(System.currentTimeMillis()/1000);
                               ra.setAlternativa(a);
                               a.setResposta(ra);
                           }
                           num_alternativas_respondidas++;
                       }
                       if(num_alternativas_respondidas >= num_alternativas) num_questoes_respondidas++;
                    }
                    break;
            }
        }
        return (num_questoes_respondidas >= num_questoes);
    }

    public boolean atualizaRespostasQuestionario(Questionario q) {
        boolean no_errors = true;
        if(q.getL_questoes() != null) {
            for(TipoQuestao qao : q.getL_questoes()){
                if(qao.getTipoQuestao() == QuestaoAlternativa.QUESTAO_ALTERNATIVA){
                    QuestaoAlternativa qa = (QuestaoAlternativa) qao;
                    RespostaAlternativaDAO ra_dao = new RespostaAlternativaDAO(context);
                    for(long id : qa.getAlternativas().keySet()){
                        RespostaAlternativa ra = qa.getAlternativas().get(id).getResposta();
                        if(ra == null ) {
                            ra = new RespostaAlternativa();
                            ra.setAlternativa(qa.getAlternativas().get(id));
                            ra.setData_de_resposta(System.currentTimeMillis()/1000);
                            ra.setSelecionado(false);
                        }
                        if(ra.getId() > 0){
                            no_errors = ra_dao.atualizarRespostaAlternativa(ra);
                        } else {
                            ra_dao.inserirRespostaAlternativa(ra);
                            no_errors = (ra != null && ra.getId() > 0);
                        }
                    }
                } else {
                    if(qao.getResposta().getId() > 0){
                        no_errors = tq_dao.atualizarResposta(qao);
                    } else {
                        no_errors = tq_dao.inserirResposta(qao);
                    }
                }
                if(!no_errors) {
                    MessageDialog.showMessage(context, "Erro ao salvar suas respostas, tente novamente!", "Erro ao responder questionário!");
                    break;
                }
            }
        } else {
            MessageDialog.showMessage(context, "Erro ao obter informações sobre o questionário!", "Erro ao responder questionário!");
            no_errors = false;
        }
        return no_errors;
    }

    public void setVisualizou(Questionario q) {
        q.setData_visualizacao(DataHelper.now());
        q_dao.atualizaQuestionario(q);
    }

    public void setVisualizou(InteracaoQuestionario i_q) {
        i_q.setData_visualizacao(DataHelper.now());
        i_q_dao.atualizaQuestionario(i_q);
    }

    public void atualizarConclusao(InteracaoQuestionario interacao_questionario) {
        interacao_questionario.setData_termino(DataHelper.now());
        i_q_dao.atualizaQuestionario(interacao_questionario);
    }

    public void atualizarInicio(InteracaoQuestionario interacao_questionario) {
        interacao_questionario.setData_inicio(DataHelper.now());
        i_q_dao.atualizaQuestionario(interacao_questionario);
    }

}
