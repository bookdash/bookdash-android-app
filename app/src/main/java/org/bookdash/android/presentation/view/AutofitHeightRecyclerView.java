package org.bookdash.android.presentation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.bookdash.android.R;


public class AutofitHeightRecyclerView extends RecyclerView {


    private int rowHeight = -1;

    public AutofitHeightRecyclerView(Context context) {
        super(context);
    }

    public AutofitHeightRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AutofitHeightRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    R.attr.rowHeight
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            rowHeight = array.getDimensionPixelSize(0, -1);
            array.recycle();
        }

    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (rowHeight > 0 && getAdapter() != null && getAdapter().getItemCount() != 0) {
            int numberCols = 1;

            int numberOfItems = getAdapter().getItemCount();

            double minimumHeight = Math.ceil((double) (numberOfItems) / numberCols) * (rowHeight + 1);
            setMinimumHeight((int) minimumHeight);

        }
        super.onMeasure(widthSpec, heightSpec);

    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }


}
