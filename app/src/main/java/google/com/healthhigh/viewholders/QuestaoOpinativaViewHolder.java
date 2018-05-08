package google.com.healthhigh.viewholders;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.domain.QuestaoOpinativa;
import google.com.healthhigh.domain.RespostaOpinativa;

/**
 * Created by Alan on 23/04/2018.
 */

public class QuestaoOpinativaViewHolder extends RecyclerView.ViewHolder {
    private TextView txt_dsc_questao;
    private EditText edtxt_opiniao;
    private QuestaoOpinativa questaoOpinativa;
    private TextWatcher text_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            RespostaOpinativa ropn;
            if(questaoOpinativa.getResposta() != null){
                ropn = (RespostaOpinativa) questaoOpinativa.getResposta();
            } else {
                ropn = new RespostaOpinativa();
                ropn.setQuestao(questaoOpinativa);
                if(questaoOpinativa.getQuestionario().getDesafio_atual() != null){
                    ropn.setPublicacao(questaoOpinativa.getQuestionario().getDesafio_atual().getPublicacao());
                }
                questaoOpinativa.setResposta(ropn);
            }
            ropn.setOpiniao(s.toString());
            ropn.setData_de_resposta(System.currentTimeMillis()/1000);
        }
    };
    public QuestaoOpinativaViewHolder(View v) {
        super(v);
        txt_dsc_questao = (TextView) v.findViewById(R.id.txt_descricao_questao_opinativa);
        edtxt_opiniao = (EditText) v.findViewById(R.id.resposta_qualitativa_unidade_questao);
        edtxt_opiniao.addTextChangedListener(text_watcher);
    }

    public TextView getTxt_dsc_questao() {
        return txt_dsc_questao;
    }

    public void setTxt_dsc_questao(TextView txt_dsc_questao) {
        this.txt_dsc_questao = txt_dsc_questao;
    }

    public EditText getEdtxt_opiniao() {
        return edtxt_opiniao;
    }

    public void setEdtxt_opiniao(EditText edtxt_opiniao) {
        this.edtxt_opiniao = edtxt_opiniao;
    }

    public QuestaoOpinativa getQuestaoOpinativa() {
        return questaoOpinativa;
    }

    public void setQuestaoOpinativa(QuestaoOpinativa questaoOpinativa) {
        this.questaoOpinativa = questaoOpinativa;
        if(questaoOpinativa.getResposta() != null){
            RespostaOpinativa resposta = (RespostaOpinativa) questaoOpinativa.getResposta();
            this.edtxt_opiniao.setText(resposta.getOpiniao());
        }
    }
}
