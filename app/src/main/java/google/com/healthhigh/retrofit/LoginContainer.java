package google.com.healthhigh.retrofit;

public class LoginContainer {
    private final String method;
    private final String handler;
    private final String login;
    private final String senha;
    private final int tipo;

    public LoginContainer(String method, String handler, String login, String senha, int tipo) {
        this.method = method;
        this.handler = handler;
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
    }

    public String getMethod() {
        return method;
    }

    public String getHandler() {
        return handler;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public int getTipo() {
        return tipo;
    }
}
