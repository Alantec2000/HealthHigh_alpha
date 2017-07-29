package google.com.healthhigh.domain;

/**
 * Created by Alan on 28/06/2017.
 */

public class Colaborador {
    private int id;
    private int sexo;
    private String nome;
    private String senha;
    private String cpf;
    private String email;
    private String facebook;
    private String twitter;
    private String altura;
    private short ativo;
    private long XP;
    private long total_corrido;
    private long total_caminhado;
    private long total_pulado;

    public Colaborador(){}

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFacebook() {
        return facebook;
    }
    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
    public String getTwitter() {
        return twitter;
    }
    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
    public int getSexo() {
        return sexo;
    }
    public void setSexo(int sexo) {
        this.sexo = sexo;
    }
    public String getAltura() {
        return altura;
    }
    public void setAltura(String altura) {
        this.altura = altura;
    }
    public short getAtivo() {
        return ativo;
    }
    public void setAtivo(short ativo) {
        this.ativo = ativo;
    }
    public long getXP() {
        return XP;
    }
    public void setXP(long XP) {
        this.XP = XP;
    }
    public long getTotal_corrido() {
        return total_corrido;
    }
    public void setTotal_corrido(long total_corrido) {
        this.total_corrido = total_corrido;
    }
    public long getTotal_caminhado() {
        return total_caminhado;
    }
    public void setTotal_caminhado(long total_caminhado) {
        this.total_caminhado = total_caminhado;
    }
    public long getTotal_pulado() {
        return total_pulado;
    }
    public void setTotal_pulado(long total_pulado) {
        this.total_pulado = total_pulado;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
