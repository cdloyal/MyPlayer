package cd.myplayer.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @Description: java类作用描述
 * @Author: 陈达
 * @CreateDate: 2019/6/28 11:27
 * @UpdateUser: 陈达
 * @UpdateDate: 2019/6/28 11:27
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public abstract class BaseHandler<T> extends Handler {

    private final WeakReference<T> weakReference;

    public BaseHandler(T t) {
        super(Looper.getMainLooper());
        this.weakReference = new WeakReference<T>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T t = weakReference.get();
//        if(presenter==null)
        handlerMessage(t,msg);
    }

    public abstract void handlerMessage(T t,Message msg);
}
