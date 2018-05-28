package google.com.healthhigh.domain;

public class ExecucaoAtividade {
    private long id, data_fim_execucao, tempo_execucao;
    private long data_inicio_execucao;
    private int total_passos_dados;
    private SessaoAtividade sessao_atividade;

    public ExecucaoAtividade() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getData_inicio_execucao() {
        return data_inicio_execucao;
    }

    public void setData_inicio_execucao(long data_inicio_execucao) {
        this.data_inicio_execucao = data_inicio_execucao;
    }

    public long getData_fim_execucao() {
        return data_fim_execucao;
    }

    public void setData_fim_execucao(long data_fim_execucao) {
        this.data_fim_execucao = data_fim_execucao;
    }

    public long getTempo_execucao() {
        return tempo_execucao;
    }

    public void setTempo_execucao(long tempo_execucao) {
        this.tempo_execucao = tempo_execucao;
    }

    public int getTotal_passos_dados() {
        return total_passos_dados;
    }

    public void setTotal_passos_dados(int total_passos_dados) {
        this.total_passos_dados = total_passos_dados;
    }

    public SessaoAtividade getSessao_atividade() {
        return sessao_atividade;
    }

    public void setSessao_atividade(SessaoAtividade sessao_atividade) {
        this.sessao_atividade = sessao_atividade;
    }
}
