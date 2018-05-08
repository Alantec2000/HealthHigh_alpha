package google.com.healthhigh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.healthhigh.R;

import java.util.List;

import google.com.healthhigh.domain.InteracaoQuestionario;
import google.com.healthhigh.domain.Publicacao;
import google.com.healthhigh.domain.Questionario;
import google.com.healthhigh.utils.DataHelper;
import google.com.healthhigh.viewholders.QuestionarioViewHolder;

/**
 * Created by Alan on 22/10/2017.
 */

public class QuestionarioAdapter extends RecyclerView.Adapter {
    private List<Questionario> questionarios;
    private Context context;

    public QuestionarioAdapter(Context c, List<Questionario> q){
        context = c;
        questionarios = q;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_linha_questionario, parent, false);
        return new QuestionarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QuestionarioViewHolder q_h = (QuestionarioViewHolder) holder;
        Questionario q = questionarios.get(position);
        q_h.getTitulo().setText(q.getTitulo());
        q_h.getDescricao().setText(q.getDescricao());
        q_h.setQuestionario(q);
        int hide = q.getData_visualizacao() > 0 ? View.INVISIBLE : View.VISIBLE;
        q_h.getStatus_visualizacao().setVisibility(hide);
        q_h.getData_criacao().setText(DataHelper.parseUT(q.getData_criacao(), "dd/MM/yy"));
        long now = DataHelper.now();
        q_h.getResponder().setEnabled(true);
        if (q_h.getQuestionario().getDesafio_atual() != null) {
            if(q_h.getQuestionario().getDesafio_atual().getPublicacao() != null){
                Publicacao p = q_h.getQuestionario().getDesafio_atual().getPublicacao();
                InteracaoQuestionario i_q = q_h.getQuestionario().getInteracao_questionario();
                //Publicação em vigência
                if (p.getData_inicio() < now && p.getData_fim() > now) {
                    // Desafio aceito
                    if (i_q != null) {
                        q_h.getStatus_publicacao().setText(getStatusQuestionario(i_q));
                    } else {
                        q_h.getStatus_publicacao().setText("Nova Publicação");
                    }
                } else
                    // Publicação fora de vigência
                    if (p.getData_inicio() < now && p.getData_fim() < now) {
                        q_h.getStatus_publicacao().setText("Publicação Encerrada");
                        q_h.getResponder().setEnabled(false);
                    }
            } else {
                q_h.getStatus_publicacao().setText("Não publicado");
            }
        } else {
            q_h.getResponder().setEnabled(false);
            int d_count = q.getDesafios_associados() != null ? q.getDesafios_associados().size() : 0;
            q_h.getStatus_publicacao().setText( d_count + " Desafio(s) associado(s)");
        }
    }

    private String getStatusQuestionario(InteracaoQuestionario q) {
        String ret = "Indefinido";
        if(q != null){
            // Questionário visualizado
            if(q.getData_visualizacao() > 0){
                ret = "Pendente";
                if(q.getData_inicio() > 0 && q.getData_termino() <= 0){
                    ret = "Iniciado";
                } else if(q.getData_inicio() > 0 && q.getData_termino() > 0){
                    ret = "Finalizado";
                }
            } else if(q.getQuestionario().getDesafio_atual().getPublicacao().getData_fim() > DataHelper.now()) {
                ret = "Nova Publicação";
            } else {
                ret = "Encerrado";
            }
        }
        return ret;
    }

    @Override
    public int getItemCount() {
        return questionarios.size();
    }
}
