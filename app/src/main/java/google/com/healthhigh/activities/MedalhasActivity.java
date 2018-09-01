package google.com.healthhigh.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.healthhigh.R;

import google.com.healthhigh.dao.ItemDAO;
import google.com.healthhigh.domain.Item;
import google.com.healthhigh.tarefas_assincronas.CarregaListaMedalhas;
import google.com.healthhigh.utils.BitmapUtil;

public class MedalhasActivity extends AppCompatActivity {
    public final static String SOMENTE_NOVAS = "mostrar_somente_premiacoes_novas";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medalhas);
        NotificationManager n_m = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        n_m.cancel(R.layout.activity_medalhas);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setItensList();
    }

    private void insereMedalhaTeste(){
        ItemDAO iD = new ItemDAO(this);
        Item i = new Item();
        i.setNome("Medalhona 2");
        i.setDescricao("Medalhada");
        i.setXp(100);
        i.setTipo(0);
        i.setImg(BitmapUtil.bitmapToByteArray(BitmapFactory.decodeResource(this.getResources(), R.drawable.academia2)));
        iD.insereItem(i);
    }

    private void setItensList() {
        RecyclerView rv = (RecyclerView) findViewById(R.id.listaMedalhas);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            new CarregaListaMedalhas(this, rv).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new CarregaListaMedalhas(this, rv).execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_medalhas, menu);
        return true;
    }

}
