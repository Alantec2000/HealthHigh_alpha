package google.com.healthhigh.domain;

/**
 * Created by Alan on 22/10/2017.
 */

public class Resposta {
    public long id;
    public Questionario questionario;
    public Questao questao = new Questao();
    public String qualitativo;
    public short quantitativo = 1; // 1 = Não, 2 = Sim;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQualitativo() {
        return qualitativo;
    }

    public void setQualitativo(String qualitativo) {
        this.qualitativo = qualitativo;
    }

    public short getQuantitativo() {
        return quantitativo;
    }

    public void setQuantitativo(short quantitativo) {
        this.quantitativo = quantitativo;
    }

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }
}
