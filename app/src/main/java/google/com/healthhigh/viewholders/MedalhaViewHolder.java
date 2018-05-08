package google.com.healthhigh.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.healthhigh.R;

import google.com.healthhigh.activities.DetalhesMedalhaActivity;
import google.com.healthhigh.domain.Item;

/**
 * Created by Alan on 24/06/2017.
 */

public class MedalhaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private Item i;
    final ImageView img;
    final TextView nome;
    final TextView data;
    final TextView XP;

    public MedalhaViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        img = (ImageView) view.findViewById(R.id.imgIconeMedalha);
        nome = (TextView) view.findViewById(R.id.nomeMedalha);
        data = (TextView) view.findViewById(R.id.dataInsercaoMedalha);
        XP = (TextView) view.findViewById(R.id.experienciaMedalha);
        i = null;
    }

    public ImageView getImg() {
        return img;
    }

    public TextView getNome() {
        return nome;
    }

    public TextView getData() {
        return data;
    }

    public TextView getXP() {
        return XP;
    }

    public void setItem(Item i){
        this.i = i;
    }

    @Override
    public void onClick(View v) {
        if(this.i != null){
            Context c = v.getContext();
            Intent i = new Intent(c, DetalhesMedalhaActivity.class);
            i.putExtra("ITEM_ID", this.i.getId());
            c.startActivity(i);
        }
    }
}
