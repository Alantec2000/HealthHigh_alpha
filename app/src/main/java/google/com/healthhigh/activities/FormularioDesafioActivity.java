package google.com.healthhigh.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.healthhigh.R;

public class FormularioDesafioActivity extends AppCompatActivity implements View.OnClickListener{
    EditText edt_txt_name, edt_txt_desc;
    Button btn_add_meta, btn_upd_meta, btn_cancel, btn_save_new_desafio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_desafio);
        getInterfaceElements();
        setButtonEvents();
    }

    private void getInterfaceElements(){
        edt_txt_name = (EditText) findViewById(R.id.formulario_desafio_name);
        edt_txt_desc = (EditText) findViewById(R.id.formulario_desafio_desc);
        btn_add_meta = (Button) findViewById(R.id.formulario_desafio_add_meta);
        btn_upd_meta = (Button) findViewById(R.id.formulario_desafio_upd_meta);
        btn_cancel = (Button) findViewById(R.id.formulario_desafio_cancel);
        btn_save_new_desafio = (Button) findViewById(R.id.formulario_desafio_save);
    }

    private void setButtonEvents() {
        btn_cancel.setOnClickListener(this);
        btn_add_meta.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.formulario_desafio_cancel:
                finish();
            break;
            case R.id.formulario_desafio_add_meta:
                Intent i = new Intent(this,FormularioMetaActivity.class);
                startActivity(i);
            break;
        }
    }
}
