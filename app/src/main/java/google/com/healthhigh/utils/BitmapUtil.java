package google.com.healthhigh.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.healthhigh.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Alan on 15/06/2017.
 * Classe utilizada para trabalhar com imagens
 */

public abstract class BitmapUtil {
    public static byte[] bitmapToByteArray(Bitmap b){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] img){
        return BitmapFactory.decodeByteArray(img, 0, img.length);
    }
}
