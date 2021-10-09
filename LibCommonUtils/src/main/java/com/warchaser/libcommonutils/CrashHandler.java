package com.warchaser.libcommonutils;

final public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private final String TAG = "CrashHandler";

    // 系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static CrashHandler INSTANCE = new CrashHandler();

    private OnHandleEvent mOnHandleEvent;

    private CrashHandler() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     */
    public void init() {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void init(OnHandleEvent onHandleEvent){
        setOnHandleEvent(onHandleEvent);
        init();
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        if(ex != null){
            if(mOnHandleEvent != null){
                mOnHandleEvent.handleException(ex);
            }
//            handleException(ex);

            if(mDefaultHandler != null){
                mDefaultHandler.uncaughtException(thread, ex);
            }
        }
    }

    public void setOnHandleEvent(OnHandleEvent onHandleEvent){
        this.mOnHandleEvent = onHandleEvent;
    }

    public interface OnHandleEvent{
        /**
         * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
         * */
        void handleException(Throwable ex);
    }

}
