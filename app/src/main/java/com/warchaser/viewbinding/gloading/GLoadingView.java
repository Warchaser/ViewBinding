package com.warchaser.viewbinding.gloading;


import static com.warchaser.libbase.ui.gloading.Gloading.STATUS_EMPTY_DATA;
import static com.warchaser.libbase.ui.gloading.Gloading.STATUS_LOADING;
import static com.warchaser.libbase.ui.gloading.Gloading.STATUS_LOAD_FAILED;
import static com.warchaser.libbase.ui.gloading.Gloading.STATUS_LOAD_SUCCESS;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.warchaser.libbase.ui.gloading.Gloading;
import com.warchaser.viewbinding.R;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class GLoadingView extends RelativeLayout {

    private Runnable mRetryTask;

    private LinearLayout mLyLoadFailed;
    private LinearLayout mLyEmptyData;
    private LinearLayout mLoadingAnim;
    private TextView mBtnRetry;
    private TextView mTvEmptyHint;

    public GLoadingView(Context context) {
        super(context);
    }

    public GLoadingView(Context context, Runnable retryTask, int type) {
        super(context);
        if(type == Gloading.TYPE_DEFAULT){
            LayoutInflater.from(context).inflate(R.layout.gloading_hint_error, this, true);
        } else {
            LayoutInflater.from(context).inflate(R.layout.gloading_hint_error_custom, this, true);
        }

        mLyLoadFailed = findViewById(R.id.mLyLoadFailed);
        mLyEmptyData = findViewById(R.id.mLyEmptyData);
        mLoadingAnim = findViewById(R.id.mLoadingAnim);
        mBtnRetry = findViewById(R.id.mBtnRetry);
        mTvEmptyHint = findViewById(R.id.mTvEmptyHint);

        mBtnRetry.setOnClickListener(v -> mRetryTask.run());

        mRetryTask = retryTask;
    }

    public void setStatus(int status, String emptyHint){
        boolean show = true;
        switch (status){
            case STATUS_LOAD_SUCCESS:
                show = false;
                break;
            case STATUS_LOADING:
                mLoadingAnim.setVisibility(View.VISIBLE);
                mLyLoadFailed.setVisibility(View.GONE);
                mLyEmptyData.setVisibility(View.GONE);
                break;
            case STATUS_LOAD_FAILED:
                mLyLoadFailed.setVisibility(View.VISIBLE);
                mLoadingAnim.setVisibility(View.GONE);
                mLyEmptyData.setVisibility(View.GONE);
                break;
            case STATUS_EMPTY_DATA:
                if(!TextUtils.isEmpty(emptyHint)){
                    mTvEmptyHint.setText(emptyHint);
                }
                mLyEmptyData.setVisibility(View.VISIBLE);
                mLyLoadFailed.setVisibility(View.GONE);
                mLoadingAnim.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
