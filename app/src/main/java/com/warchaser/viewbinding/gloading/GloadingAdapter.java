package com.warchaser.viewbinding.gloading;

import android.view.View;

import com.warchaser.libbase.ui.gloading.Gloading;

public class GloadingAdapter implements Gloading.Adapter {

    @Override
    @SuppressWarnings("unused")
    public View getView(Gloading.Holder holder, View convertView, int status, String emptyHint, int type) {
        GLoadingView gloadingView = null;

        if(convertView instanceof GLoadingView){
            gloadingView = (GLoadingView) convertView;
        }

        if(gloadingView == null){
            gloadingView = new GLoadingView(holder.getContext(), holder.getRetryTask(), type);
        }

        gloadingView.setStatus(status, emptyHint);


        return gloadingView;
    }
}
