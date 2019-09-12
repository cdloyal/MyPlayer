package cd.myplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * 作者：chenda
 * 时间：2019/8/27:16:50
 * 邮箱：
 * 说明：
 */
public class AudioPager extends View{


    public AudioPager(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("本地音频",50,50,new Paint());
    }

}
