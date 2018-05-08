package google.com.healthhigh.domain;

/**
 * Created by Alan on 25/04/2018.
 * Essa classe é responsável por armazenar a interação entre desafio e usuário
 */

public class InteracaoDesafio {
    private long id;
    private long data_aceito;
    private long data_conclusao;
    private boolean realizando_no_momento;
    private Desafio desafio = new Desafio();
    private Publicacao publicacao = new Publicacao();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getData_aceito() {
        return data_aceito;
    }

    public void setData_aceito(long data_aceito) {
        this.data_aceito = data_aceito;
    }

    public long getDataConclusao() {
        return data_conclusao;
    }

    public void setDataConclusao(long data_conclusao) {
        this.data_conclusao = data_conclusao;
    }

    public boolean isRealizando_no_momento() {
        return realizando_no_momento;
    }

    public void setRealizando_no_momento(boolean realizando_no_momento) {
        this.realizando_no_momento = realizando_no_momento;
    }

    public Publicacao getPublicacao() {
        return publicacao;
    }

    public void setPublicacao(Publicacao publicacao) {
        this.publicacao = publicacao;
    }

    public long getData_conclusao() {
        return data_conclusao;
    }

    public void setData_conclusao(long data_conclusao) {
        this.data_conclusao = data_conclusao;
    }

    public Desafio getDesafio() {
        return desafio;
    }

    public void setDesafio(Desafio desafio) {
        this.desafio = desafio;
    }
}
