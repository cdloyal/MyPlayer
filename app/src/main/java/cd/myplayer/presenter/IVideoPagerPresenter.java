package cd.myplayer.presenter;

import android.content.Context;
import android.widget.AdapterView;

import cd.myplayer.viewcontroller.IVideoPagerController;

/**
 * 作者：chenda
 * 时间：2019/8/30:10:30
 * 邮箱：
 * 说明：
 */
public interface IVideoPagerPresenter {

    public void setContext(Context context);
    public void initData();
    public AdapterView.OnItemClickListener getOnItemClickListener();

    public void setIVideoPagerController(IVideoPagerController videoPagerController);

}
