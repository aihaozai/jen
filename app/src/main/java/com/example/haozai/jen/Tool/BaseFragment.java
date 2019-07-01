package com.example.haozai.jen.Tool;

/**
 * Created by haozai on 2018-06-13.
 */

import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    //Fragment的View离开的标记
    protected boolean viewSwiper;
    /** Fragment当前状态是否可见 */
    protected boolean isVisible;

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser );
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    /**
     * 可见
     */
    protected void onVisible() {
        Log.i(TAG,isVisible+"base-----onVisible");
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
        Log.i(TAG,isVisible+"base----onisVisible");
        lazyLoad();
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();

}