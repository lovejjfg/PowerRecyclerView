package com.lovejjfg.swiperefreshrecycleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Joe on 2017/4/2..
 * Email lovejjfg@gmail.com
 */


public class ViewUtils {


    public static void calculateTag1(TextView first, TextView second, final String text) {
        ViewTreeObserver observer = first.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Layout layout = first.getLayout();
                int lineEnd = layout.getLineEnd(0);
                String substring = text.substring(0, lineEnd);
                String substring1 = text.substring(lineEnd, text.length());
                Log.i("TAG", "onGlobalLayout:" + "+end:" + lineEnd);
                Log.i("TAG", "onGlobalLayout: 第一行的内容：：" + substring);
                Log.i("TAG", "onGlobalLayout: 第二行的内容：：" + substring1);
                if (TextUtils.isEmpty(substring1)) {
                    second.setVisibility(View.GONE);
                    second.setText(null);
                } else {
                    second.setVisibility(View.VISIBLE);
                    second.setText(substring1);
                }
                first.getViewTreeObserver().removeOnPreDrawListener(
                        this);
                return false;
            }
        });

    }

    public static void calculateTag2(TextView tag, TextView title, final String text) {
        ViewTreeObserver observer = tag.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                SpannableString spannableString = new SpannableString(text);
                LeadingMarginSpan.Standard what = new LeadingMarginSpan.Standard(tag.getWidth() + dip2px(tag.getContext(), 10), 0);
                spannableString.setSpan(what, 0, spannableString.length(), SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
                title.setText(spannableString);
                tag.getViewTreeObserver().removeOnPreDrawListener(
                        this);
                return false;
            }
        });

    }

    public static int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }

    public static class CenteredImageSpan extends ImageSpan {
        private WeakReference<Drawable> mDrawableRef;

        public CenteredImageSpan(Context context, final int drawableRes) {
            super(context, drawableRes);
        }

        @Override
        public int getSize(Paint paint, CharSequence text,
                           int start, int end,
                           Paint.FontMetricsInt fm) {
            Drawable d = getCachedDrawable();
            Rect rect = d.getBounds();

            if (fm != null) {
                Paint.FontMetricsInt pfm = paint.getFontMetricsInt();
                // keep it the same as paint's fm
                fm.ascent = pfm.ascent;
                fm.descent = pfm.descent;
                fm.top = pfm.top;
                fm.bottom = pfm.bottom;
            }

            return rect.right;
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text,
                         int start, int end, float x,
                         int top, int y, int bottom, @NonNull Paint paint) {
            Drawable b = getCachedDrawable();
            canvas.save();

            int drawableHeight = b.getIntrinsicHeight();
            int fontAscent = paint.getFontMetricsInt().ascent;
            int fontDescent = paint.getFontMetricsInt().descent;
            int transY = bottom - b.getBounds().bottom + (drawableHeight - fontDescent + fontAscent) / 2;

            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }

        // Redefined locally because it is a private member from DynamicDrawableSpan
        private Drawable getCachedDrawable() {
            WeakReference<Drawable> wr = mDrawableRef;
            Drawable d = null;

            if (wr != null) {
                d = wr.get();
            }

            if (d == null) {
                d = getDrawable();
                mDrawableRef = new WeakReference<>(d);
            }

            return d;
        }
    }
}
