package google.com.healthhigh.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.domain.Alternativa;
import google.com.healthhigh.domain.RespostaAlternativa;
import google.com.healthhigh.utils.DataHelper;

/**
 * Created by Alan on 22/04/2018.
 */

public class AlternativaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private CheckBox cb_alternativa;
    private QuestaoAlternativaViewHolder vh_questao_alternativa;
    private Alternativa alternativa;
    public AlternativaViewHolder(View v) {
        super(v);
        cb_alternativa = (CheckBox) v.findViewById(R.id.cb_selecionar_alternativa);
        cb_alternativa.setOnClickListener(this);
    }

    public CheckBox getCb_alternativa() {
        return cb_alternativa;
    }

    public void setVh_questao_alternativa(QuestaoAlternativaViewHolder qavh) {
        vh_questao_alternativa = qavh;
    }

    public QuestaoAlternativaViewHolder getVh_questao_alternativa() {
        return vh_questao_alternativa;
    }

    public Alternativa getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(Alternativa alternativa) {
        this.alternativa = alternativa;
        if(this.alternativa.getResposta() != null){
            RespostaAlternativa resposta_alternativa = this.alternativa.getResposta();
            cb_alternativa.setChecked(resposta_alternativa.isSelecionada());

        }
    }

    @Override
    public void onClick(View v) {
        RespostaAlternativa ra;
        if(alternativa.getResposta() != null){
            ra = alternativa.getResposta();
        } else {
            ra = new RespostaAlternativa();
            ra.setAlternativa(alternativa);
            if(alternativa.getQuestaoAlternativa().getQuestionario().getDesafio_atual() != null){
                ra.setPublicacao(alternativa.getQuestaoAlternativa().getQuestionario().getDesafio_atual().getPublicacao());
            }
            alternativa.setResposta(ra);
        }
        ra.setSelecionado(cb_alternativa.isChecked());
        ra.setData_de_resposta(DataHelper.now());
    }
}
