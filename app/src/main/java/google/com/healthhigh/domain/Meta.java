package google.com.healthhigh.domain;

/**
 * Created by Alan on 10/06/2017.
 */

public class Meta {
    private int tipo, qtde;
    private long id, tempo, data;
    private String
            nome,
            descricao;

    public static String getTipoMeta(int tipo){
        /*tipo de atividade (0 - caminhada, 1 - corrida, 2 - pulo, 3 - evento)*/
        String retorno;
        switch (tipo){
            case 0:
                retorno = "Questionario";
                break;
            case 1:
                retorno = "Corrida";
                break;
            case 2:
                retorno = "Pulo";
                break;
            case 3:
                retorno = "Evento";
                break;
            default:
                retorno = "--";
        }
        return retorno;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getTempo() {
        return tempo;
    }

    public void setTempo(long tempo) {
        this.tempo = tempo;
    }

    public long getData() {
        return data;
    }

    public void setData(long tempo) {
        this.data = tempo;
    }
}
