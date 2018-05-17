package google.com.healthhigh.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.healthhigh.R;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.adapter.QuestionarioAdapter;
import google.com.healthhigh.controller.QuestionarioController;
import google.com.healthhigh.dao.AlternativaDAO;
import google.com.healthhigh.dao.QuestaoAlternativaDAO;
import google.com.healthhigh.dao.QuestaoOpinativaDAO;
import google.com.healthhigh.dao.QuestaoOptativaDAO;
import google.com.healthhigh.dao.QuestionarioDAO;
import google.com.healthhigh.dao.QuestionarioQuestaoAlternativaDAO;
import google.com.healthhigh.dao.QuestionarioQuestaoOpinativaDAO;
import google.com.healthhigh.dao.Questionario_QuestaoOptativaDAO;
import google.com.healthhigh.domain.Alternativa;
import google.com.healthhigh.domain.QuestaoAlternativa;
import google.com.healthhigh.domain.QuestaoOpinativa;
import google.com.healthhigh.domain.QuestaoOptativa;
import google.com.healthhigh.domain.Questionario;
import google.com.healthhigh.utils.DataHelper;

public class ListaQuestionariosActivity extends AppCompatActivity {
    private QuestionarioController q_controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_questionarios);
        q_controller = new QuestionarioController(this);
//        addQuestionarioTeste();
        setListaQuestionario();
    }

    private void addQuestionarioTeste() {
        Questionario q = new Questionario();
        q.setTitulo("Questionario 1");
        q.setDescricao("Primeiro questionario de Teste");
        q.setData_criacao(DataHelper.now());
        QuestionarioDAO q_dao = new QuestionarioDAO(this);
        q_dao.insereQuestionario(q);
        if(q != null){
            inserirQOpnTeste(q);
            inserirQATeste(q);
            inserirQOptTeste(q);
        }
    }

    private void inserirQOptTeste(Questionario q) {
        QuestaoOptativa qopt = new QuestaoOptativa();
        qopt.setQuestionario(q);
        qopt.setDescricao("Primeira questão optativa! Diga Sim ou não?");
        qopt.setData_criacao(DataHelper.now());
        QuestaoOptativaDAO qopt_dao = new QuestaoOptativaDAO(this);
        qopt_dao.inserirQuestaoOptativa(qopt);
        if(qopt != null) {
            Questionario_QuestaoOptativaDAO qqopt_dao = new Questionario_QuestaoOptativaDAO(this);
            qqopt_dao.inserirQuestionarioQuestaoOptativa(qopt);
        }
    }

    private void inserirQOpnTeste(Questionario q) {
        QuestaoOpinativa qopn = new QuestaoOpinativa();
        qopn.setQuestionario(q);
        qopn.setDescricao("Primeira questão opinativa! Justifique sua resposta... pode ser qualquer uma:");
        qopn.setData_criacao(DataHelper.now());
        QuestaoOpinativaDAO qopn_dao = new QuestaoOpinativaDAO(this);
        qopn_dao.insereQuestaoOpinativa(qopn);
        if(qopn != null) {
            QuestionarioQuestaoOpinativaDAO qqopt_dao = new QuestionarioQuestaoOpinativaDAO(this);
            qqopt_dao.inserirQuestionarioQuestaoOpinativa(qopn);
        }
    }

    private void inserirQATeste(Questionario q){
        QuestaoAlternativa qa = new QuestaoAlternativa();
        qa.setQuestionario(q);
        qa.setDescricao("Primeira questão alternativa que eu faço! Quais das alternativas abaixo você irá selecionar?");
        qa.setData_criacao(DataHelper.now());
        QuestaoAlternativaDAO qa_dao = new QuestaoAlternativaDAO(this);
        qa_dao.insereQuestaoAlternativa(qa);
        if(qa != null){
            Map<Long, Alternativa> alternativas = new TreeMap<>();
            AlternativaDAO a_dao = new AlternativaDAO(this);
            for(int i = 1; i < 5; i++){
                Alternativa a = new Alternativa();
                a.setDescricao("Alternativa " + String.valueOf(i));
                a.setData_criacao(DataHelper.now());
                a.setQuestaoAlternativa(qa);
                a_dao.inserirAlternativa(a);
            }
            QuestionarioQuestaoAlternativaDAO qqa_dao = new QuestionarioQuestaoAlternativaDAO(this);
            qqa_dao.insereAssociacaoQuestaoAlternativaQuestionario(qa);
        }
    }
    private void setListaQuestionario() {
        RecyclerView rv = (RecyclerView) findViewById(R.id.lista_questionarios);
        List<Questionario> questionarios = q_controller.getQuestionarios(0);
        rv.setAdapter(new QuestionarioAdapter(this, questionarios));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);
    }
}
