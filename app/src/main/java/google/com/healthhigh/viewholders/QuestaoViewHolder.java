package google.com.healthhigh.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.domain.TipoQuestao;

/**
 * Created by Alan on 22/10/2017.
 */

public class QuestaoViewHolder extends RecyclerView.ViewHolder {
    private final TextView descricao;
    private final RadioGroup opcoes_quantitativas;
    private final EditText resposta_qualitativa;
    private final View view;
    private TipoQuestao questao;

    public QuestaoViewHolder(View v) {
        super(v);
        view = v;
        descricao = (TextView) v.findViewById(R.id.txt_descricao_questao_opinativa);
        opcoes_quantitativas = (RadioGroup) v.findViewById(R.id.rg_opcoes_questao);
        resposta_qualitativa = (EditText) v.findViewById(R.id.resposta_qualitativa_unidade_questao);
    }

    public View getView() {
        return view;
    }

    public TextView getDescricao() {
        return descricao;
    }

    public RadioGroup getOpcoes_quantitativas() {
        return opcoes_quantitativas;
    }

    public EditText getResposta_qualitativa() {
        return resposta_qualitativa;
    }

    public void setQuestao(TipoQuestao questao) {
        this.questao = questao;
    }

    public TipoQuestao getQuestao() {
        return questao;
    }
}
