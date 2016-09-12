package com.example.lars.vierrotiert;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class SquareLinearLayout extends LinearLayout {
    public SquareLinearLayout(Context context) {
        super(context);
    }

    public SquareLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        int widthDesc = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightDesc = View.MeasureSpec.getMode(heightMeasureSpec);
        int size = 0;
        if (widthDesc == View.MeasureSpec.UNSPECIFIED
                && heightDesc == View.MeasureSpec.UNSPECIFIED) {
            size = getContext().getResources().getDimensionPixelSize(R.dimen.default_size); // Use your own default size, for example 125dp
        } else if ((widthDesc == View.MeasureSpec.UNSPECIFIED || heightDesc == View.MeasureSpec.UNSPECIFIED)
                && !(widthDesc == View.MeasureSpec.UNSPECIFIED && heightDesc == View.MeasureSpec.UNSPECIFIED)) {
            //Only one of the dimensions has been specified so we choose the dimension that has a value (in the case of unspecified, the value assigned is 0)
            size = Math.min(width, height);
        } else {
            //In all other cases both dimensions have been specified so we choose the smaller of the two
            size = Math.min(width, height);
        }
        setMeasuredDimension(size, size);
    }
}
