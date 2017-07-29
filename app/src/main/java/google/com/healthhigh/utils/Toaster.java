package google.com.healthhigh.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Alan on 15/06/2017.
 */

public abstract class Toaster {
    private static int l_s = Toast.LENGTH_SHORT,
                       l_l = Toast.LENGTH_LONG;
    private Toaster(){}
    public static void toastShortMessage(Context c, String m){
        Toast.makeText(c, m, l_s).show();
    }
    public static void toastShortMessage(Context c, int m){
        Toast.makeText(c, Integer.toString(m), l_s).show();
    }
    public static void toastShortMessage(Context c, float m){
        Toast.makeText(c, Float.toString(m), l_s).show();
    }
    public static void toastShortMessage(Context c, double m){
        Toast.makeText(c, Double.toString(m), l_s).show();
    }
    public static void toastShortMessage(Context c, long m){
        Toast.makeText(c, Long.toString(m), l_s).show();
    }
    public static void toastLongMessage(Context c, String m){
        Toast.makeText(c, m, l_l).show();
    }
}
