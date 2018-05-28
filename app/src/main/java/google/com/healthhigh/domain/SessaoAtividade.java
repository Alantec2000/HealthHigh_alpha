package google.com.healthhigh.domain;

import java.util.Map;
import java.util.TreeMap;

public class SessaoAtividade {
    private long id;
    private long data_inicio, data_fim;
    private InteracaoAtividade interacao_atividade;
    private Map<Long, ExecucaoAtividade> atividades;

    public SessaoAtividade() {
        atividades = new TreeMap<>();
    }

    public void setInteracao_atividade(InteracaoAtividade interacao_atividade) {
        this.interacao_atividade = interacao_atividade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(long data_inicio) {
        this.data_inicio = data_inicio;
    }

    public long getData_fim() {
        return data_fim;
    }

    public void setData_fim(long data_fim) {
        this.data_fim = data_fim;
    }

    public InteracaoAtividade getInteracao_atividade() {
        return interacao_atividade;
    }

    public Map<Long, ExecucaoAtividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(Map<Long, ExecucaoAtividade> atividades) {
        this.atividades = atividades;
    }

    public long getTempoExecucaoTotal(){
        long total = 0;
        if(atividades != null)
            for(ExecucaoAtividade e_a : atividades.values()){
                total += e_a.getTempo_execucao();
            }
        return total;
    }

    public int getTotalPassosDados(){
        int total = 0;
        if(atividades != null)
            for(ExecucaoAtividade e_a : atividades.values()){
                total += e_a.getTotal_passos_dados();
            }
        return total;
    }
}
