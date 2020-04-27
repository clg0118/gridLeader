package xxy.com.gridleader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Betty Li on 2018/3/8.
 */

public class CircleView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mColor = Color.parseColor("#5AC4B3");
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);

        textPaint.setColor(Color.WHITE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(400,200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        RectF rectF = new RectF(0,0,400,400);
        canvas.drawArc(rectF,180,180,false,mPaint);

    }
}
