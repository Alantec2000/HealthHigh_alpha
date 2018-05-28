package google.com.healthhigh.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.healthhigh.R;

import org.json.JSONObject;

import google.com.healthhigh.utils.ConverterSting;
import google.com.healthhigh.webservice.Caller;

public class LoginActivity extends AppCompatActivity {
    public static String rslt="";//string que é passada para o Web Service
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btLogin = (Button) findViewById(R.id.btn_autenticar);
        btLogin.setOnClickListener(onClickLogin());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }
    private View.OnClickListener onClickLogin() {//método do clique do botão
        final AlertDialog ad=new AlertDialog.Builder(this).create();

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    TextView tLogin = (TextView)findViewById(R.id.txt_login);
                    TextView tSenha = (TextView)findViewById(R.id.txt_senha);
                    String login = tLogin.getText().toString();
                    String senha = tSenha.getText().toString();
                    rslt="START";
                    Caller c=new Caller();//chama a conexão
                    c.a= login + "_" + senha;
                    c.join();
                    c.start();
                    while(rslt=="START") {
                        try {
                            Thread.sleep(10);
                        } catch (Exception ex) {
                        }
                    }
                    try {

                        JSONObject jsonLogin = new JSONObject(ConverterSting.substring(13,rslt));//a string para o objeto Json
                        String validaLogin =jsonLogin.getString("Status"); //extrair so o que eu quero, no caso é o Status = 1 ou 0
                        if(validaLogin.equals("1")) {
                            // Navega para a próxima tela
                            Intent intent = new Intent(getContext(),HomeActivity.class);//mudar para a activity que pretende mudar
                            //Bundle params = new Bundle();
                            startActivity(intent);
                        } else {
                            alert("Login e senha incorretos.");
                        }
                    } catch (Exception e){
                        Log.e("Erro", e.getMessage());
                    }
                }catch(Exception ex) {
                    ad.setTitle("Error!"); ad.setMessage(ex.toString());
                    ad.show();
                }
            }
        };
    }
    private Context getContext() {
        return this;
    }

    private void alert(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

}
