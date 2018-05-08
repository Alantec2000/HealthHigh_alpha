package google.com.healthhigh.domain;

import google.com.healthhigh.activities.Interacao;

public class InteracaoNoticia extends Interacao {
    private long tempo_leitura;
    private Noticia noticia;
    private InteracaoDesafio interacao_desafio;

    @Override
    public TipoMeta getMeta() {
        return noticia;
    }

    public Noticia getNoticia() {
        return noticia;
    }

    public void setNoticia(Noticia noticia) {
        this.noticia = noticia;
        this.meta = noticia;
    }

    public InteracaoDesafio getInteracao_desafio() {
        return interacao_desafio;
    }

    public void setInteracao_desafio(InteracaoDesafio interacao_desafio) {
        this.interacao_desafio = interacao_desafio;
    }

    public long getTempo_leitura() {
        return tempo_leitura;
    }

    public void setTempo_leitura(long tempo_leitura) {
        this.tempo_leitura = tempo_leitura;
    }
}