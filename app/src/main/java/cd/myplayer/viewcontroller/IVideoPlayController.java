package cd.myplayer.viewcontroller;

import android.os.Bundle;

import java.util.ArrayList;

import cd.myplayer.bean.MediaBean;

/**
 * 作者：chenda
 * 时间：2019/8/30:10:54
 * 邮箱：
 * 说明：
 */
public interface IVideoPlayController {
//    public void play(Uri uri);

    void setMediaBeans(ArrayList<MediaBean> mediaBeans);

    void play(int index);

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);

    void onStop();

}
