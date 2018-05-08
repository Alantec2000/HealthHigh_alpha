package google.com.healthhigh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.healthhigh.R;

import java.util.List;

import google.com.healthhigh.domain.Alternativa;
import google.com.healthhigh.viewholders.AlternativaViewHolder;

/**
 * Created by Alan on 23/04/2018.
 */

public class AlternativaAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Alternativa> alternativas;

    public AlternativaAdapter(Context c, List<Alternativa> a){
        this.context = c;
        this.alternativas = a;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_linha_alternativa, parent, false);
        return new AlternativaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AlternativaViewHolder a_holder = (AlternativaViewHolder) holder;
        Alternativa a = alternativas.get(position);
        a_holder.getCb_alternativa().setText(a.getDescricao());
        a_holder.setAlternativa(a);
    }

    @Override
    public int getItemCount() {
        return alternativas.size();
    }
}
