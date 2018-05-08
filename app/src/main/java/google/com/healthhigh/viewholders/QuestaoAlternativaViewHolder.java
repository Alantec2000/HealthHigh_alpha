package google.com.healthhigh.viewholders;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.healthhigh.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.adapter.AlternativaAdapter;
import google.com.healthhigh.domain.Alternativa;
import google.com.healthhigh.domain.QuestaoAlternativa;

/**
 * Created by Alan on 22/04/2018.
 */

public class QuestaoAlternativaViewHolder extends RecyclerView.ViewHolder {
    private View v;
    private TextView descricao;
    private RecyclerView alternativas;
    private QuestaoAlternativa questaoAlternativa;
    public QuestaoAlternativaViewHolder(View v) {
        super(v);
        descricao = (TextView) v.findViewById(R.id.txt_dsc_questao_alternativa);
        alternativas = (RecyclerView) v.findViewById(R.id.rv_lista_alternativas);
    }

    public TextView getDescricao() {
        return descricao;
    }

    public void setDescricao(TextView descricao) {
        this.descricao = descricao;
    }

    public RecyclerView getAlternativas() {
        return alternativas;
    }

    public Map<Long, Alternativa> getAlternativas_map() {
        return questaoAlternativa.getAlternativas();
    }

    public void setAlternativas_map(Map<Long, Alternativa> alternativas_map) {
        this.questaoAlternativa.setAlternativas(alternativas_map);
        List<Alternativa> alternativas = new ArrayList<Alternativa>(alternativas_map.values());
        AlternativaAdapter a_adapter = new AlternativaAdapter(this.itemView.getContext(), alternativas);
        this.alternativas.setAdapter(a_adapter);
        this.alternativas.setLayoutManager(new LinearLayoutManager(this.itemView.getContext()));
    }

    public QuestaoAlternativa getQuestaoAlternativa() {
        return questaoAlternativa;
    }

    public void setQuestaoAlternativa(QuestaoAlternativa questaoAlternativa) {
        this.questaoAlternativa = questaoAlternativa;
    }

    public void atualizar_alternativas() {

    }
}
