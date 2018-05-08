package google.com.healthhigh.domain;

import java.util.List;

public class Item {
    private int id;
    private String nome;
    private String descricao;
    private int tipo;
    private int xp;
    private byte[] img;
    private List<Meta> metas;
    private long data;

    public Item(String n, String d, int t, int xp, byte[] img){
        this.nome = n;
        this.descricao = d;
        this.tipo = t;
        this.xp = xp;
        this.img = img;
    }

    public Item(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public void addMeta(Meta m){
        this.metas.add(m);
    }
}
