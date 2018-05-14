package google.com.healthhigh.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import google.com.healthhigh.utils.DataHelper;

/**
 * Created by Alan on 16/05/2017.
 */

public class Desafio {
    private String TAG = "DESAFIO";
    public static final String TAG_ID = "desafio_id";
    private String titulo;
    private String descricao;

    private int status = 1;
    private long id;
    private long data_criacao;
    private long data_aceito;
    private long data_conclusao;
    private long data_visualizacao;

    private int tipo;
    private int quantidade;
    private int tentativas;

    public static final int PENDENTE = 1;
    public static final int VISUALIZADO = 2;
    public static final int EM_EXECUCAO = 3;
    public static final int CONCLUIDO = 4;
    public static final int ENCERRADO = 5;

    private Publicacao publicacao;
    private InteracaoDesafio interacao_desafio
            ;
    private boolean aceito;
    private List<Meta> metas = new ArrayList<>();
    private Map<Long, TipoMeta> metas_list = new TreeMap<>();
    public Desafio() {}

    public Desafio(long id, String t, String d, int td, boolean fa, int tn) {
        this.id = id;
        this.titulo = t;
        this.descricao = d;
        this.tipo = td;
        this.tentativas = tn;
    }

    public Map<Long, TipoMeta> getMetas_list() {
        return metas_list;
    }

    public void setMetas_list(Map<Long, TipoMeta> metas_list) {
        this.metas_list = metas_list;
    }

    public boolean isAceito() {return aceito;}

    public void setAceito(boolean aceito) {this.aceito = aceito;}

    public long getData_criacao() {return data_criacao;}

    public void setData_criacao(long data_criacao) {this.data_criacao = data_criacao;}

    public long getData_aceito() {return data_aceito;}

    public void setData_aceito(long data_aceito) {this.data_aceito = data_aceito;}

    public void addMeta(Meta m){this.metas.add(m);}

    public List<Meta> getMetas() {
        return metas;
    }

    public void setMetas(List<Meta> metas) {
        this.metas = metas;
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
}
