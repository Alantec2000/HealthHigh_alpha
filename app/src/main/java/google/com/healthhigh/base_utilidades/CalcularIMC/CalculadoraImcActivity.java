package google.com.healthhigh.base_utilidades.CalcularIMC;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.healthhigh.R;

import java.util.Locale;

import google.com.healthhigh.base.Base;

public class CalculadoraImcActivity extends Base {

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_imc);

        Button btnCalcular = (Button) findViewById(R.id.btnCalcularIMC);
        btnCalcular.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText altura = (EditText) findViewById(R.id.txtViewAltura);
                EditText peso = (EditText) findViewById(R.id.txtViewPeso);

                //valida se a altura e o peso não estão nulos(vazios) para assim poder realizar os calculos
                if(!altura.getText().toString().isEmpty() && !peso.getText().toString().isEmpty()) {
                    //faz o cast da altura e do peso para se tornarem Double
                    double a = Double.parseDouble(altura.getText().toString());
                    double p = Double.parseDouble(peso.getText().toString());
                    double imc = calcularIMC(p, a);

                    //responsável por fazer com que o imc só seja mostrado com duas casas decimais
                    imc = Double.valueOf(String.format(Locale.US, "%.2f", imc));

                    Toast.makeText(CalculadoraImcActivity.this, mensagemAoUsuario(imc), Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(CalculadoraImcActivity.this, "Você precisa preencher todos os campos!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public double calcularIMC(double peso, double altura){
        return peso / Math.pow(altura, 2);
    }
    public String mensagemAoUsuario(double imc){

        if(imc < 17){
            return "Você está muito abaixo do Peso IMC = " +imc ;
        } else if(imc >= 17 && imc <= 18.49){
            return "Você está abaixo do Peso IMC = " +imc;
        } else if(imc >= 18.5 && imc <= 24.99){
            return "Você está com o Peso normal IMC = " +imc;
        } else if(imc >= 25 && imc <= 29.99){
            return "Você está acima do Peso IMC = " +imc;
        } else if(imc >= 30 && imc <= 34.99){
            return "Você está com nível 1 de Obesidade IMC = " +imc;
        } else if(imc >= 35 && imc <= 39.99){
            return "Você está com obesidade Severa IMC = " +imc;
        } else if(imc >= 40){
            return "Você está com obesidade Mórbida IMC = " +imc;
        }
        return null;
    }
}
