package google.com.healthhigh.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Alan on 19/04/2018.
 */

public class QuestaoAlternativa extends TipoQuestao {
    public static final int QUESTAO_ALTERNATIVA = 1;
    private Map<Long, Alternativa> alternativas;

    public QuestaoAlternativa() {
        this.alternativas = new TreeMap<>();
    }

    protected List<TipoResposta> obterRespostasQuestao() {
        List<TipoResposta> resposta = null;
        for(Long a : alternativas.keySet()){
            resposta.add(alternativas.get(a).getResposta());
        }
        return resposta;
    }

    @Override
    public int getTipoQuestao() {
        return QUESTAO_ALTERNATIVA;
    }

    @Override
    protected TipoResposta obterRespostaQuestao() {
        List<TipoResposta> resposta = obterRespostasQuestao();
        return resposta.get(resposta.size());
    }

    public Map<Long, Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(Map<Long, Alternativa> alternativas) {
        this.alternativas = alternativas;
    }
}
