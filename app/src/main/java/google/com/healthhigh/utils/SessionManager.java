package google.com.healthhigh.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonObject;

import google.com.healthhigh.activities.HomeActivity;
import google.com.healthhigh.activities.LoginActivity;
import google.com.healthhigh.domain.Usuario;
import google.com.healthhigh.retrofit.ContainerAPI;

public class SessionManager {
    public static final String user_session_file = "UserSession";
    private final String id = "id_usuario",
            login = "login_usuario",
            data_primeiro_acesso = "data_criacao_sessao",
            data_ultimo_acesso = "data_ultimo_acesso_usuario",
            permanecer_logado = "manter_usuario_logado",
            nome = "nome_usuario",
            email = "e-mail_usuario",
            sexo = "sexo",
            matricula = "numero_matricula_usuario",
            deficiente = "usuario_tem_necessidades_especiais",
            desativado = "usuario_desativado",
            data_nascimento = "data_nascimento_usuario",
            pontuacao = "pontuacao_usuario",
            data_atualizacao_pontuacao_usuario = "data_atualizacao_pontuacao_usuario",
            posicao_ranking = "ranking_usuario",
            data_atualizacao_ranking = "data_atualizacao_ranking_usuario";

    private final SharedPreferences.Editor EDITOR;
    private final SharedPreferences CONTAINER;
    private final Context context;
    private boolean esta_logando;

    public SessionManager(SharedPreferences preferences, Context context) {
        //Inicializa contexto da tela de login
        this.context = context;

        //Inicializa gerenciador de sessão
        CONTAINER = preferences;
        EDITOR = preferences.edit();
        EDITOR.apply();
    }

    public void clear() {
        EDITOR.clear().commit();
    }

    public long getId(){
        return CONTAINER.getLong(id, 0);
    }

    public long getDataPrimeiroAcesso(){
        return CONTAINER.getLong(data_primeiro_acesso, 0);
    }

    public long getDataUltimoAcesso(){
        return CONTAINER.getLong(data_ultimo_acesso, 0);
    }

    public boolean manterSessao(){
        return CONTAINER.getBoolean(permanecer_logado, false);
    }

    public String getNome() {
        return CONTAINER.getString(nome, "--");
    }

    public int getPosicao_ranking() {
        return CONTAINER.getInt(posicao_ranking, 0);
    }

    public long getPontuacao() {
        return CONTAINER.getLong(pontuacao, 0);
    }

    public void setLogin(String login){
        EDITOR.putString(this.login, login);
    }
    public void setNome(String nome){
        EDITOR.putString(this.nome, nome);
    }
    public void setSexo(Character sexo){
        EDITOR.putString(this.sexo, String.valueOf(sexo));
    }
    public void setMatricula(String matricula) {
        EDITOR.putString(this.matricula, matricula);
    }
    public void setEmail(String email){
        EDITOR.putString(this.email, email);
    }
    public void setDesativado(boolean desativado){
        EDITOR.putBoolean(this.desativado, desativado);
    }
    public void setDeficiente(boolean is_deficiente){
        EDITOR.putBoolean(deficiente, is_deficiente);
    }
    public void setId(long id){
        EDITOR.putLong(this.id, id);
    }
    public void setDataNascimento(long data_nascimento){
        EDITOR.putLong(this.data_nascimento, data_nascimento);
    }
    public void setDataAcesso(long data_acesso){
        EDITOR.putLong(this.data_primeiro_acesso, data_acesso);
    }
    public void setDataUltimoAcesso(long data_ultimo_acesso){
        EDITOR.putLong(this.data_ultimo_acesso, data_ultimo_acesso);
    }
    public void setPosicaoRanking(int posicao){
        EDITOR.putInt(this.posicao_ranking, posicao);
    }
    public void setDataAtualizacaoRanking(long data_atualizacao){
        EDITOR.putLong(this.data_atualizacao_ranking, data_atualizacao);
    }
    public void setTotalPontuacao(long pontuacao){
        EDITOR.putLong(this.pontuacao, pontuacao);
    }
    public void setDataAtualizacaoPontuacao(long data_atualizacao){
        EDITOR.putLong(this.data_atualizacao_pontuacao_usuario, data_atualizacao);
    }

    public void verDadosDisponiveis(){
        Log.i("id", String.valueOf(getId()));
        Log.i("Nome", getNome());
    }

    public void persistir(){
        EDITOR.apply();
    }

    public boolean logarUsuario(Usuario usuario) {
        boolean result = false;
        if (usuario != null) {
            if (usuario.getId() > 0 && !usuario.isDesativado()) {
                result = true;
                // Informações de controle
                setId(usuario.getId());
                setLogin(usuario.getLogin());

                // Informações pessoais
                setNome(usuario.getNome());
                setMatricula(usuario.getMatricula());
                setSexo(usuario.getSexo());
                setEmail(usuario.getEmail());
                setDataNascimento(usuario.getData_nascimento());
                setDeficiente(usuario.isDeficiente());
                setDesativado(usuario.isDesativado());

                // Pontuação
                setTotalPontuacao(0);
                setDataAtualizacaoPontuacao(0);

                // Ranking
                setPosicaoRanking(0);
                setDataAtualizacaoRanking(0);

                // Sessão
                setDataAcesso(DataHelper.now());
                setDataUltimoAcesso(DataHelper.now());
                persistir();
            }
        }
        return result;
    }

    public boolean logarUsuario(JsonObject c) {
        boolean result = false;
        if (c != null) {
            if (c.get("id").getAsInt() > 0 && c.get("status").getAsInt() > 0) {
                result = true;
                // Informações de controle
                setId(c.get("id").getAsInt());

                // Informações pessoais
                setNome(c.get("nome").getAsString());
                setMatricula(c.get("matricula").getAsString());
                setEmail(c.get("email").getAsString());
                setSexo(c.get("sexo").getAsCharacter());
//                setDataNascimento(c.data_nascimento);
                setDesativado(c.get("status").getAsBoolean());

                // Pontuação
                setTotalPontuacao(0);
                setDataAtualizacaoPontuacao(0);

                // Ranking
                setPosicaoRanking(0);
                setDataAtualizacaoRanking(0);

                // Sessão
                setDataAcesso(DataHelper.now());
                setDataUltimoAcesso(DataHelper.now());
                persistir();
                verificarLogin();
            }
        }
        return result;
    }

    public void verificarLogin(){
        if(!this.estaLogado() && !esta_logando){
            Intent i = new Intent(this.context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            setDataUltimoAcesso(DataHelper.now());
            persistir();
        }
    }

    public boolean estaLogado() {
        long id = getId();
        boolean response = false;
        if(id > 0){
            response = ((getDataPrimeiroAcesso() - getDataUltimoAcesso()) < DataHelper.milliMin()*30 || manterSessao());
        }
        return response;
    }

    public void estaLogando() {
        this.esta_logando = true;
    }

    public void naoEstaLogando() {
        this.esta_logando = false;
    }
}
