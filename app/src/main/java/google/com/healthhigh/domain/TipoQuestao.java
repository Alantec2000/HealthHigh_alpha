package google.com.healthhigh.domain;

import java.util.List;

/**
 * Created by Alan on 19/04/2018.
 */

public abstract class TipoQuestao {
    private long id, id_associacao;
    private Questionario questionario;
    protected String descricao = "";
    protected long data_criacao = 0;
    private RespostaOptativa resposta;

    abstract public int getTipoQuestao();
    abstract protected TipoResposta obterRespostaQuestao();

    public TipoResposta getResposta(){
        return obterRespostaQuestao();
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    public void setResposta(RespostaOptativa resposta) {
        this.resposta = resposta;
    }
}
