package cd.myplayer.viewcontroller;

import java.util.ArrayList;

import cd.myplayer.bean.MediaBean;

/**
 * 作者：chenda
 * 时间：2019/8/30:10:52
 * 邮箱：
 * 说明：
 */
public interface IVideoPagerController {
    public void onVideoDataInited(ArrayList<MediaBean> mediaBeans);
}
