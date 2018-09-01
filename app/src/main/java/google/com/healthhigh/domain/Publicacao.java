package google.com.healthhigh.domain;

import com.google.gson.annotations.SerializedName;

import google.com.healthhigh.utils.DataHelper;

/**
 * Created by Alan on 17/04/2018.
 */

public class Publicacao {
    @SerializedName("id_publicacao")
    private long id;

    @SerializedName("vigencia_inicio_publicacao")
    private long data_inicio;

    @SerializedName("vigencia_fim_publicacao")
    private long data_fim;

    @SerializedName("data_criacao_publicacao")
    private long data_criacao;

    private transient Desafio desafio;

    public static final int PENDENTE = 1;
    public static final int VISUALIZADO = 2;
    public static final int EM_EXECUCAO = 3;
    public static final int CONCLUIDO = 4;
    public static final int ENCERRADO = 5;

    public static String getStatusText(int status){
        String status_text;
        switch (status){
            case PENDENTE:
                status_text = "Pendente";
                break;
            case VISUALIZADO:
                status_text = "Visualizado";
                break;
            case EM_EXECUCAO:
                status_text = "Em execução";
                break;
            case CONCLUIDO:
                status_text = "Concluido";
                break;
            case ENCERRADO:
                status_text = "Encerrado";
                break;
            default:
                status_text = "Indefinido";
        }
        return status_text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(long data_inicio) {
        this.data_inicio = data_inicio;
    }

    public long getData_fim() {
        return data_fim;
    }

    public void setData_fim(long data_fim) {
        this.data_fim = data_fim;
    }

    public long getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(long data_criacao) {
        this.data_criacao = data_criacao;
    }

    public Desafio getDesafio() {
        return desafio;
    }

    public void setDesafio(Desafio desafio) {
        this.desafio = desafio;
    }

    public boolean isVigente() {
        long now = DataHelper.now();
        return (getData_inicio() < now && getData_fim() > now);
    }
}
