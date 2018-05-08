package google.com.healthhigh.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.healthhigh.R;

import google.com.healthhigh.base.Base;
import google.com.healthhigh.dao.Questionario_QuestaoAlternativaDAO;
import google.com.healthhigh.domain.Questionario;

public class QuestionarioFakeActivity extends Base implements View.OnClickListener{
    public static String QUESTIONARIO_ID = "ID_QUESTIONARIO";
    private RecyclerView rv = null;
    private Questionario_QuestaoAlternativaDAO questionario_questao_dao;
    private Questionario questionario;
    Intent self_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_questionario_fake);
        self_intent = this.getIntent();
        Button responder = (Button) findViewById(R.id.responder_questionario_fake);
        responder.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        RadioGroup rg = (RadioGroup) findViewById(R.id.opcoes_questao_1);
        int opt = rg.getCheckedRadioButtonId();
        String text = "";
        if(opt > -1){
            RadioButton rb = (RadioButton) findViewById(opt);
            text = rb.getText().toString();
        }
        outState.putString("resposta_quantitativa_1", text);

        EditText txt = (EditText) findViewById(R.id.questao_fake_resposta_qualitativa_1);
        outState.putString("resposta_qualitativa_1",txt.getText().toString());

        rg = (RadioGroup) findViewById(R.id.opcoes_questao_2);
        opt = rg.getCheckedRadioButtonId();
        text = "";
        if(opt > -1){
            RadioButton rb = (RadioButton) findViewById(opt);
            text = rb.getText().toString();
        }
        outState.putString("resposta_quantitativa_2",text);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        Intent data = new Intent();
        RadioGroup rg = (RadioGroup) findViewById(R.id.opcoes_questao_1);
        int opt = rg.getCheckedRadioButtonId();
        String text = "";
        if(opt > -1){
            RadioButton rb = (RadioButton) findViewById(opt);
            text = rb.getText().toString();
        }
        data.putExtra("resposta_quantitativa_1", text);

        EditText txt = (EditText) findViewById(R.id.questao_fake_resposta_qualitativa_1);
        data.putExtra("resposta_qualitativa_1",txt.getText().toString());

        rg = (RadioGroup) findViewById(R.id.opcoes_questao_2);
        opt = rg.getCheckedRadioButtonId();
        text = "";
        if(opt > -1){
            RadioButton rb = (RadioButton) findViewById(opt);
            text = rb.getText().toString();
        }
        data.putExtra("resposta_quantitativa_2",text);
        setResult(RESULT_OK, data);
        finish();
    }
}
