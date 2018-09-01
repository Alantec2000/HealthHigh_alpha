package google.com.healthhigh.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.controller.PremiacaoController;
import google.com.healthhigh.dao.ItemDAO;
import google.com.healthhigh.domain.Item;
import google.com.healthhigh.domain.Premiacao;
import google.com.healthhigh.utils.BitmapUtil;
import google.com.healthhigh.utils.DataHelper;

public class DetalhesMedalhaActivity extends AppCompatActivity {
    private Handler h = new Handler();
    private ItemDAO iDao = null;
    private Premiacao i;
    private TextView nomeMedalha;
    private TextView dscMedalha;
    private TextView dataInsercaoMedalha;
    private TextView qtdXPMedalha;
    private TextView nMedalhasAssociadas;
    private ImageView medalhaIcone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_medalha);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final long id = getIntent().getLongExtra("ITEM_ID",0);
        new Thread(){
            public void run(){
                iDao = new ItemDAO(DetalhesMedalhaActivity.this);
                nomeMedalha = (TextView) findViewById(R.id.nomeMedalha);
                dscMedalha = (TextView) findViewById(R.id.dscMedalha);
                dataInsercaoMedalha = (TextView) findViewById(R.id.dataInsercaoMedalha);
                qtdXPMedalha = (TextView) findViewById(R.id.qtdXPMedalha);
                nMedalhasAssociadas = (TextView) findViewById(R.id.nMedalhasAssociadas);
                medalhaIcone = (ImageView) findViewById(R.id.medalhaIcone);
                if(id > 0){
                    i = (new PremiacaoController(getContext())).getPremiacao(id);
                }
                if(i != null){
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            setItemContent(i);
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public Context getContext(){
        return this;
    }

    private void setItemContent(Premiacao i) {
        nomeMedalha.setText(i.getNome());
        String data = DataHelper.toDateString(i.getData_criacao());
        dataInsercaoMedalha.setText(data);
        String xp = Integer.toString(i.getExperiencia()) + " Pontos";
        qtdXPMedalha.setText(xp);
        nMedalhasAssociadas.setText("00");
    }
}
