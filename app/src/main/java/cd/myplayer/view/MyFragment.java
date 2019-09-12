package cd.myplayer.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cd.myplayer.utils.LogUtil;

/**
 * 作者：chenda
 * 时间：2019/8/27:17:16
 * 邮箱：
 * 说明：
 */
@SuppressLint("ValidFragment")
public class MyFragment extends Fragment {
    private View view;

    public static MyFragment newInstance(View view) {
        LogUtil.d("newInstance()");
        Bundle bundle = new Bundle();
        MyFragment myFragment = new MyFragment();
        myFragment.view = view;
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("MyFragment onCreate()");
        LogUtil.d("MyFragment hashCode="+this.hashCode());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d("MyFragment onCreateView()");
        return view;
    }

    @Override
    public void onDestroy() {
        LogUtil.d("MyFragment onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}