package me.yokeyword.fragmentation.anim;

import android.os.Parcel;
import android.os.Parcelable;

import me.yokeyword.fragmentation.R;

/**
 * 用于正在播放列表Fragment动画
 * 自下而上
 * */
public class FragmentVerticalAnimator extends FragmentAnimator implements Parcelable {

    public FragmentVerticalAnimator(){
        enter = R.anim.anim_current_list_enter;
        exit = R.anim.anim_current_list_exit;
        popEnter = R.anim.anim_current_list_pop_enter;
        popExit = R.anim.anim_current_list_pop_exit;
    }

    protected FragmentVerticalAnimator(Parcel in){
        super(in);
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FragmentVerticalAnimator> CREATOR = new Creator<FragmentVerticalAnimator>() {
        @Override
        public FragmentVerticalAnimator createFromParcel(Parcel in) {
            return new FragmentVerticalAnimator(in);
        }

        @Override
        public FragmentVerticalAnimator[] newArray(int size) {
            return new FragmentVerticalAnimator[size];
        }
    };

}
