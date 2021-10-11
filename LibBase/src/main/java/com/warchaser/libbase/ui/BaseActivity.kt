package com.warchaser.libbase.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.trello.rxlifecycle3.components.support.RxFragmentActivity
import com.warchaser.libbase.ui.presenter.IBasePresenter
import com.warchaser.libbase.ui.presenter.IBaseView
import com.warchaser.libcommonutils.AppManager
import com.warchaser.libcommonutils.PackageUtil
import me.yokeyword.fragmentation.*
import me.yokeyword.fragmentation.anim.FragmentAnimator

abstract class BaseActivity<P : IBasePresenter<V>, V : IBaseView, VB : ViewBinding>(private val inflate : (LayoutInflater) -> VB) : RxFragmentActivity(), ISupportActivity, IBaseView{

    protected lateinit var TAG : String
    protected var mPresenter : P? = null

    private lateinit var mDelegate : SupportActivityDelegate

    private lateinit var mViewBond : VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = PackageUtil.getSimpleClassName(this)
        mDelegate = SupportActivityDelegate(this)
        mDelegate.onCreate(savedInstanceState)
        AppManager.getInstance().addActivity(this)

        mPresenter = onLoadPresenter()?.apply {
            attachView(this@BaseActivity as V)
        }

        beforeSetContentView(savedInstanceState)

        mViewBond = inflate(layoutInflater)
        setContentView(mViewBond.root)

        afterSetContentView(savedInstanceState)
    }

    protected fun getViewBound() : VB = mViewBond

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDelegate.onPostCreate(savedInstanceState)
    }

    override fun onDestroy() {
        mDelegate.onDestroy()
        super.onDestroy()
    }

    override fun finish() {
        super.finish()
        finishAction()
    }

    protected open fun finishAction(){
        AppManager.getInstance().removeActivity(this)

        mPresenter?.run {
            detachView()
        }
        mPresenter = null
    }

    /**
     * setContent之前操作
     */
    protected open fun beforeSetContentView(savedInstanceState: Bundle?){}

    /**
     * setContent之后操作
     */
    protected open fun afterSetContentView(savedInstanceState: Bundle?) {}

    protected open fun getContentLayoutId(): Int = 0

    protected abstract fun onLoadPresenter(): P?

    fun startCertainActivity(activityClass: Class<out Activity?>?) {
        startActivity(Intent(this, activityClass))
    }

    override fun getSupportDelegate(): SupportActivityDelegate? = mDelegate

    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    override fun extraTransaction(): ExtraTransaction? = mDelegate.extraTransaction()

    /**
     * Note： return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean = mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev)

    /**
     * 不建议复写该方法,请使用 [.onBackPressedSupport] 代替
     */
    override fun onBackPressed() {
        mDelegate.onBackPressed()
    }

    /**
     * 该方法回调时机为,Activity回退栈内Fragment的数量 小于等于1 时,默认finish Activity
     * 请尽量复写该方法,避免复写onBackPress(),以保证SupportFragment内的onBackPressedSupport()回退事件正常执行
     */
    override fun onBackPressedSupport() {
        mDelegate.onBackPressedSupport()
    }

    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    override fun getFragmentAnimator(): FragmentAnimator? = mDelegate.fragmentAnimator

    /**
     * Set all fragments animation.
     * 设置Fragment内的全局动画
     */
    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator?) {
        mDelegate.fragmentAnimator = fragmentAnimator
    }

    /**
     * Set all fragments animation.
     * 构建Fragment转场动画
     *
     *
     * 如果是在Activity内实现,则构建的是Activity内所有Fragment的转场动画,
     * 如果是在Fragment内实现,则构建的是该Fragment的转场动画,此时优先级 > Activity的onCreateFragmentAnimator()
     *
     * @return FragmentAnimator对象
     */
    override fun onCreateFragmentAnimator(): FragmentAnimator? = mDelegate.onCreateFragmentAnimator()

    /**
     * Causes the Runnable r to be added to the action queue.
     *
     *
     * The runnable will be run after all the previous action has been run.
     *
     *
     * 前面的事务全部执行后 执行该Action
     */
    override fun post(runnable: Runnable?) {
        mDelegate.post(runnable)
    }

    /****************************************以下为可选方法(Optional methods) */ // 选择性拓展其他方法
    open fun loadRootFragment(containerId: Int, toFragment: ISupportFragment) {
        mDelegate.loadRootFragment(containerId, toFragment)
    }

    /**
     * 加载多个同级根Fragment,类似Wechat, QQ主页的场景
     */
    open fun loadMultipleRootFragment(
        containerId: Int,
        showPosition: Int,
        vararg toFragments: ISupportFragment?
    ) {
        mDelegate.loadMultipleRootFragment(containerId, showPosition, *toFragments)
    }

    open fun start(toFragment: ISupportFragment?) {
        mDelegate.start(toFragment)
    }

    /**
     * @param launchMode Same as Activity's LaunchMode.
     */
    open fun start(toFragment: ISupportFragment?, @ISupportFragment.LaunchMode launchMode: Int) {
        mDelegate.start(toFragment, launchMode)
    }

    /**
     * It is recommended to use [SupportFragment.startWithPopTo].
     *
     * @see .popTo
     * @see .start
     */
    open fun startWithPopTo(
        toFragment: ISupportFragment?,
        targetFragmentClass: Class<*>?,
        includeTargetFragment: Boolean
    ) {
        mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment)
    }

    /**
     * Pop the fragment.
     */
    open fun pop() {
        mDelegate.pop()
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     */
    open fun popTo(targetFragmentClass: Class<*>?, includeTargetFragment: Boolean) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment)
    }

    /**
     * If you want to begin another FragmentTransaction immediately after popTo(), use this method.
     * 如果你想在出栈后, 立刻进行FragmentTransaction操作，请使用该方法
     */
    open fun popTo(
        targetFragmentClass: Class<*>?,
        includeTargetFragment: Boolean,
        afterPopTransactionRunnable: Runnable?
    ) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable)
    }

    open fun popTo(
        targetFragmentClass: Class<*>?,
        includeTargetFragment: Boolean,
        afterPopTransactionRunnable: Runnable?,
        popAnim: Int
    ) {
        mDelegate.popTo(
            targetFragmentClass,
            includeTargetFragment,
            afterPopTransactionRunnable,
            popAnim
        )
    }

    /**
     * 得到位于栈顶Fragment
     */
    open fun getTopFragment(): ISupportFragment? = SupportHelper.getTopFragment(supportFragmentManager)

    /**
     * 获取栈内的fragment对象
     */
    open fun <T : ISupportFragment?> findFragment(fragmentClass: Class<T>?): T? = SupportHelper.findFragment(supportFragmentManager, fragmentClass)

    /**
     * show一个Fragment,hide一个Fragment ; 主要用于类似微信主页那种 切换tab的情况
     */
    open fun showHideFragment(showFragment: ISupportFragment?, hideFragment: ISupportFragment?) {
        mDelegate.showHideFragment(showFragment, hideFragment)
    }

    open fun showHideFragment(showFragment: ISupportFragment?) {
        mDelegate.showHideFragment(showFragment)
    }

}
