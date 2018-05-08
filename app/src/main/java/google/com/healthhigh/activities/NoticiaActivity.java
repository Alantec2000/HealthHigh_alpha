package google.com.healthhigh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.healthhigh.R;

import java.util.List;

import google.com.healthhigh.adapter.NoticiaAdapter;
import google.com.healthhigh.base.Base;
import google.com.healthhigh.controller.NoticiaController;
import google.com.healthhigh.domain.Noticia;

public class NoticiaActivity extends Base implements View.OnClickListener {
    public final int request_code = 1;
    public RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);
        setNoticias();
    }

    private void setNoticias() {
        rv = (RecyclerView) findViewById(R.id.rv_lista_noticias);
        NoticiaController noticia_controller = new NoticiaController(this);
        List<Noticia> noticias = noticia_controller.obterListaNoticias(0);
        rv.setAdapter(new NoticiaAdapter(this, noticias));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
