package cd.myplayer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import cd.myplayer.R;
import cd.myplayer.utils.LogUtil;
import cd.myplayer.utils.StatusBarUtil;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;

    private Handler handler = new Handler();
    private RelativeLayout rl_welcome;
    private Runnable mainRunnable = new Runnable() {
        @Override
        public void run() {
            startMainActivity();
        }
    };
    private boolean isStartMainActivity = false;

    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //这里注意下 因为在评论区发现有网友调用setRootViewFitsSystemWindows 里面 winContent.getChildCount()=0 导致代码无法继续
        //是因为你需要在setContentView之后才可以调用 setRootViewFitsSystemWindows

        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
//        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
//        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
//        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
//        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
//            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
//            //这样半透明+白=灰, 状态栏的文字能看得清
//            StatusBarUtil.setStatusBarColor(this,0xffffffff);
        }
//        StatusBarUtil.setStatusBarColor(this, Color.RED);

        rl_welcome = findViewById(R.id.rl_welcome);
        rl_welcome.setOnClickListener(this);

        if (!requestPermissions()) {
            handler.postDelayed(mainRunnable ,3000);
        }


    }

    private boolean requestPermissions() {
        ArrayList<String> toApplyList = new ArrayList<String>();
        for (String perm : NEEDED_PERMISSIONS) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), ACTION_REQUEST_PERMISSIONS);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int i = 0; i < permissions.length; i++) {
                isAllGranted &= (grantResults[i] == PackageManager.PERMISSION_GRANTED);
            }
            if (isAllGranted) {
                handler.postDelayed(mainRunnable ,3000);
            } else {
                Toast.makeText(this,"权限不够",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startMainActivity(){
        if(!isStartMainActivity){
            isStartMainActivity = true;
            //singleTask可以使得点击不创建多个activity，但是还是用标志位好
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        LogUtil.d("onDestroy()");
//        handler.removeCallbacks(mainRunnable);  //为了解决3秒内打开，退出，3秒后消息触发弹出Activity
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        startMainActivity();
    }
}
