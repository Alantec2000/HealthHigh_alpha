package google.com.healthhigh.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.healthhigh.R;

import java.util.ArrayList;
import java.util.List;

import google.com.healthhigh.adapter.QuestionarioAdapter;
import google.com.healthhigh.dao.QuestionarioDAO;
import google.com.healthhigh.dao.Questionario_QuestaoAlternativaDAO;
import google.com.healthhigh.domain.Questao;
import google.com.healthhigh.domain.Questionario;
import google.com.healthhigh.utils.Toaster;

public class ListaQuestionariosFakeActivity extends AppCompatActivity implements View.OnClickListener {
    private QuestionarioDAO questionario_dao;
    public final int request_code = 1;
    private Questionario_QuestaoAlternativaDAO questionario_questao_dao;
    Button responder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_questionarios_fake);
        responder = (Button) findViewById(R.id.responder_questionario);
        responder.setOnClickListener(this);
//        questionario_dao = new QuestionarioDAO(this);
//        addQuestionarioTeste();
//        setListaQuestionario();
    }

    private void addQuestionarioTeste() {
        questionario_questao_dao = new Questionario_QuestaoAlternativaDAO(this);
        for(int i = 1; i <= 3 ; i++){
            Questionario q = new Questionario();
            q.setTitulo("Questionario " + i);
            q.setDescricao("Questionario para saber se você é saudável.");
            List<Questao> q_l = new ArrayList<>();
            for(int x = 1; x <= 3 ; x++){
                Questao qs = new Questao();
                qs.setTipo(x);
                qs.setDescricao("Você gosta de salada?");
                qs.setQuestionario(q);
                q_l.add(qs);
            }
            q.setQuestoes(q_l);
            questionario_questao_dao.insertQuestaoInQuestionario(q);
        }
    }

    private void setListaQuestionario() {
        RecyclerView rv = (RecyclerView) findViewById(R.id.lista_questionarios);
        List<Questionario> questionarios = questionario_dao.getQuestionarios(0);
        rv.setAdapter(new QuestionarioAdapter(this, questionarios));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(this, QuestionarioFakeActivity.class), request_code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == request_code){
            if(resultCode == Activity.RESULT_OK) {
                int count = 0;
                String q1 = data.getStringExtra("resposta_quantitativa_1");
                count = !q1.isEmpty() ? (count+1) : count;
                q1 = data.getStringExtra("resposta_quantitativa_2");
                count = !q1.isEmpty() ? (count+1) : count;
                q1 = data.getStringExtra("resposta_qualitativa_1");
                count = !q1.isEmpty() ? (count+1) : count;
                if(count == 3){
                    responder.setEnabled(false);
                    Toaster.toastLongMessage(this, "Enviando respostas ao servidor...");
                    Toaster.toastShortMessage(this, "Questionário enviado ao servidor com sucesso!");
                    responder.setText("Questionário respondido!");
                } else if(count > 0){
                    responder.setText("Continuar respondendo");
                }
            }
        }
    }
}

