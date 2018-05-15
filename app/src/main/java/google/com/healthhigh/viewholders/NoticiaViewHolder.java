package google.com.healthhigh.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.activities.LendoNoticiaActivity;
import google.com.healthhigh.domain.Noticia;

/**
 * Created by Alan on 24/04/2018.
 */

public class NoticiaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView txt_titulo_noticia;
    private TextView txt_status_visualizacao;
    private TextView txt_status_publicacao;
    private TextView txt_descricao;
    private TextView txt_data_criacao;
    private ImageView img_icone_noticia;
    private Button btn_ler;
    private Noticia noticia;
    public NoticiaViewHolder(View v) {
        super(v);
        txt_titulo_noticia = (TextView) v.findViewById(R.id.txt_titulo_noticia);
        txt_descricao = (TextView) v.findViewById(R.id.txt_descricao);
        img_icone_noticia = (ImageView) v.findViewById(R.id.img_icone_noticia);
        txt_status_visualizacao = (TextView) v.findViewById(R.id.txt_data_criacao_noticia);
        txt_status_publicacao = (TextView) v.findViewById(R.id.txt_status_publicacao_desafio);
        txt_data_criacao = (TextView) v.findViewById(R.id.txt_data_criacao_noticia);
        btn_ler = (Button) v.findViewById(R.id.btn_ler);
        btn_ler.setOnClickListener(this);
    }

    public Noticia getNoticia() {
        return noticia;
    }

    public void setNoticia(Noticia noticia) {
        this.noticia = noticia;
    }

    public TextView getTxt_titulo_noticia() {
        return txt_titulo_noticia;
    }

    public ImageView getImg_icone_noticia() {
        return img_icone_noticia;
    }

    public void setTxt_titulo_noticia(TextView txt_titulo_noticia) {
        this.txt_titulo_noticia = txt_titulo_noticia;
    }

    public void setImg_icone_noticia(ImageView img_icone_noticia) {
        this.img_icone_noticia = img_icone_noticia;
    }

    public TextView getTxt_status_visualizacao() {
        return txt_status_visualizacao;
    }

    public void setTxt_status_visualizacao(TextView txt_status_visualizacao) {
        this.txt_status_visualizacao = txt_status_visualizacao;
    }

    public TextView getTxt_data_criacao() {
        return txt_data_criacao;
    }

    public void setTxt_data_criacao(TextView txt_data_criacao) {
        this.txt_data_criacao = txt_data_criacao;
    }

    public TextView getTxt_descricao() {
        return txt_descricao;
    }

    public void setTxt_descricao(TextView txt_descricao) {
        this.txt_descricao = txt_descricao;
    }

    public Button getBtn_ler() {
        return btn_ler;
    }

    public void setBtn_ler(Button btn_ler) {
        this.btn_ler = btn_ler;
    }

    public TextView getTxt_status_publicacao() {
        return txt_status_publicacao;
    }

    public void setTxt_status_publicacao(TextView txt_status_publicacao) {
        this.txt_status_publicacao = txt_status_publicacao;
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        if(noticia != null){
            Intent i = new Intent(context, LendoNoticiaActivity.class);
            i.putExtra(LendoNoticiaActivity.NOTICIA_ID, noticia.getId());
            context.startActivity(i);
        }
    }

}
