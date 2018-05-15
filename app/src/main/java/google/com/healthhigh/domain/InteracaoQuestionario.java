package google.com.healthhigh.domain;

/**
 * Created by Alan on 25/04/2018.
 */

public class InteracaoQuestionario extends Interacao{
    private Questionario questionario;
    private InteracaoDesafio interacao_desafio;
    private long data_inicio, data_termino;

    public String statusQuestionario() {
        String status = "Indefinido";
        if(getPublicacao() != null){
            Publicacao p = getPublicacao();
            //Publicação em vigência
            if (p.isVigente()) {
                if(getData_visualizacao() > 0) {
                    status = "Pendente";
                    if(getData_inicio() > 0 && getData_termino() <= 0){
                        status = "Iniciado";
                    } else if(getData_termino() > 0){
                        status = "Finalizado";
                    }
                } else {
                    status = "Nova Publicação";
                }
            } else {
                status = "Publicação Encerrada";
            }
        } else {
            status = "Não publicado";
        }
        return status;
    }

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
