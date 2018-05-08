package google.com.healthhigh.domain;

import google.com.healthhigh.activities.Interacao;

/**
 * Created by Alan on 29/05/2017.
 */

public class Noticia extends TipoMeta{
    private String titulo, corpo;
    private Desafio desafio_atual;
    private InteracaoNoticia interacao_noticia;
    public String getTitulo() {
        return titulo;
    }

    @Override
    public Interacao getInteracao() {
        return getInteracao_noticia();
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public InteracaoNoticia getInteracao_noticia() {
        return interacao_noticia;
    }

    public void setInteracao_noticia(InteracaoNoticia interacao_noticia) {
        this.interacao_noticia = interacao_noticia;
    }

    public boolean foiLida(){
        return !(interacao_noticia != null && interacao_noticia.getData_visualizacao() != 0);
    }
}
