package google.com.healthhigh.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.healthhigh.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import google.com.healthhigh.broadcastReceiver.IniciarColetaNovosDesafios;
import google.com.healthhigh.tarefas_assincronas.CarregaDesafiosPreview;
import google.com.healthhigh.utils.SessionManager;
import google.com.healthhigh.utils.Toaster;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class HomeActivity extends UserSessionManager implements View.OnClickListener{
    private Intent intent;
    @BindView(R.id.txt_nome_usuario) TextView nome_usuario;
    @BindView(R.id.txt_total_experiencia_usuario) TextView experiencia_usuario;
    @BindView(R.id.txt_ranking_atual_usuario) TextView ranking_usuario;
    @BindView(R.id.img_foto_perfil_usuario) ImageView foto_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        pref.verDadosDisponiveis();
        nome_usuario.setText(pref.getNome());
        experiencia_usuario.setText("Ranking: " + String.valueOf(pref.getPontuacao()));
        ranking_usuario.setText("Pont.: " + String.valueOf(pref.getPosicao_ranking()));
    }


    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView rv_lista_preview_desafio = (RecyclerView) findViewById(R.id.listaDesafios);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new CarregaDesafiosPreview(this, rv_lista_preview_desafio).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new CarregaDesafiosPreview(this, rv_lista_preview_desafio).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    public void enviarSinalparaObterDesafios(){
        Intent i = new Intent("google.com.healthhigh.ColetarDesafios");
        sendBroadcast(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_noticias:
                startActivity(new Intent(this,NoticiaActivity.class));
            break;
            case R.id.db_manager:
                startActivity(new Intent(this, AndroidDatabaseManager.class));
            break;
            case R.id.action_questionario:
//                startActivity(new Intent(this,ListaQuestionariosActivity.class));
                startActivity(new Intent(this,ListaQuestionariosActivity.class));
            break;
            case R.id.action_desafios:
                startActivity(new Intent(HomeActivity.this, ListaDesafiosActivity.class));
            break;
            case R.id.home_menu_atividade:
                startActivity(new Intent(HomeActivity.this, RealizandoAtividadeActivity.class));
            break;
            case R.id.home_menu_atualizar_base:
                enviarSinalparaObterDesafios();
                Toaster.toastShortMessage(HomeActivity.this, "Obtendo desafios");
            break;
            case R.id.home_menu_logout:
                pref.clear();
                Intent i = new Intent(this, LoginActivity.class);
                i.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    @OnClick({R.id.btn_menu_medalhas, R.id.btn_menu_conquistas, R.id.btn_menu_trofeus, R.id.txt_preview_questionarios_header, R.id.txt_preview_desafios_header})
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_menu_medalhas:
                startActivity(new Intent(HomeActivity.this, MedalhasActivity.class));
            break;
            case R.id.btn_menu_trofeus:
            break;
            case R.id.btn_menu_conquistas:
                Toaster.toastShortMessage(HomeActivity.this, "Conquistas");
            break;
            case R.id.txt_preview_questionarios_header:
                startActivity(new Intent(HomeActivity.this, ListaQuestionariosActivity.class));
            break;
            case R.id.txt_preview_desafios_header:
                startActivity(new Intent(HomeActivity.this, ListaDesafiosActivity.class));
            break;
        }
    }
}
