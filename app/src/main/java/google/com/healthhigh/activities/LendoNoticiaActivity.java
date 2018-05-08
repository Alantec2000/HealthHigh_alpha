package google.com.healthhigh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.controller.NoticiaController;
import google.com.healthhigh.domain.Noticia;
import google.com.healthhigh.utils.MessageDialog;

public class LendoNoticiaActivity extends AppCompatActivity implements ViewTreeObserver.OnScrollChangedListener{
    public ScrollView lendo_noticia;
    public Intent self_intent;
    public TextView titulo_noticia, corpo_noticia;
    public static final String NOTICIA_ID = "noticia_id";

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

    private void obterNoticia() {
        long id_noticia = self_intent.getLongExtra(NOTICIA_ID, 0);
        if(id_noticia > 0){
            NoticiaController noticia_controller = new NoticiaController(this);
            Noticia noticia = noticia_controller.obterNoticia(id_noticia);
            if(noticia != null){
                titulo_noticia.setText(noticia.getTitulo());
                corpo_noticia.setText(noticia.getCorpo());
                if(!noticia.foiLida()){
                    noticia_controller.setNoticiaLida(noticia);
                }
//                if(!noticia.publicacaoFoiLida()){
//                    if(noticia.getInteracao_noticia() == null)
//                        noticia_controller.inserirNovaInteracao(noticia);
//                    noticia_controller.setInteracaoNoticiaLida(noticia.getInteracao_noticia());
//                }
            } else {
                MessageDialog.showMessage(this, "Notícia não encontrado!", "Erro ao obter notícia");
            }
        } else {
            MessageDialog.showMessage(this, "Nenhum notícia informado!", "Erro ao obter notícia");
        }
    }
    @Override
    public void onScrollChanged() {
        double total = ((double)lendo_noticia.getScrollY()/(double)lendo_noticia.getChildAt(0).getHeight())*(double)100;
//        Log.i("percentual totalis", Double.toString(());
        Log.i("percentual scroll y", Double.toString(lendo_noticia.getScrollY()));
        Log.i("percentual height", Double.toString(lendo_noticia.getChildAt(0).getHeight()));
    }
}
