package google.com.healthhigh.domain;

import com.google.gson.annotations.SerializedName;

public class Atividade extends TipoMeta {
    private long id_atividade;
    private transient InteracaoAtividade interacao_atividade;

    @SerializedName("id_premiacao_atividade")
    private long id_premiacao;

    @SerializedName("nome_atividade")
    private String nome_atividade;

    @SerializedName("descricao_atividade")
    private String descricao_atividade;

    @SerializedName("total_dados_atividade")
    private int total_passos_dados_atividade;

    private long total_tempo_execucao_atividade;

    public long getId_premiacao() {
        return id_premiacao;
    }

    public void setId_premiacao(long id_premiacao) {
        this.id_premiacao = id_premiacao;
    }

    public long getId_atividade() {
        return id_atividade;
    }

    public void setId_atividade(long id_atividade) {
        this.id_atividade = id_atividade;
    }

    @Override
    public int getTipo() {
        return ATIVIDADE;
    }

    @Override
    public boolean isConcluida() {
        int total_passos = 0, total_tempo = 0;
        if(interacao_atividade != null){
            for(SessaoAtividade s_a : interacao_atividade.getSessoes_atividade().values()) {
               total_passos += s_a.getTotalPassosDados();
            }
        }
        return (total_passos >= total_passos_dados_atividade);
    }

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
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
        return nome_atividade;
    }

    public void setNome(String nome) {
        this.nome_atividade = nome;
    }

    public String getDescricao() {
        return descricao_atividade;
    }

    public void setDescricao(String descricao) {
        this.descricao_atividade = descricao;
    }

    public int getTotal_passos_dados() {
        return total_passos_dados_atividade;
    }

    public void setTotal_passos_dados(int total_passos_dados) {
        this.total_passos_dados_atividade = total_passos_dados;
    }

    public long getTotal_tempo_execucao() {
        return total_tempo_execucao_atividade;
    }

    public void setTotal_tempo_execucao(long total_tempo_execucao) {
        this.total_tempo_execucao_atividade = total_tempo_execucao;
    }

    public long getIdPremiacao() {
        return (getPremiacao() != null ? getPremiacao().getId() : 0);
    }
}