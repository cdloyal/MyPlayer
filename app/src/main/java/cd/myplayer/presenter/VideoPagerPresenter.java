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
public class VideoPagerPresenter implements IVideoPagerPresenter {

    private final static int VIDEODATA_INITED = 0;
    private final static int AUDIODATA_INITED = 1;
    private Context context;
    private ArrayList<MediaBean> mediaBeans;

    private Handler handler  = new MyHandler(this);
    private IVideoPagerController videoPagerController;

    private static class MyHandler extends BaseHandler<VideoPagerPresenter> {

        MyHandler(VideoPagerPresenter presenter) {
            super(presenter);
        }

        @Override
        public void handlerMessage(VideoPagerPresenter presenter, Message msg) {
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
//                            Intent intent = new Intent();
//                            intent.setDataAndType(Uri.parse(media.getData()),"video/*");
//                            context.startActivity(intent);

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
        new Thread(new MyRunnable(VideoPagerPresenter.this)).start();
    }

    private static class MyRunnable extends BaseRunnable<VideoPagerPresenter> {
        MyRunnable(VideoPagerPresenter presenter) {
            super(presenter);
        }

        @Override
        public void run(VideoPagerPresenter presenter) {
            if(presenter==null)
                return;
            presenter.mediaBeans = MediaUtils.queryLocalVideo(presenter.context);
            presenter.handler.sendEmptyMessage(VIDEODATA_INITED);
        }
    }
}
