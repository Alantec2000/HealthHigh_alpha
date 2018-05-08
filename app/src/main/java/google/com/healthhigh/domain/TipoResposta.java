package google.com.healthhigh.domain;

/**
 * Created by Alan on 19/04/2018.
 */

public abstract class TipoResposta {
    private long id;
    private Publicacao publicacao;
    private long data_de_resposta = 0;
    abstract protected TipoQuestao obterQuestaoRespostas();

    public long getData_de_resposta() {
        return data_de_resposta;
    }

    public void setData_de_resposta(long data_de_resposta) {
        this.data_de_resposta = data_de_resposta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Publicacao getPublicacao() {
        return publicacao;
    }

    public void setPublicacao(Publicacao publicacao) {
        this.publicacao = publicacao;
    }
}
