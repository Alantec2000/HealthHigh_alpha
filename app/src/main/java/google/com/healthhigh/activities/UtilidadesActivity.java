package google.com.healthhigh.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.healthhigh.R;

import google.com.healthhigh.base.Base;
import google.com.healthhigh.base_utilidades.CalcularIMC.CalculadoraImcActivity;
import google.com.healthhigh.base_utilidades.DicaDietaActivity;
import google.com.healthhigh.base_utilidades.DicaExercicioActivity;

public class UtilidadesActivity extends Base {
    private ImageButton btnIMC, btnExer, btnDieta;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilidades);

        btnIMC = (ImageButton) findViewById(R.id.imagenButtonIMC);
        btnIMC.setOnClickListener(onClickIMC());
        btnExer = (ImageButton) findViewById(R.id.imagenButtonExer);
        btnExer.setOnClickListener(onClickExer());
        btnDieta = (ImageButton) findViewById(R.id.imagenButtonDieta);
        btnDieta.setOnClickListener(onClickDica());
    }
    private View.OnClickListener onClickIMC() {//método do clique do botão
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), CalculadoraImcActivity.class);
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener onClickExer() {//método do clique do botão
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), DicaExercicioActivity.class);
                startActivity(intent);
            }
        };
    }
    private View.OnClickListener onClickDica() {//método do clique do botão
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getContext(), DicaDietaActivity.class);
                startActivity(intent);
            }
        };
    }
    private Context getContext() {
        return this;
    }
}