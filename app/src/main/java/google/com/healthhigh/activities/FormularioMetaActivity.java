package google.com.healthhigh.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.google.healthhigh.R;

public class FormularioMetaActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edt_txt_name, edt_txt_desc;
    private Button btn_save, btn_cancel;
    private NumberPicker nb_pck_time_period;
    private Spinner spn_reward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_meta);
        getInterfaceElements();
        setElementsEvents();
    }

    public void getInterfaceElements() {
        edt_txt_name = (EditText) findViewById(R.id.formulario_meta_name);
        edt_txt_desc = (EditText) findViewById(R.id.formulario_meta_desc);
        btn_cancel = (Button) findViewById(R.id.formulario_meta_cancel);
        btn_save = (Button) findViewById(R.id.formulario_meta_save);
        nb_pck_time_period = (NumberPicker) findViewById(R.id.formulario_meta_period);
        spn_reward = (Spinner) findViewById(R.id.formulario_meta_reward);
    }

    public void setElementsEvents(){
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.formulario_meta_cancel:
                finish();
            break;
        }
    }
}
