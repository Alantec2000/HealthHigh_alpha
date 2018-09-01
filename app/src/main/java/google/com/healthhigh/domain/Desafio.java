package google.com.healthhigh.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import google.com.healthhigh.utils.DataHelper;

/**
 * Created by Alan on 16/05/2017.
 */

public class Desafio {
    private transient  String TAG = "DESAFIO";
    public transient static final String TAG_ID = "desafio_id";
    @SerializedName("titulo")
    private String titulo;
    @SerializedName("descricao")
    private String descricao;

    @SerializedName("publicacoes")
    private List<Publicacao> publicacoes;

    private transient int status = 1;
    @SerializedName("id")
    private long id;
    @SerializedName("id_premiacao_desafio")
    private long id_premiacao;
    @SerializedName("id_grupo_desafio")
    private long id_grupo;
    @SerializedName("data_criacao_desafio")
    private long data_criacao;

    private transient long data_aceito;
    private transient long data_conclusao;
    private transient long data_visualizacao;

    private transient int tipo;
    private transient int quantidade;
    private transient int tentativas;

    public transient static final int PENDENTE = 1;
    public transient static final int VISUALIZADO = 2;
    public transient static final int EM_EXECUCAO = 3;
    public transient static final int CONCLUIDO = 4;
    public transient static final int ENCERRADO = 5;
    public transient static final int NAO_PUBLICADO = 5;

    private transient Publicacao publicacao;
    private Premiacao premiacao;
    private transient InteracaoDesafio interacao_desafio;
    private transient boolean aceito;
    private transient List<Meta> metas_TO_OLD = new ArrayList<>();
    private List<Atividade> atividades = new ArrayList<>();
    private List<TipoMeta> metas = new ArrayList<>();

    public Desafio() {
        publicacoes = new ArrayList<>();
    }

    public Desafio(long id, String t, String d, int td, boolean fa, int tn) {
        this.id = id;
        this.titulo = t;
        this.descricao = d;
        this.tipo = td;
        this.tentativas = tn;
    }

    public Premiacao getPremiacao() {
        return premiacao;
    }

    public void setPremiacao(Premiacao premiacao) {
        this.premiacao = premiacao;
    }

    public List<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
    }

    public List<TipoMeta> getMetas() {
        return metas;
    }

    public void setMetas(List<TipoMeta> metas) {
        this.metas = metas;
    }

    public boolean isAceito() {return aceito;}

    public void setAceito(boolean aceito) {this.aceito = aceito;}

    public long getData_criacao() {return data_criacao;}

    public void setData_criacao(long data_criacao) {this.data_criacao = data_criacao;}

    public long getData_aceito() {return data_aceito;}

    public void setData_aceito(long data_aceito) {this.data_aceito = data_aceito;}

    public void addMeta(Meta m){this.metas_TO_OLD.add(m);}

    public List<Meta> getMetas_TO_OLD() {
        return metas_TO_OLD;
    }

    public void setMetas_TO_OLD(List<Meta> metas_TO_OLD) {
        this.metas_TO_OLD = metas_TO_OLD;
    }

    public int getTentativas() {
        return tentativas;
    }

    public void setTentativas(int tentativas) {
        this.tentativas = tentativas;
    }

    public int getTipo() {return tipo;}

    public void setTipo(int tipo) {this.tipo = tipo;}

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getData_conclusao() {
        return data_conclusao;
    }

    public void setData_conclusao(long data_conclusao) {
        this.data_conclusao = data_conclusao;
    }

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

    public Publicacao getPublicacao() {
        return publicacao;
    }

    public void setPublicacao(Publicacao publicacao) {
        this.publicacao = publicacao;
    }

    public InteracaoDesafio getInteracao_desafio() {
        return interacao_desafio;
    }

    public void setInteracao_desafio(InteracaoDesafio interacao_desafio) {
        this.interacao_desafio = interacao_desafio;
    }

    public long getData_visualizacao() {
        return data_visualizacao;
    }

    public void setData_visualizacao(long data_visualizacao) {
        this.data_visualizacao = data_visualizacao;
    }

    public String getStatusPublicacao() {
        String status = "Indefinido";
        Publicacao p = getPublicacao();
        if(p != null){
            if(p.isVigente()){
                InteracaoDesafio i_d = getInteracao_desafio();
                status = "Desafio Publicado";
                if(i_d != null) {
                    if(i_d.getData_visualizacao() > 0){
                        status = "Desafio Publicado";
                        if(i_d.estaRealizando()){
                            status = "Realizando";
                        } else if(i_d.getData_cancelamento() > 0) {
                            status = "Cancelado";
                        } else if (i_d.getData_conclusao() > 0) {
                            status = "Concluído";
                        }
                    } else {
                        status = "Publicação nova";
                    }
                } else {
                    status = "Publicação nova";
                }
            } else if(p.getData_fim() < DataHelper.now()) {
                status = "Publicação encerrada";
            }
        } else {
            status = "Não publicado";
        }
        return null;
    }

    public List<Publicacao> getPublicacoes() {
        return this.publicacoes;
    }

    public long getId_premiacao() {
        return id_premiacao;
    }

    public void setId_premiacao(long id_premiacao) {
        this.id_premiacao = id_premiacao;
    }
}
