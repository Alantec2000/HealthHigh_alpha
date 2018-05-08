package google.com.healthhigh.domain;

import java.util.List;

/**
 * Created by Alan on 19/04/2018.
 */

public class QuestaoOpinativa extends TipoQuestao {
    public static final int QUESTAO_OPINATIVA = 3;
    private RespostaOpinativa resposta;

    @Override
    public int getTipoQuestao() {
        return QUESTAO_OPINATIVA;
    }

    @Override
    protected TipoResposta obterRespostaQuestao() {
        return resposta;
    }

    public void setResposta(RespostaOpinativa resposta) {
        this.resposta = resposta;
    }
}
