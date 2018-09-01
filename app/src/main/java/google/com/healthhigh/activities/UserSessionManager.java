package google.com.healthhigh.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import google.com.healthhigh.utils.SessionManager;

public abstract class UserSessionManager extends AppCompatActivity {
    protected SessionManager pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(pref == null){
            pref = new SessionManager(getApplicationContext().getSharedPreferences(SessionManager.user_session_file, Context.MODE_PRIVATE), this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pref != null){
            pref.verificarLogin();
        }
    }
}
