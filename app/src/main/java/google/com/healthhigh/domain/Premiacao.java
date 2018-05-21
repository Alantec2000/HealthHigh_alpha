package google.com.healthhigh.domain;

public class Premiacao {
    private long id, data_criacao, data_visualizacao;
    private boolean status;
    private int experiencia;
    private String nome;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(long data_criacao) {
        this.data_criacao = data_criacao;
    }

    public long getData_visualizacao() {
        return data_visualizacao;
    }

    public void setData_visualizacao(long data_visualizacao) {
        this.data_visualizacao = data_visualizacao;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
