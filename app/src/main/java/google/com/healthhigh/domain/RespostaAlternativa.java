package google.com.healthhigh.domain;

/**
 * Created by Alan on 19/04/2018.
 */

public class RespostaAlternativa extends TipoResposta {
    private Alternativa alternativa;
    private boolean selecionado;
    @Override
    protected TipoQuestao obterQuestaoRespostas() {
        return alternativa.getQuestaoAlternativa();
    }

    public Alternativa getAlternativa() {
        return alternativa;
    }

    public void setAlternativa(Alternativa alternativa) {
        this.alternativa = alternativa;
    }

    public boolean isSelecionada() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }
}
