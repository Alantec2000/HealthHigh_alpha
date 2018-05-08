package google.com.healthhigh.utils;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Alan on 12/08/2017.
 */

public abstract class MessageDialog {
    private static AlertDialog ad;
    public static void showMessage(Context c, String msg, String titulo){
        final AlertDialog ad = new AlertDialog.Builder(c).create();
        ad.setTitle(titulo);
        ad.setMessage(msg);
        ad.show();
    }
}
