package google.com.healthhigh.domain;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.activities.Interacao;

public abstract class TipoMeta {
    protected long id, data_criacao, data_visualizacao;
    protected Desafio desafio_atual;
    protected Map<Long, Desafio> desafios_associados;

    public long getId() {
        return id;
    }

    public long getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(long data_criacao) {
        this.data_criacao = data_criacao;
    }

    public long getData_visualizacao() {
        return data_visualizacao;
    }

    public void setData_visualizacao(long data_visualizacao) {
        this.data_visualizacao = data_visualizacao;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Desafio getDesafio_atual() {
        return desafio_atual;
    }

    public void setDesafio_atual(Desafio desafio_atual) {
        this.desafio_atual = desafio_atual;
    }

    public Map<Long, Desafio> getDesafios_associados() {
        return desafios_associados;
    }

    public abstract Interacao getInteracao();

    public long getDataVisualizacaoInteracao(){
        long data_visualizacao = 0;
        if(getInteracao() != null){
            data_visualizacao = getInteracao().getData_visualizacao();
        }
        return data_visualizacao;
    }

    public void setDesafios_associados(Map<Long, Desafio> desafios_associados) {
        this.desafios_associados = desafios_associados;
    }
}
