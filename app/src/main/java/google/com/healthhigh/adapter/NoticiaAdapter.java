package google.com.healthhigh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.healthhigh.R;

import java.util.List;

import google.com.healthhigh.domain.Noticia;
import google.com.healthhigh.utils.DataHelper;
import google.com.healthhigh.viewholders.NoticiaViewHolder;

/**
 * Created by Alan on 24/04/2018.
 */

public class NoticiaAdapter extends RecyclerView.Adapter {
    public List<Noticia> noticias;
    public Context context;

    public NoticiaAdapter(Context c, List<Noticia> noticias){
        context = c;
        this.noticias = noticias;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_linha_noticia, parent, false);
        return new NoticiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NoticiaViewHolder n_holder = (NoticiaViewHolder) holder;
        Noticia n = noticias.get(position);
        n_holder.getTxt_titulo_noticia().setText(n.getTitulo());
        n_holder.setNoticia(n);
        n_holder.getTxt_descricao().setText(n.getCorpo());
        n_holder.getTxt_status_visualizacao().setVisibility(n.getData_visualizacao() != 0 ? View.INVISIBLE : View.VISIBLE);
        n_holder.getTxt_data_criacao().setText(DataHelper.toDateString(n.getData_criacao()));
        if(n.getDesafio_atual() != null){
            // Publicação em vigência
            if(n.getDesafio_atual().getPublicacao().getData_fim() > DataHelper.now()){
                //Publicação da notícia já foi visualizada
                if(n.getInteracao().getData_visualizacao() > 0){
                    n_holder.getTxt_status_publicacao().setText("Publicação Lida");
                } else {
                    n_holder.getTxt_status_publicacao().setText("Publicação Nova");
                }
            } else {
                n_holder.getTxt_status_publicacao().setText("Publicação encerrada");
            }
        } else {
            n_holder.getTxt_status_publicacao().setText("Desafio não aceito");
        }
    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }
}
