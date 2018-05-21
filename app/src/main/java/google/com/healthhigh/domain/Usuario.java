package google.com.healthhigh.domain;

/**
 * Created by Alan on 17/04/2018.
 */

public class Usuario {
    private String nome, sobrenome, login, matricula;
    private char sexo;
    private long data_nascimento, data_cadastro;
    private String foto;
    private boolean deficiente;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public long getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(long data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public long getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(long data_cadastro) {
        this.data_cadastro = data_cadastro;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isDeficiente() {
        return deficiente;
    }

    public void setDeficiente(boolean deficiente) {
        this.deficiente = deficiente;
    }
}
