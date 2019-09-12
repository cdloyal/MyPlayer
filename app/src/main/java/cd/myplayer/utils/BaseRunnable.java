package cd.myplayer.utils;

import java.lang.ref.WeakReference;

/**
 * 作者：chenda
 * 时间：2019/8/30:9:31
 * 邮箱：
 * 说明：
 */
public abstract class BaseRunnable<T> implements Runnable {

    WeakReference<T> weakReference;

    protected BaseRunnable(T t){
        weakReference = new WeakReference<>(t);
    }
    @Override
    public void run() {
        run(weakReference.get());
    }
    public abstract void run(T t);
}
