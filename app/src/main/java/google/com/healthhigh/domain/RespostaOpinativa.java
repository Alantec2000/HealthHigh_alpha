package google.com.healthhigh.domain;

/**
 * Created by Alan on 19/04/2018.
 */

public class RespostaOpinativa extends TipoResposta {
    private QuestaoOpinativa questao;
    private String opiniao;

    @Override
    public TipoQuestao obterQuestaoRespostas() {
        return questao;
    }

    public QuestaoOpinativa getQuestao() {
        return questao;
    }

    public void setQuestao(QuestaoOpinativa questao) {
        this.questao = questao;
    }

    public String getOpiniao() {
        return opiniao;
    }

    public void setOpiniao(String opiniao) {
        this.opiniao = opiniao;
    }
}
