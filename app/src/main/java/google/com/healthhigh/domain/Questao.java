package google.com.healthhigh.domain;

/**
 * Created by Alan on 22/10/2017.
 */

public class Questao extends TipoQuestao{
    private int tipo; // 1 = Quantitativa, 2 = Qualitativa, 3 = Ambas
    private long id;
    private String descricao;
    private Questionario questionario;
    private Resposta resposta;
    private TipoResposta resposta_t;

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    @Override
    public int getTipoQuestao() {
        return 0;
    }

    @Override
    protected TipoResposta obterRespostaQuestao() {
        return null;
    }

    public TipoResposta getResposta() {
        return resposta_t;
    }

    public void setResposta(Resposta resposta) {
        this.resposta = resposta;
    }
}
