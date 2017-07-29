package google.com.healthhigh.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import google.com.healthhigh.activities.DetalhesDesafios;
import google.com.healthhigh.dao.DesafioDAO;
import google.com.healthhigh.domain.Desafio;

import java.util.List;

import com.google.healthhigh.R;
public class DesafioList implements View.OnClickListener{
    private LinearLayout listaDesafios;
    private DesafioDAO dao = null;
    private Context c = null;

    public DesafioList(Context c, LinearLayout ListaDesafio){
        listaDesafios = ListaDesafio;
        this.c = c;
        dao = new DesafioDAO(c);
    }

    public void setDesafiosPreview(List<Desafio> desafios){
        LayoutInflater i = LayoutInflater.from(c);
        for(Desafio x : desafios){
            View v = i.inflate(R.layout.layout_linha_desafio, null);
            ImageView iv = (ImageView) v.findViewById(R.id.iconeDesafio);
            TextView tv = (TextView) v.findViewById(R.id.tituloDesafio);
            TextView cv = (TextView) v.findViewById(R.id.contadorTentativas);
            TextView dv = (TextView) v.findViewById(R.id.descricaoDesafio);
            iv.setImageResource(R.drawable.corrida);
            tv.setText(x.getTitulo());
            cv.setText(Integer.toString(x.getTentativas()));
            dv.setText(x.getDescricao());
            v.setId((int) x.getId());
            v.setOnClickListener(this);
            listaDesafios.addView(v);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(c, DetalhesDesafios.class);
        intent.putExtra("desafio_id", v.getId());
        c.startActivity(intent);
    }

    class viewHolder {
        final TextView titulo;
        final TextView descricao;
        final ImageView foto;
        final TextView tentativas;

        public viewHolder(View v){
            foto = (ImageView) v.findViewById(R.id.iconeDesafio);
            titulo = (TextView) v.findViewById(R.id.tituloDesafio);
            tentativas = (TextView) v.findViewById(R.id.contadorTentativas);
            descricao = (TextView) v.findViewById(R.id.descricaoDesafio);
        }
    }

}
