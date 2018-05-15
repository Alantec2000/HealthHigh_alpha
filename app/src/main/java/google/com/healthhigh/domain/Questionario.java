package google.com.healthhigh.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 22/10/2017.
 */

public class Questionario extends TipoMeta {
    private long id, data_criacao, data_visualizacao;
    private String titulo, descricao;
    private InteracaoQuestionario interacao_questionario;
    private List<Questao> questoes = new ArrayList<>();
    private List<TipoQuestao> l_questoes;

    public long getData_visualizacao() {
        return data_visualizacao;
    }

    public void setData_visualizacao(long data_visualizacao) {
        this.data_visualizacao = data_visualizacao;
    }

    public List<TipoQuestao> getL_questoes() {
        return l_questoes;
    }

    public void setL_questoes(List<TipoQuestao> l_questoes) {
        this.l_questoes = l_questoes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

    public long getId() {
        return id;
    }

    @Override
    public int getTipo() {
        return QUESTIONARIO;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public Interacao getInteracao() {
        return interacao_questionario;
    }

    public long getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(long data_criacao) {
        this.data_criacao = data_criacao;
    }

    public InteracaoQuestionario getInteracao_questionario() {
        return interacao_questionario;
    }

    public void setInteracao_questionario(InteracaoQuestionario interacao_questionario) {
        this.interacao_questionario = interacao_questionario;
    }
}
