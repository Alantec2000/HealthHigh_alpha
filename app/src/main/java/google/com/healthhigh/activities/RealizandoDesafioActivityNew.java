package google.com.healthhigh.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.controller.AtividadeController;
import google.com.healthhigh.controller.DesafioController;
import google.com.healthhigh.dao.DesafioXMetaDAO;
import google.com.healthhigh.domain.Desafio;
import google.com.healthhigh.sensors.SensorPasso;
import google.com.healthhigh.sensors.SensorUI;
import google.com.healthhigh.utils.MessageDialog;
import google.com.healthhigh.utils.Toaster;

public class RealizandoDesafioActivityNew extends AppCompatActivity{
    public static String DESAFIO_ID = "desafio_id";
    private SensorPasso pedometro;
    private Intent intent;
    private Desafio d = null;
    private RecyclerView rv;
    private DesafioController d_c;
    private AtividadeController a_c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        setContentView(R.layout.activity_realizando_desafio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        rv = (RecyclerView) findViewById(R.id.listaMetasRealizando);
        d_c = new DesafioController(this);
        a_c = new AtividadeController(this);
    }

    private void setDesafioMetas() {
        d = d_c.getDesafioAtual();
        if(d != null) {
            carregarAtividades();
        }
    }

    private void carregarAtividades() {

    }

    private void setPedometro() {
        pedometro = new SensorPasso(this);
        if(pedometro.getStepSensor() != null) {
            SensorUI hud = new SensorUI();
            hud.setnPassos((TextView) findViewById(R.id.txt_n_passos_atividade));
            hud.setTempo((TextView) findViewById(R.id.txt_cronometro_atividade));
            pedometro.setEventListener(hud, d);
        } else {
            Toaster.toastLongMessage(this, "Celular não possui sensor compatível para execução das atividades");
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        unsetPedometro();
    }
}
