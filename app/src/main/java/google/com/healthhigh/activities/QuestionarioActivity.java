package google.com.healthhigh.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.healthhigh.R;

import java.util.List;

import google.com.healthhigh.adapter.QuestaoAdapter;
import google.com.healthhigh.base.Base;
import google.com.healthhigh.controller.QuestionarioController;
import google.com.healthhigh.domain.Questionario;
import google.com.healthhigh.domain.TipoQuestao;
import google.com.healthhigh.utils.MessageDialog;

public class QuestionarioActivity extends Base implements View.OnClickListener {
    public static String QUESTIONARIO_ID = "ID_QUESTIONARIO";
    private RecyclerView rv = null;
    private Questionario questionario;
    private boolean lista_questao = false;
    QuestionarioController questionario_controller;
    Intent self_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_questionario);
        self_intent = this.getIntent();
        Button btn_resposta = (Button) findViewById(R.id.btn_responder_questionario);
        btn_resposta.setOnClickListener(this);
        questionario_controller = new QuestionarioController(this);
        obterQuestionario();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void obterQuestionario() {
        long id_questionario = self_intent.getLongExtra(QUESTIONARIO_ID, 0);
        if(id_questionario > 0){
            questionario = questionario_controller.getQuestionario(id_questionario);
            if(questionario != null){
                if(questionario.getDesafio_atual() != null){
                    setQuestoes(questionario.getL_questoes());
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Este questionário associado ao desafio atual!")
                            .setTitle("Erro")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            } else {
                MessageDialog.showMessage(this, "Questionário não foi encontrado!", "Erro ao obter questionário");
            }
        } else {
            MessageDialog.showMessage(this, "Nenhum questionário informado!", "Erro ao obter questionário");
        }
    }

    private void setQuestoes(List<TipoQuestao> questoes) {
        rv = (RecyclerView) findViewById(R.id.lista_questoes_responder_questionario);
        try{
            rv.setAdapter(new QuestaoAdapter(this, questoes));
            rv.setLayoutManager(new LinearLayoutManager(this));
            lista_questao = true;
        } catch (Exception e){
            Log.e("lista_questao", e.getMessage());
            lista_questao = false;
        } finally {
            if(lista_questao && questionario.getData_visualizacao() <= 0){
                questionario_controller.setVisualizou(questionario);
            }
            if(questionario.getInteracao_questionario() != null && questionario.getInteracao_questionario().getData_visualizacao() <= 0){
                questionario_controller.setVisualizou(questionario.getInteracao_questionario());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_responder_questionario:
                boolean respondido = questionario_controller.validar_respostas(questionario);
                boolean respostas_armazenadas = false;
                if(questionario.getInteracao_questionario() != null){
                    questionario_controller.atualizarInicio(questionario.getInteracao_questionario());
                }
                if(respondido){
                    respostas_armazenadas = questionario_controller.atualizaRespostasQuestionario(questionario);
                    if(respostas_armazenadas) {
                        if(questionario.getInteracao_questionario() != null){
                            questionario_controller.atualizarConclusao(questionario.getInteracao_questionario());
                        }
                        MessageDialog.showMessage(this, "Questionário foi respondido com sucesso!", "Sucesso!");
                    }
                }
                break;
        }
    }
}
