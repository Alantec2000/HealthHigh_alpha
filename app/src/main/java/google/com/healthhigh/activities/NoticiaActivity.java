package google.com.healthhigh.activities;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import google.com.healthhigh.base.Base;
import com.google.healthhigh.R;

public class NoticiaActivity extends Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutNoticia);

         for(int i=0; i<100;i++){
             LayoutInflater inflater = LayoutInflater.from(this);

             //Cria o textView inflando o arquivo de layout, "infla o xml".
             TextView text = (TextView) inflater.inflate(R.layout.inflate_textview, layout,false);

             //agora basta usar a view inflada normalmente
             text.setText("texto: "+i);
             layout.addView(text);
        }
    }
}
