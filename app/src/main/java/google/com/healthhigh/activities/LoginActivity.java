package google.com.healthhigh.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.healthhigh.R;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import google.com.healthhigh.domain.Usuario;
import google.com.healthhigh.retrofit.ContainerAPI;
import google.com.healthhigh.retrofit.LoginContainer;
import google.com.healthhigh.retrofit.ParametrosRequisicaoLogin;
import google.com.healthhigh.retrofit.RespostaContainer;
import google.com.healthhigh.retrofit.RetrofitConfig;
import google.com.healthhigh.utils.MessageDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends UserSessionManager {
    public static String rslt="";//string que é passada para o Web Service
    @BindView(R.id.btn_autenticar) Button btn_login;
    @BindView(R.id.txt_login) EditText txt_login;
    @BindView(R.id.txt_senha) EditText txt_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        pref.estaLogando();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pref.estaLogado()){
            Log.i("Loging", "Usuário já está logado");
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pref.naoEstaLogando();
    }

    @OnClick(R.id.btn_autenticar)
    public void autenticar(){
        btn_login.setEnabled(false);
        final Button btn_login_aux = btn_login;
        String login = txt_login.getText().toString();
        String senha = txt_senha.getText().toString();
        Call<ResponseBody> call = new RetrofitConfig().getServicoPerfilUsuario().autenticarUsuario(login, senha);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                btn_login.setEnabled(true);
                if(response.isSuccessful()){
                    String resposta = null;
                    int id = 0;
                    JsonObject jo = null;
                    try {
                        resposta = response.body().string();
                        jo = new JsonParser().parse(resposta).getAsJsonObject();
                        id = jo.get("id") == null ? 0 : Integer.parseInt(String.valueOf(jo.get("id")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(jo != null && id > 0){
                        if(pref.logarUsuario(jo)){
                            Intent i = new Intent(getContext(), HomeActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    } else {
                        MessageDialog.showMessage(getContext(), "E-mail/Senha incorretos!", "Erro de autenticação:");
                    }
                } else {
                    onFailure(call, new Throwable("Requisição incorreta!"));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                btn_login.setEnabled(true);
                MessageDialog.showMessage(getContext(), "Problema ao autenticar o usuário", "Erro:");
                Log.e("Login error", t.getMessage());
            }
        });
    }


/*
    @OnClick(R.id.btn_autenticar)
    public void autenticar(){
        btn_login.setEnabled(false);
        final Button btn_login_aux = btn_login;
        String login = txt_login.getText().toString();
        String senha = txt_senha.getText().toString();
        Call<RespostaContainer<Usuario>> call = new RetrofitConfig().getServicoPerfilUsuario().autenticarUsuario("autenticar", login, senha);
        call.enqueue(new Callback<RespostaContainer<Usuario>>() {
            @Override
            public void onResponse(Call<RespostaContainer<Usuario>> call, Response<RespostaContainer<Usuario>> response) {
                btn_login.setEnabled(true);
                int code = response.code();
                if(response.isSuccessful()){
                    if(code == 200){
                        RespostaContainer<Usuario> resposta = response.body();
                        if(resposta.getCode() > 0){
                            Usuario usuario = resposta.getContent();
                            if(pref.logarUsuario(usuario)){
                                Intent i = new Intent(getContext(), HomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            } else {
                                boolean isDesativado = false;
                                if(usuario != null)
                                    isDesativado = usuario.isDesativado();
                                if(isDesativado) {
                                    MessageDialog.showMessage(getContext(), "Seu usuário está desativado!", "Erro:");
                                } else {
                                    MessageDialog.showMessage(getContext(), "Login ou senha inválidos!", "Erro:");
                                }
                            }
                        } else {
                            MessageDialog.showMessage(getContext(), resposta.getMessage(), "Erro:");
                        }
                    } else {
                        MessageDialog.showMessage(getContext(), "Login ou senha inválidos!", "Erro:");
                    }
                } else {
                    onFailure(call, new Throwable("Erro ao autenticar usuário"));
                }
            }

            @Override
            public void onFailure(Call<RespostaContainer<Usuario>> call, Throwable t) {
                btn_login.setEnabled(true);
                MessageDialog.showMessage(getContext(), "Problema ao autenticar o usuário", "Erro:");
                Log.e("Login error", t.getMessage());
            }
        });
    }*/

    private Context getContext() {
        return this;
    }
}
