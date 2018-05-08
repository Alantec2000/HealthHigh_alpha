package google.com.healthhigh.domain;

/**
 * Created by Alan on 19/04/2018.
 */

public class RespostaOptativa extends TipoResposta {
    private QuestaoOptativa questao;
    private boolean opcao;
    @Override
    protected TipoQuestao obterQuestaoRespostas() {
        return questao;
    }

    public boolean getOpcao() {
        return opcao;
    }

    public void setOpcao(boolean opcao) {
        this.opcao = opcao;
    }

    public QuestaoOptativa getQuestao() {
        return questao;
    }

    public void setQuestao(QuestaoOptativa questao) {
        this.questao = questao;
    }
}
