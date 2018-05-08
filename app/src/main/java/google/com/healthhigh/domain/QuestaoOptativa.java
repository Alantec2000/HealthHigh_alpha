package google.com.healthhigh.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 19/04/2018.
 */

public class QuestaoOptativa extends TipoQuestao {
    public static final int QUESTAO_OPTATIVA = 2;
    private RespostaOptativa resposta;

    @Override
    public int getTipoQuestao() {
        return QUESTAO_OPTATIVA;
    }

    @Override
    protected TipoResposta obterRespostaQuestao() {
        return resposta;
    }

    public void setResposta(RespostaOptativa resposta) {
        this.resposta = resposta;
    }
}
