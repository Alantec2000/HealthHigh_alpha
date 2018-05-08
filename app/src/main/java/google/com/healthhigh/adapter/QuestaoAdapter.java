package google.com.healthhigh.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.healthhigh.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import google.com.healthhigh.domain.Questao;
import google.com.healthhigh.domain.QuestaoAlternativa;
import google.com.healthhigh.domain.QuestaoOpinativa;
import google.com.healthhigh.domain.QuestaoOptativa;
import google.com.healthhigh.domain.RespostaOpinativa;
import google.com.healthhigh.domain.TipoQuestao;
import google.com.healthhigh.utils.Toaster;
import google.com.healthhigh.viewholders.QuestaoAlternativaViewHolder;
import google.com.healthhigh.viewholders.QuestaoOpinativaViewHolder;
import google.com.healthhigh.viewholders.QuestaoOptativaViewHolder;
import google.com.healthhigh.viewholders.QuestaoViewHolder;

/**
 * Created by Alan on 22/10/2017.
 */

public class QuestaoAdapter extends RecyclerView.Adapter {
    private List<TipoQuestao> questoes = new ArrayList<>();
    private Context context;

    public QuestaoAdapter(Context c, List<TipoQuestao> q){
        context = c;
        questoes = q;
    }

    @Override
    public int getItemViewType(int position) {
        int tipo = 0;
        tipo = questoes.get(position).getTipoQuestao();
        return tipo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder q_holder;
        switch (viewType){
            case QuestaoAlternativa.QUESTAO_ALTERNATIVA:
                view = LayoutInflater.from(context).inflate(R.layout.layout_questao_alternativa, parent, false);
                q_holder = new QuestaoAlternativaViewHolder(view);
            break;
            case QuestaoOptativa.QUESTAO_OPTATIVA:
                view = LayoutInflater.from(context).inflate(R.layout.layout_questao_optativa, parent, false);
                q_holder = new QuestaoOptativaViewHolder(view);
            break;
            case QuestaoOpinativa.QUESTAO_OPINATIVA:
                view = LayoutInflater.from(context).inflate(R.layout.layout_questao_opinativa, parent, false);
                q_holder = new QuestaoOpinativaViewHolder(view);
            break;
            default:
                view = LayoutInflater.from(context).inflate(R.layout.layout_questao_ambas_questionario, parent, false);
                q_holder = new QuestaoViewHolder(view);
        }
        return q_holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TipoQuestao q = questoes.get(position);
        switch (q.getTipoQuestao()){
            case QuestaoAlternativa.QUESTAO_ALTERNATIVA:
                getQAltViewHolder(holder, (QuestaoAlternativa) q);
                break;
            case QuestaoOpinativa.QUESTAO_OPINATIVA:
                getQOpnViewHolder(holder, (QuestaoOpinativa) q);
                break;
            case QuestaoOptativa.QUESTAO_OPTATIVA:
                getQOptViewHolder(holder, (QuestaoOptativa) q);
                break;
        }
        if(position %2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
    }

    private RecyclerView.ViewHolder getQAltViewHolder(RecyclerView.ViewHolder holder, QuestaoAlternativa qa){
        QuestaoAlternativaViewHolder qa_holder = (QuestaoAlternativaViewHolder) holder;
        qa_holder.getDescricao().setText(qa.getDescricao());
        qa_holder.setQuestaoAlternativa(qa);
        qa_holder.setAlternativas_map(qa.getAlternativas());
        return holder;
    }

    private RecyclerView.ViewHolder getQOptViewHolder(RecyclerView.ViewHolder holder, QuestaoOptativa qopt){
        QuestaoOptativaViewHolder qopt_holder = (QuestaoOptativaViewHolder) holder;
        qopt_holder.getTxt_descricao().setText(qopt.getDescricao());
        qopt_holder.setQuestaoOptativa(qopt);
        qopt_holder.setResposta();
        return holder;
    }

    private RecyclerView.ViewHolder getQOpnViewHolder(RecyclerView.ViewHolder holder, QuestaoOpinativa qopn){
        QuestaoOpinativaViewHolder qopn_holder = (QuestaoOpinativaViewHolder) holder;
        qopn_holder.getTxt_dsc_questao().setText(qopn.getDescricao());
        qopn_holder.setQuestaoOpinativa(qopn);
        return holder;
    }

    @Override
    public int getItemCount() {
        return questoes.size();
    }
}
