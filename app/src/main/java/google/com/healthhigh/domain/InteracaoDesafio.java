package google.com.healthhigh.domain;

import google.com.healthhigh.utils.DataHelper;

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

    public static final String
            PENDENTE = "Pendente",
            EM_EXECUCAO = "Realizando",
            CANCELADO = "Cancelado",
            CONCLUIDO = "Concluído",
            ENCERRADO = "Encerrado";

    private String status;

    public void setStatus(String new_status){
        status = new_status;
    }

    public String getStatus(){
        return status;
    }

    public void atualizaStatus() {
        status = "Indefinido";
        if(getData_conclusao() > 0) {
            setStatus(CONCLUIDO);
            desafio.setStatus(Desafio.CONCLUIDO);
        } else if(getData_aceito() > 0 && estaRealizando()){
            setStatus(EM_EXECUCAO);
            desafio.setStatus(Desafio.EM_EXECUCAO);
        } else if(getData_cancelamento() > 0){
            if(getPublicacao().isVigente()){
                setStatus(PENDENTE);
                desafio.setStatus(Desafio.PENDENTE);
            } else{
                setStatus(CANCELADO);
                desafio.setStatus(Desafio.ENCERRADO);
            }
        } else if(!getPublicacao().isVigente()){
            setStatus(ENCERRADO);
            desafio.setStatus(Desafio.ENCERRADO);
        } else {
            setStatus(PENDENTE);
            desafio.setStatus(Desafio.PENDENTE);
        }
    }

    public long getIdDesafio() {
        return (getDesafio() != null ? getDesafio().getId() : 0);
    }
}
