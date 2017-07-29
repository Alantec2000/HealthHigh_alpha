package google.com.healthhigh.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.tarefas_assincronas.CarregaListaMedalhas;
import google.com.healthhigh.tarefas_assincronas.CarregaTelaInicial;
import google.com.healthhigh.utils.Toaster;

import com.google.healthhigh.R;

import java.io.ByteArrayOutputStream;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inicializaBotoesFacanha();
        inicializaBotoes();
        RecyclerView rv = (RecyclerView) findViewById(R.id.listaDesafios);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            new CarregaTelaInicial(this, rv).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new CarregaTelaInicial(this, rv).execute();
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
        getMenuInflater().inflate(R.menu.menu_noticia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_noticias:
                intent = new Intent(getContext(),NoticiaActivity.class);
                startActivity(intent);
                break;
            case R.id.action_questionario:
                intent = new Intent(getContext(),QuestionarioActivity.class);
                startActivity(intent);
                break;
            case R.id.action_utilidades:
                intent = new Intent(getContext(),UtilidadesActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private Context getContext() {
        return this;
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
            case R.id.headerPreviewDesafios:
                startActivity(new Intent(HomeActivity.this, ListaDesafiosActivity.class));
            break;
        }
    }
}
