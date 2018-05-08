package google.com.healthhigh.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.dao.ItemDAO;
import google.com.healthhigh.domain.Item;
import google.com.healthhigh.utils.BitmapUtil;
import google.com.healthhigh.utils.DataHelper;

public class DetalhesMedalhaActivity extends AppCompatActivity {
    private Handler h = new Handler();
    private ItemDAO iDao = null;
    private Item i;
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
        new Thread(){
            public void run(){
                iDao = new ItemDAO(DetalhesMedalhaActivity.this);
                nomeMedalha = (TextView) findViewById(R.id.nomeMedalha);
                dscMedalha = (TextView) findViewById(R.id.dscMedalha);
                dataInsercaoMedalha = (TextView) findViewById(R.id.dataInsercaoMedalha);
                qtdXPMedalha = (TextView) findViewById(R.id.qtdXPMedalha);
                nMedalhasAssociadas = (TextView) findViewById(R.id.nMedalhasAssociadas);
                medalhaIcone = (ImageView) findViewById(R.id.medalhaIcone);
                int id = getIntent().getIntExtra("ITEM_ID",0);
                if(id > 0){
                    i = iDao.getItem("SELECT * FROM " + ItemDAO.TABLE_NAME +
                            " WHERE " + ItemDAO.ID + " = " + id + ";");
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

    private void setItemContent(Item i) {
        nomeMedalha.setText(i.getNome());
        dscMedalha.setText(i.getDescricao());
        String data = DataHelper.parseUT(i.getData()/1000, "dd/MM/y");
        dataInsercaoMedalha.setText(data);
        medalhaIcone.setImageBitmap(BitmapUtil.getImage(i.getImg()));
        String xp = Integer.toString(i.getXp()) + " Pontos";
        qtdXPMedalha.setText(xp);
        nMedalhasAssociadas.setText("00");
    }
}
