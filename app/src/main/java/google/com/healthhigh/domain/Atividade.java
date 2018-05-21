package google.com.healthhigh.domain;

public class Atividade extends TipoMeta {
    private InteracaoAtividade interacao_atividade;
    private String nome, descricao;
    private int total_passos_dados;
    private long total_tempo_execucao;

    @Override
    public int getTipo() {
        return ATIVIDADE;
    }

    @Override
    public boolean isConcluida() {
        boolean concluida = false;
        if(interacao_atividade != null){
            concluida = (interacao_atividade.getTempoExecucaoTotal() >= total_tempo_execucao && interacao_atividade.getTotalPassosDados() >= total_tempo_execucao);
        }
        return concluida;
    }

    @Override
    public Interacao getInteracao() {
        return interacao_atividade;
    }

    public InteracaoAtividade getInteracao_atividade() {
        return interacao_atividade;
    }

    public void setInteracao_atividade(InteracaoAtividade interacao_atividade) {
        this.interacao_atividade = interacao_atividade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getTotal_passos_dados() {
        return total_passos_dados;
    }

    public void setTotal_passos_dados(int total_passos_dados) {
        this.total_passos_dados = total_passos_dados;
    }

    public long getTotal_tempo_execucao() {
        return total_tempo_execucao;
    }

    public void setTotal_tempo_execucao(long total_tempo_execucao) {
        this.total_tempo_execucao = total_tempo_execucao;
    }
}