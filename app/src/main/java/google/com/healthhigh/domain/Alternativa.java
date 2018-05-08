package google.com.healthhigh.domain;

/**
 * Created by Alan on 19/04/2018.
 */

public class Alternativa {
    private long id_alternativa;
    private String descricao = "";
    private long data_criacao = 0;
    private QuestaoAlternativa questaoAlternativa;
    private RespostaAlternativa resposta;
    public RespostaAlternativa getResposta(){
        return resposta;
    }

    public long getId_alternativa() {
        return id_alternativa;
    }

    public void setId(long id_alternativa) {
        this.id_alternativa = id_alternativa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(long data_criacao) {
        this.data_criacao = data_criacao;
    }

    public QuestaoAlternativa getQuestaoAlternativa() {
        return questaoAlternativa;
    }

    public void setQuestaoAlternativa(QuestaoAlternativa questaoAlternativa) {
        this.questaoAlternativa = questaoAlternativa;
    }

    public void setResposta(RespostaAlternativa resposta) {
        this.resposta = resposta;
    }
}
