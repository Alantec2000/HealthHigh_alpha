package google.com.healthhigh.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.healthhigh.R;

import google.com.healthhigh.adapter.MetaListAdapter;
import google.com.healthhigh.dao.DesafioXMetaDAO;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.sensors.SensorUI;
import google.com.healthhigh.sensors.SensorPasso;
import google.com.healthhigh.utils.MessageDialog;
import google.com.healthhigh.utils.Toaster;

public class RealizandoDesafioActivity extends AppCompatActivity{
    public static String DESAFIO_ID = "desafio_id";
    private DesafioXMetaDAO dao;
    private SensorPasso pedometro;
    private Intent intent;
    private Desafio d = null;
    private RecyclerView rv;
    public boolean realizandoDesafio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        setContentView(R.layout.activity_realizando_desafio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        rv = (RecyclerView) findViewById(R.id.listaMetasRealizando);
        dao = new DesafioXMetaDAO(this);
    }

    private void setDesafioMetas() {
        long id = intent.getLongExtra(Desafio.TAG_ID, -1);

        if(id > -1){
            d = dao.getDesafioMetas(id);
            if(d != null){
//                rv.setAdapter(new MetaListAdapter(this, d.getMetas_TO_OLD()));
//                rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            } else {
                MessageDialog.showMessage(this, "Desafio não encontrado", "Desafio selecionado não foi encontrado!");
            }
        } else {
            MessageDialog.showMessage(this, "Desafio não informado", "ID do desafio não encontrado!");
        }
    }

    private void setPedometro() {
        pedometro = new SensorPasso(this);
        if(pedometro.getStepSensor() != null) {
            SensorUI hud = new SensorUI();
            // hud.setMedia((TextView) findViewById(R.id.mediaEixos));
//            hud.setnPassos((TextView) findViewById(R.id.nPassos));
//            hud.setTempo((TextView) findViewById(R.id.tempoDecorrido));
//            pedometro.iniciarPedometro(hud, d);
            realizandoDesafio = true;
        } else {
            Toaster.toastLongMessage(this, "Celular não possui sensor compatível para execução de atividades");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDesafioMetas();
        setPedometro();
    }

    public void unsetPedometro(){
        pedometro.unsetEventListener();
        realizandoDesafio = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unsetPedometro();
    }
}
