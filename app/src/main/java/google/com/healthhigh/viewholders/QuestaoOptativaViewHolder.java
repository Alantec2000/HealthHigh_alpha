package google.com.healthhigh.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.healthhigh.R;

import org.w3c.dom.Text;

import google.com.healthhigh.domain.QuestaoOptativa;
import google.com.healthhigh.domain.RespostaOptativa;

/**
 * Created by Alan on 23/04/2018.
 */

public class QuestaoOptativaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private TextView txt_descricao;
    private ToggleButton rg_opcoes;
    private QuestaoOptativa questaoOptativa;

    public QuestaoOptativaViewHolder(View v) {
        super(v);
        rg_opcoes = (ToggleButton) v.findViewById(R.id.rg_opcoes_questao);
        txt_descricao = (TextView) v.findViewById(R.id.txt_descricao_questao_optativa);
        rg_opcoes.setOnClickListener(this);
    }

    public TextView getTxt_descricao() {
        return txt_descricao;
    }

    public void setTxt_descricao(TextView txt_descricao) {
        this.txt_descricao = txt_descricao;
    }

    public ToggleButton getRg_opcoes() {
        return rg_opcoes;
    }

    public void setRg_opcoes(ToggleButton rg_opcoes) {
        this.rg_opcoes = rg_opcoes;
    }

    public QuestaoOptativa getQuestaoOptativa() {
        return questaoOptativa;
    }

    public void setQuestaoOptativa(QuestaoOptativa questaoOptativa) {
        this.questaoOptativa = questaoOptativa;
    }

    public void setResposta() {
        if(questaoOptativa != null && questaoOptativa.getResposta() != null){
            RespostaOptativa respostaOptativa = (RespostaOptativa) questaoOptativa.getResposta();
            rg_opcoes.setChecked(respostaOptativa.getOpcao());
        } else {
            RespostaOptativa respostaOptativa = new RespostaOptativa();
            respostaOptativa.setOpcao(false);
            respostaOptativa.setQuestao(questaoOptativa);
            respostaOptativa.setData_de_resposta(System.currentTimeMillis()/1000);
            questaoOptativa.setResposta(respostaOptativa);
        }
    }

    @Override
    public void onClick(View v) {
        RespostaOptativa ropt;
        if(questaoOptativa.getResposta() != null){
            ropt = (RespostaOptativa) questaoOptativa.getResposta();
        } else {
            ropt = new RespostaOptativa();
            ropt.setQuestao(questaoOptativa);
        }
        ropt.setOpcao(rg_opcoes.isChecked());
        ropt.setData_de_resposta(System.currentTimeMillis()/1000);
    }
}
