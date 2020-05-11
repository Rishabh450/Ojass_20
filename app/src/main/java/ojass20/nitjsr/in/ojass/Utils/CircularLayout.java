package ojass20.nitjsr.in.ojass.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ojass20.nitjsr.in.ojass.R;

public class CircularLayout extends RelativeLayout {
    private Context mContext;
    public CircularLayout(Context context) {
        super(context);
        mContext = context;
    }

    public CircularLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.mContext = mContext;
    }

    public CircularLayout(Context context, AttributeSet attrs, Context mContext) {
        super(context, attrs);
        this.mContext = mContext;
    }

    public CircularLayout(Context context, AttributeSet attrs, int defStyleAttr, Context mContext) {
        super(context, attrs, defStyleAttr);
        this.mContext = mContext;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
