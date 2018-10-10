package com.lovejjfg.swiperefreshrecycleview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.lovejjfg.powerrecycle.PowerAdapter;

/**
 * Created by Carson_Ho on 17/6/8.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    // 写右边字的画笔(具体信息)
    private Paint mPaint;

    // 写左边日期字的画笔( 时间 + 日期)
    private Paint mPaint1;
    private Paint mPaint2;

    // 左 上偏移长度
    private int itemView_leftinterval;
    private int itemView_topinterval;

    // 轴点半径
    private int circle_radius;

    // 图标
    private Bitmap mIcon;

    // 在构造函数里进行绘制的初始化，如画笔属性设置等
    public DividerItemDecoration(Context context) {

        // 轴点画笔(红色)
        mPaint = new Paint();
        mPaint.setColor(Color.RED);

        // 左边时间文本画笔(蓝色)
        // 此处设置了两只分别设置 时分 & 年月
        mPaint1 = new Paint();
        mPaint1.setColor(Color.BLUE);
        mPaint1.setTextSize(30);

        mPaint2 = new Paint();
        mPaint2.setColor(Color.BLUE);

        // 赋值ItemView的左偏移长度为200
        itemView_leftinterval = 200;

        // 赋值ItemView的上偏移长度为50
        itemView_topinterval = 50;

        // 赋值轴点圆的半径为10
        circle_radius = 10;

        // 获取图标资源
        mIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
    }

    // 重写getItemOffsets（）方法
    // 作用：设置ItemView 左 & 上偏移长度
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // 设置ItemView的左 & 上偏移长度分别为200 px & 50px,即此为onDraw()可绘制的区域
    }

    // 重写onDraw（）
    // 作用:在间隔区域里绘制时光轴线 & 时间文本
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        // 获取RecyclerView的Child view的个数
        int childCount = parent.getChildCount();
        RecyclerView.Adapter adapter = parent.getAdapter();
        PowerAdapter powerAdapter;
        if (adapter instanceof PowerAdapter) {
            powerAdapter = (PowerAdapter) adapter;
        }
        // 遍历每个Item，分别获取它们的位置信息，然后再绘制对应的分割线
        for (int i = 0; i < childCount; i++) {

            // 获取每个Item对象
            View child = parent.getChildAt(i);
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int position = parent.getChildAdapterPosition(child);
            if (position % 2 != 0) {
                continue;
            }
            if (adapter.getItemViewType(position) != 0) {
                continue;
            }
            System.out.println("current index:" + i + ";;getChildAdapterPosition:" + position);

            /**
             * 绘制轴点
             */
            // 轴点 = 圆 = 圆心(x,y)
            float centerx = child.getLeft() - itemView_leftinterval / 3;
            float centery = child.getTop() - itemView_topinterval + (itemView_topinterval + child.getHeight()) / 2;
            // 绘制轴点圆
            if (position % 3 == 0) {
                c.drawCircle(centerx, centery, circle_radius, mPaint);
            }
            // 通过Canvas绘制角标
            // c.drawBitmap(mIcon,centerx - circle_radius ,centery - circle_radius,mPaint);

            /**
             * 绘制上半轴线
             */
            // 上端点坐标(x,y)
            float upLine_up_x = centerx;
            float upLine_up_y = child.getTop() - itemView_topinterval;

            // 下端点坐标(x,y)
            float upLine_bottom_x = centerx;
            float upLine_bottom_y = centery;

            //绘制上半部轴线
            c.drawLine(upLine_up_x, upLine_up_y, upLine_bottom_x, upLine_bottom_y, mPaint);

            /**
             * 绘制下半轴线
             */

            // 上端点坐标(x,y)
            float bottomLine_up_x = centerx;
            float bottom_up_y = centery;

            // 下端点坐标(x,y)
            float bottomLine_bottom_x = centerx;
            float bottomLine_bottom_y = child.getBottom();

            //绘制下半部轴线
            c.drawLine(bottomLine_up_x, bottom_up_y, bottomLine_bottom_x, bottomLine_bottom_y, mPaint);

            /**
             * 绘制左边时间文本
             */
            // 获取每个Item的位置
            int index = parent.getChildAdapterPosition(child);
            // 设置文本起始坐标
            float Text_x = child.getLeft() - itemView_leftinterval * 5 / 6;
            float Text_y = upLine_bottom_y;

            // 根据Item位置设置时间文本
            //switch (index) {
            //    case (0):
            //        // 设置日期绘制位置
            //        c.drawText("13:40", Text_x, Text_y, mPaint1);
            //        c.drawText("2017.4.03", Text_x + 5, Text_y + 20, mPaint2);
            //        break;
            //    case (1):
            //        // 设置日期绘制位置
            //        c.drawText("17:33", Text_x, Text_y, mPaint1);
            //        c.drawText("2017.4.03", Text_x + 5, Text_y + 20, mPaint2);
            //        break;
            //    case (2):
            //        // 设置日期绘制位置
            //        c.drawText("18:11", Text_x, Text_y, mPaint1);
            //        c.drawText("2017.4.03", Text_x + 5, Text_y + 20, mPaint2);
            //        break;
            //    case (3):
            //        // 设置日期绘制位置
            //        c.drawText("9:40", Text_x, Text_y, mPaint1);
            //        c.drawText("2017.4.04", Text_x + 5, Text_y + 20, mPaint2);
            //        break;
            //    case (4):
            //        // 设置日期绘制位置
            //        c.drawText("13:20", Text_x, Text_y, mPaint1);
            //        c.drawText("2017.4.04", Text_x + 5, Text_y + 20, mPaint2);
            //        break;
            //    case (5):
            //        // 设置日期绘制位置
            //        c.drawText("20:40", Text_x, Text_y, mPaint1);
            //        c.drawText("2017.4.04", Text_x + 5, Text_y + 20, mPaint2);
            //        break;
            //    default:c.drawText("已签收", Text_x, Text_y, mPaint1);
            //}
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
}
