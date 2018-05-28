package google.com.healthhigh.domain;

public class InteracaoNoticia extends Interacao {
    public final static String
            LIDA = "Lida",
            NOVA = "Nova",
            PENDENTE = "Pendente",
            ENCERRADA = "Publicação Encerrada",
            NAO_PUBLICADA = "Não Publicada";
    private long tempo_leitura;
    private Noticia noticia;
    private InteracaoDesafio interacao_desafio;

    public String statusNoticia(){
        String status = "Indefinido";
        if(getPublicacao() != null) {
            if(getData_visualizacao() <= 0){
                status = NOVA;
            } else if(getTempo_leitura() <= 0){
                status = PENDENTE;
            } else {
                status = LIDA;
            }
        } else {
            status = NAO_PUBLICADA;
        }
        return status;
    }

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