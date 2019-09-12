package cd.myplayer.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

import cd.myplayer.activity.VideoPlayerActivity;
import cd.myplayer.bean.MediaBean;
import cd.myplayer.utils.BaseHandler;
import cd.myplayer.utils.BaseRunnable;
import cd.myplayer.utils.LogUtil;
import cd.myplayer.utils.MediaUtils;
import cd.myplayer.viewcontroller.IVideoPagerController;

/**
 * 作者：chenda
 * 时间：2019/8/29:15:49
 * 邮箱：
 * 说明：
 */
public class VideoNetPagerPresenter implements IVideoPagerPresenter {

    private final static int VIDEODATA_INITED = 0;
    private final static int AUDIODATA_INITED = 1;
    private Context context;
    private ArrayList<MediaBean> mediaBeans;

    private Handler handler  = new MyHandler(this);
    private IVideoPagerController videoPagerController;

    private static class MyHandler extends BaseHandler<VideoNetPagerPresenter> {

        MyHandler(VideoNetPagerPresenter presenter) {
            super(presenter);
        }

        @Override
        public void handlerMessage(VideoNetPagerPresenter presenter, Message msg) {
            if(presenter==null)
                return;

            switch (msg.what){
                case VIDEODATA_INITED:
                    presenter.videoPagerController.onVideoDataInited(presenter.mediaBeans);
                    break;
                case AUDIODATA_INITED:
                    break;
            }
        }
    }

    public void setContext(Context context){
        this.context = context;
    }


    public void initData() {
        initVideoDate();
    }

    @Override
    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.d("media.getData()="+mediaBeans.get(position).getData()+",media.getDisplayName()="+mediaBeans.get(position).getDisplayName()+",media.getSize()="+mediaBeans.get(position).getSize()+",media.getDuration()="+mediaBeans.get(position).getDuration());

                //请求第三访播放器
//                Intent intent = new Intent();
//                intent.setDataAndType(Uri.parse(media.getData()),"video/*");
//                context.startActivity(intent);

//                Intent intent = new Intent(context, VideoPlayerActivity.class);
//                intent.setDataAndType(Uri.parse(mediaBeans.get(position).getData()),"video/*");
//                context.startActivity(intent);

                //传整个列表
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("mediaBeans",mediaBeans);
                bundle.putInt("index",position);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        };
    }

    @Override
    public void setIVideoPagerController(IVideoPagerController videoPagerController) {
        this.videoPagerController = videoPagerController;
    }

    private void initVideoDate() {
        //本地查找也费时间，要在子线程完成
        new Thread(new MyRunnable(VideoNetPagerPresenter.this)).start();
    }

    private static class MyRunnable extends BaseRunnable<VideoNetPagerPresenter> {
        MyRunnable(VideoNetPagerPresenter presenter) {
            super(presenter);
        }

        @Override
        public void run(VideoNetPagerPresenter presenter) {
            if(presenter==null)
                return;

//            presenter.mediaBeans = MediaUtils.queryLocalVideo(presenter.context);
//            presenter.handler.sendEmptyMessage(VIDEODATA_INITED);

            presenter.mediaBeans = new ArrayList<>();
            presenter.mediaBeans.add(new MediaBean(1024, "netVideo", 1024, "https://showapi.oss-cn-hangzhou.aliyuncs.com/modleapi/wjy/%E8%A7%86%E9%A2%91.mp4"));
//            presenter.mediaBeans.add(new MediaBean(1024, "netVideo", 1024, "magnet:?xt=urn:btih:ab0968ef431ac0d4059a38ed497a79a52f3bf0b8&dn=%E6%89%AB%E6%AF%922%EF%BC%9A%E5%A4%A9%E5%9C%B0%E5%AF%B9%E5%86%B3HD%E5%9B%BD%E7%B2%A4%E5%8F%8C%E8%AF%AD%E4%B8%AD%E8%8B%B1%E5%8F%8C%E5%AD%97[%E7%94%B5%E5%BD%B1%E5%A4%A9%E5%A0%82www.dy2018.com].mkv&tr=http://t.t789.me:2710/announce&tr=http://t.t789.co:2710/announce&tr=http://t.t789.vip:2710/announce"));
            presenter.handler.sendEmptyMessage(VIDEODATA_INITED);

        }
    }
}
