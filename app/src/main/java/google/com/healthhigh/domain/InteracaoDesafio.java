package google.com.healthhigh.domain;

/**
 * Created by Alan on 25/04/2018.
 * Essa classe é responsável por armazenar a interação entre desafio e usuário
 */

public class InteracaoDesafio extends Interacao {
    private long id;
    private long data_aceito;
    private long data_conclusao;
    private long data_cancelamento;
    private boolean realizando_no_momento;
    private Desafio desafio = new Desafio();

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

    public boolean estaRealizando() {
        return realizando_no_momento;
    }

    public void setRealizando_no_momento(boolean realizando_no_momento) {
        this.realizando_no_momento = realizando_no_momento;
    }

    public long getData_cancelamento() {
        return data_cancelamento;
    }

    public void setData_cancelamento(long data_cancelamento) {
        this.data_cancelamento = data_cancelamento;
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
