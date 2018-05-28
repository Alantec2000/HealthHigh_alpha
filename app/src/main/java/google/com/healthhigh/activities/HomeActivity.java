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
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.tarefas_assincronas.CarregaDesafiosPreview;
import google.com.healthhigh.utils.Toaster;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inicializaBotoesFacanha();
        inicializaBotoes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView rv_lista_preview_desafio = (RecyclerView) findViewById(R.id.listaDesafios);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            new CarregaDesafiosPreview(this, rv_lista_preview_desafio).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new CarregaDesafiosPreview(this, rv_lista_preview_desafio).execute();
        }
    }

    private void inicializaBotoes() {
        TextView btnListaCompletaDesafios = (TextView) findViewById(R.id.headerPreviewDesafios);
        btnListaCompletaDesafios.setOnClickListener(this);
    }

    private void inicializaBotoesFacanha() {
        ImageButton btnMedalhas = (ImageButton) findViewById(R.id.btnMenuMedalhas);
        btnMedalhas.setOnClickListener(this);
        ImageButton btnTrofeus = (ImageButton) findViewById(R.id.btnMenuTrofeus);
        btnTrofeus.setOnClickListener(this);
        ImageButton btnConquistas = (ImageButton) findViewById(R.id.btnMenuConquistas);
        btnConquistas.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
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
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnMenuMedalhas:
                startActivity(new Intent(HomeActivity.this, MedalhasActivity.class));
            break;
            case R.id.btnMenuTrofeus:
                Toaster.toastShortMessage(HomeActivity.this, "Trofeus");
            break;
            case R.id.btnMenuConquistas:
                Toaster.toastShortMessage(HomeActivity.this, "Conquistas");
            break;
            case R.id.preview_questionarios_header:
                startActivity(new Intent(HomeActivity.this, ListaQuestionariosActivity.class));
            break;
            case R.id.headerPreviewDesafios:
                startActivity(new Intent(HomeActivity.this, ListaDesafiosActivity.class));
            break;
        }
    }
}
