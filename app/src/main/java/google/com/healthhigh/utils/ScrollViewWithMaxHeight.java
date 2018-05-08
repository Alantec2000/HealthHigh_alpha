package google.com.healthhigh.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.widget.NestedScrollView;
import android.util.TypedValue;

/**
 * Created by Alan on 17/06/2017.
 */

public class ScrollViewWithMaxHeight extends NestedScrollView{

    public static final int maxHeight = 100; // 100dp

    public ScrollViewWithMaxHeight(Context context) {
        super(context);
    }
    // default constructors

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(dpToPx(getResources(),maxHeight), MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    private int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,     res.getDisplayMetrics());
    }
}