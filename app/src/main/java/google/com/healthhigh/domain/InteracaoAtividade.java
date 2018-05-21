package google.com.healthhigh.domain;

import java.util.Map;

public class InteracaoAtividade extends Interacao {
    private long data_conclusao;
    private Map<Long, ExecucaoAtividade> atividades_executadas;
    private final Usuario usuario;

    public InteracaoAtividade(Usuario usuario) {
        this.usuario = usuario;
    }

    public long getTempoExecucaoTotal(){
        long total = 0;
        for(ExecucaoAtividade e_a : atividades_executadas.values()){
            total += e_a.getTempo_execucao();
        }
        return total;
    }

    public int getTotalPassosDados(){
        int total = 0;
        for(ExecucaoAtividade e_a : atividades_executadas.values()){
            total += e_a.getTotal_passos_dados();
        }
        return total;
    }

    public long getData_conclusao() {
        return data_conclusao;
    }

    public void setData_conclusao(long data_conclusao) {
        this.data_conclusao = data_conclusao;
    }

    public Map<Long, ExecucaoAtividade> getAtividades_executadas() {
        return atividades_executadas;
    }

    public void setAtividades_executadas(Map<Long, ExecucaoAtividade> atividades_executadas) {
        this.atividades_executadas = atividades_executadas;
    }
}
