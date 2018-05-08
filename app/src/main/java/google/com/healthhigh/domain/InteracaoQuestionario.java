package google.com.healthhigh.domain;

import google.com.healthhigh.activities.Interacao;

/**
 * Created by Alan on 25/04/2018.
 */

public class InteracaoQuestionario extends Interacao{
    private Questionario questionario;
    private InteracaoDesafio interacao_desafio;
    private long data_inicio, data_termino;

    @Override
    public TipoMeta getMeta() {
        return questionario;
    }

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    public InteracaoDesafio getInteracao_desafio() {
        return interacao_desafio;
    }

    public void setInteracao_desafio(InteracaoDesafio interacao_desafio) {
        this.interacao_desafio = interacao_desafio;
    }

    public long getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(long data_inicio) {
        this.data_inicio = data_inicio;
    }

    public long getData_termino() {
        return data_termino;
    }

    public void setData_termino(long data_termino) {
        this.data_termino = data_termino;
    }
}
