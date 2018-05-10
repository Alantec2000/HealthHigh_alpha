package google.com.healthhigh.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.controller.NoticiaController;
import google.com.healthhigh.domain.InteracaoNoticia;
import google.com.healthhigh.domain.Noticia;
import google.com.healthhigh.services.LeitorListener;
import google.com.healthhigh.services.ServiceCalcularTempoDeLeitura;
import google.com.healthhigh.utils.DataHelper;
import google.com.healthhigh.utils.MessageDialog;

public class LendoNoticiaActivity extends AppCompatActivity implements ViewTreeObserver.OnScrollChangedListener{
    boolean contando = false;
//    LeitorListener ll;
    NoticiaController noticia_controller;

    public Intent self_intent;
    private long tempo_entrada = 0;
    private long tempo_saida = 0;

    public ScrollView lendo_noticia;
    public TextView titulo_noticia, corpo_noticia;

    public static final String NOTICIA_ID = "noticia_id";
    Noticia noticia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lendo_noticia);
        self_intent = this.getIntent();
        lendo_noticia = (ScrollView) findViewById(R.id.scroll_lendo_noticia);
        lendo_noticia.getViewTreeObserver().addOnScrollChangedListener(this);
        titulo_noticia = (TextView) findViewById(R.id.txt_titulo_noticia);
        corpo_noticia = (TextView) findViewById(R.id.txt_corpo_noticia);
        obterNoticia();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tempo_entrada = DataHelper.now();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tempo_saida = DataHelper.now();
        atualizarTempoLeitura(noticia.getInteracao_noticia());
    }

    private void atualizarTempoLeitura(@NonNull InteracaoNoticia i_n) {
        if(noticia.getDesafio_atual() != null) {
            i_n.setTempo_leitura(i_n.getTempo_leitura() + (tempo_saida - tempo_entrada));
            noticia_controller.atualizarInteracaoNoticia(i_n);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void obterNoticia() {
        long id_noticia = self_intent.getLongExtra(NOTICIA_ID, 0);
        if(id_noticia > 0){
            noticia_controller = new NoticiaController(this);
            noticia = noticia_controller.obterNoticia(id_noticia);
            if(noticia != null){
                titulo_noticia.setText(noticia.getTitulo());
                corpo_noticia.setText(noticia.getCorpo());
                if(!noticia.foiLida()){
                    noticia_controller.setNoticiaLida(noticia);
                }
                if(noticia.getDesafio_atual() != null){
                    try {
                        if(noticia.getInteracao_noticia() == null) {
                            InteracaoNoticia i_n = noticia_controller.inserirNovaInteracao(noticia, noticia.getDesafio_atual());
                            noticia.setInteracao_noticia(i_n);
                        }
                        noticia_controller.setInteracaoNoticiaLida(noticia.getInteracao_noticia());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                MessageDialog.showMessage(this, "Notícia não encontrado!", "Erro ao obter notícia");
            }
        } else {
            MessageDialog.showMessage(this, "Nenhum notícia informado!", "Erro ao obter notícia");
        }
    }

    /*private void iniciarCronometroLeitura() {
        if(noticia != null){
            Intent i = new Intent(this, ServiceCalcularTempoDeLeitura.class);
            if(noticia.getInteracao_noticia() != null && noticia.getTempoLeitura() !=0)
                i.putExtra(ServiceCalcularTempoDeLeitura.I_TEMPO_INICIAL, noticia.getTempoLeitura());
            bindService(i, this, Context.BIND_AUTO_CREATE);
            contando = true;
        }
    }

    private void pararSalvarTempoLeitura() {
        if(contando)
            unbindService(this);
    }
*/
    @Override
    public void onScrollChanged() {
        double total = ((double)lendo_noticia.getScrollY()/(double)lendo_noticia.getChildAt(0).getHeight())*(double)100;
//        Log.i("percentual totalis", Double.toString(());
        Log.i("percentual scroll y", Double.toString(lendo_noticia.getScrollY()));
        Log.i("percentual height", Double.toString(lendo_noticia.getChildAt(0).getHeight()));
    }

/*    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ServiceCalcularTempoDeLeitura.ConexaoLeitor cl = (ServiceCalcularTempoDeLeitura.ConexaoLeitor) iBinder;
        ll = cl.getCronometroLeitura();
        if(ll != null && noticia.getInteracao_noticia() != null) {
            ll.setNewCronometro(this, noticia);
            ll.startCronometro();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }*/
}
