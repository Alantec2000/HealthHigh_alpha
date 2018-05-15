package google.com.healthhigh.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.activities.QuestionarioActivity;
import google.com.healthhigh.controller.QuestionarioController;
import google.com.healthhigh.domain.Questionario;
import google.com.healthhigh.utils.MessageDialog;

/**
 * Created by Alan on 22/10/2017.
 */

public class QuestionarioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    final TextView titulo, descricao, data_criacao, status_publicacao, status_visualizacao;
    final Button responder;
    private Questionario questionario;
    private Context context;

    public QuestionarioViewHolder(View v) {
        super(v);
        titulo = (TextView) v.findViewById(R.id.txt_titulo);
        descricao = (TextView) v.findViewById(R.id.txt_descricao);
        responder = (Button) v.findViewById(R.id.btn_responder);
        data_criacao = (TextView) v.findViewById(R.id.txt_data_criacao_noticia);
        status_publicacao = (TextView) v.findViewById(R.id.txt_status_publicacao_desafio);
        status_visualizacao = (TextView) v.findViewById(R.id.txt_data_criacao_noticia);
        context = v.getContext();
        responder.setOnClickListener(this);
    }

    public TextView getData_criacao() {
        return data_criacao;
    }

    public TextView getStatus_publicacao() {
        return status_publicacao;
    }

    public TextView getStatus_visualizacao() {
        return status_visualizacao;
    }

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    public TextView getTitulo() {
        return titulo;
    }

    public TextView getDescricao() {
        return descricao;
    }

    public Button getResponder() {
        return responder;
    }

    public Context getContext() {
        return context;
    }

    private void abrirQuestionario(View v){
        if(questionario != null){
            QuestionarioController qc = new QuestionarioController(context);
            Intent i = new Intent(context, QuestionarioActivity.class);
            i.putExtra(QuestionarioActivity.QUESTIONARIO_ID, questionario.getId());
            if(questionario.getDesafio_atual() != null){
                i.putExtra("desafio_id", questionario.getDesafio_atual().getId());
            } else {
                i.putExtra("desafio_id", 0);
            }
            context.startActivity(i);
        } else {
            MessageDialog.showMessage(v.getContext(), "Erro ao abrir questionário!", "Erro Crítico!");
        }
    }

    @Override
    public void onClick(View v) {
        abrirQuestionario(v);
        /*switch (v.getId()){
            case R.id.btn_responder:
                abrirQuestionario(v);
            break;
            case R.id.btn_detalhes:
                obterDetalhesQuestionario(v);
            break;
        }*/
    }

    private void obterDetalhesQuestionario(View v) {
        QuestionarioController qc = new QuestionarioController(context);
        Intent i = new Intent(context, QuestionarioActivity.class);
        i.putExtra(QuestionarioActivity.QUESTIONARIO_ID, questionario.getId());
        if(questionario.getDesafio_atual() != null){
            i.putExtra("desafio_id", questionario.getDesafio_atual().getId());
        } else {
            i.putExtra("desafio_id", 0);
        }
        context.startActivity(i);
    }
}
