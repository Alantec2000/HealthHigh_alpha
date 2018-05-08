package google.com.healthhigh.domain;

/**
 * Created by Alan on 17/04/2018.
 */

public class DesafioUsuario {
    private Colaborador colaborador;
    private Desafio desafio;
    private long data_aceito;
    private long data_conclusao;
    private boolean visualizado;
    private boolean em_execucao;

    public Desafio getDesafio() {
        return desafio;
    }

    public void setDesafio(Desafio desafio) {
        this.desafio = desafio;
    }

    public long getData_aceito() {
        return data_aceito;
    }

    public void setData_aceito(long data_aceito) {
        this.data_aceito = data_aceito;
    }

    public boolean getEm_execucao() {
        return em_execucao;
    }

    public void setEm_execucao(boolean em_execucao) {
        this.em_execucao = em_execucao;
    }
}
