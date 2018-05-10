package google.com.healthhigh.domain;

public abstract class Interacao {
    private long id, data_criacao, data_visualizacao;
    protected TipoMeta meta;
    protected Publicacao publicacao;

    public void setPublicacao(Publicacao publicacao) {
        this.publicacao = publicacao;
    }

    public Publicacao getPublicacao(){
        return publicacao;
    }
    public Desafio getDesafioAtual(){
        return getMeta().getDesafio_atual();
    }

    public long getIdMeta(){
        return (meta != null ? meta.getId() : 0);
    }

    public TipoMeta getMeta(){
        return meta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getData_visualizacao() {
        return data_visualizacao;
    }

    public void setData_visualizacao(long data_visualizacao) {
        this.data_visualizacao = data_visualizacao;
    }

    public long getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(long data_criacao) {
        this.data_criacao = data_criacao;
    }

    public long getIdPublicacao() {
        return getPublicacao() != null ? getPublicacao().getId() : 0;
    }
}
