package com.warchaser.libbase.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.viewbinding.ViewBinding
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.components.support.RxFragment
import com.warchaser.libbase.ui.presenter.IBasePresenter
import com.warchaser.libbase.ui.presenter.IBaseView
import com.warchaser.libcommonutils.PackageUtil
import me.yokeyword.fragmentation.*
import me.yokeyword.fragmentation.anim.FragmentAnimator

abstract class BaseFragment <P : IBasePresenter<V>, V : IBaseView, VB : ViewBinding>(private val mInflate: (LayoutInflater, ViewGroup?, Boolean) -> VB)
    : RxFragment(), ISupportFragment, IBaseView, LifecycleProvider<FragmentEvent>{

    protected lateinit var TAG : String
    private var mDelegate : SupportFragmentDelegate = SupportFragmentDelegate(this)
    protected lateinit var _mActivity : ISupportActivity
    protected var mPresenter : P? = null
    protected var mRootView : View? = null

    private var mViewBound: VB? = null
    private val binding: VB get() = mViewBound!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDelegate.onCreate(savedInstanceState)
        mViewBound = mInflate(inflater, container, false)
        TAG = PackageUtil.getSimpleClassName(this)
        mPresenter = onLoadPresenter()?.apply {
            attachView(this@BaseFragment as V)
        }
        mRootView = mViewBound?.root
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitView(view, savedInstanceState)
    }

    protected abstract fun onInitView(view: View, savedInstanceState: Bundle?)

    protected abstract fun onLoadPresenter(): P?

    protected fun getViewBound() = binding

    override fun onDestroy() {
        mDelegate.onDestroy()
        super.onDestroy()
    }

    override fun onDestroyView() {
        mDelegate.onDestroyView()
        super.onDestroyView()
        mViewBound = null
        mPresenter?.detachView()
        mPresenter = null
    }

    override fun getSupportDelegate() : SupportFragmentDelegate = mDelegate

    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    override fun extraTransaction(): ExtraTransaction = mDelegate.extraTransaction()

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mDelegate.onAttach(activity)
        _mActivity = mDelegate.activity as ISupportActivity
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return mDelegate.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mDelegate.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        mDelegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        mDelegate.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mDelegate.onHiddenChanged(hidden)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mDelegate.setUserVisibleHint(isVisibleToUser)
    }

    /**
     * Causes the Runnable r to be added to the action queue.
     * <p>
     * The runnable will be run after all the previous action has been run.
     * <p>
     * 前面的事务全部执行后 执行该Action
     *
     * @deprecated Use {@link #post(Runnable)} instead.
     */
    override fun enqueueAction(runnable: Runnable?) {
        mDelegate.enqueueAction(runnable)
    }

    /**
     * Causes the Runnable r to be added to the action queue.
     * <p>
     * The runnable will be run after all the previous action has been run.
     * <p>
     * 前面的事务全部执行后 执行该Action
     */
    override fun post(runnable: Runnable?) {
        mDelegate.post(runnable)
    }

    /**
     * Called when the enter-animation end.
     * 入栈动画 结束时,回调
     */
    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        mDelegate.onEnterAnimationEnd(savedInstanceState)
    }

    /**
     * Lazy initial，Called when fragment is first called.
     * <p>
     * 同级下的 懒加载 ＋ ViewPager下的懒加载  的结合回调方法
     */
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        mDelegate.onLazyInitView(savedInstanceState)
    }

    /**
     * Called when the fragment is visible.
     * 当Fragment对用户可见时回调
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    override fun onSupportVisible() {
        mDelegate.onSupportVisible()
    }

    /**
     * Called when the fragment is invivible.
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    override fun onSupportInvisible() {
        mDelegate.onSupportInvisible()
    }

    /**
     * Return true if the fragment has been supportVisible.
     */
    override fun isSupportVisible(): Boolean = mDelegate.isSupportVisible

    /**
     * Set fragment animation with a higher priority than the ISupportActivity
     * 设定当前Fragmemt动画,优先级比在SupportActivity里高
     */
    override fun onCreateFragmentAnimator(): FragmentAnimator = mDelegate.onCreateFragmentAnimator()

    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    override fun getFragmentAnimator(): FragmentAnimator? {
        return mDelegate.fragmentAnimator
    }

    /**
     * 设置Fragment内的全局动画
     */
    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator?) {
        mDelegate.fragmentAnimator = fragmentAnimator
    }

    /**
     * 按返回键触发,前提是SupportActivity的onBackPressed()方法能被调用
     *
     * @return false则继续向上传递, true则消费掉该事件
     */
    override fun onBackPressedSupport(): Boolean = mDelegate.onBackPressedSupport()

    /**
     * 类似 {@link Activity#setResult(int, Intent)}
     * <p>
     * Similar to {@link Activity#setResult(int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    override fun setFragmentResult(resultCode: Int, bundle: Bundle?) {
        mDelegate.setFragmentResult(resultCode, bundle)
    }

    /**
     * 类似  {@link Activity#onActivityResult(int, int, Intent)}
     * <p>
     * Similar to {@link Activity#onActivityResult(int, int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        mDelegate.onFragmentResult(requestCode, resultCode, data)
    }

    /**
     * 在start(TargetFragment,LaunchMode)时,启动模式为SingleTask/SingleTop, 回调TargetFragment的该方法
     * 类似 {@link Activity#onNewIntent(Intent)}
     * <p>
     * Similar to {@link Activity#onNewIntent(Intent)}
     *
     * @param args putNewBundle(Bundle newBundle)
     * @see #start(ISupportFragment, int)
     */
    override fun onNewBundle(args: Bundle?) {
        mDelegate.onNewBundle(args)
    }

    /**
     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
     *
     * @see #start(ISupportFragment, int)
     */
    override fun putNewBundle(newBundle: Bundle?) {
        mDelegate.putNewBundle(newBundle)
    }

    /****************************************以下为可选方法(Optional methods)******************************************************/

    /****************************************以下为可选方法(Optional methods) */
    /**
     * 隐藏软键盘
     */
    protected open fun hideSoftInput() {
        mDelegate.hideSoftInput()
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    protected open fun showSoftInput(view: View?) {
        mDelegate.showSoftInput(view)
    }

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    open fun loadRootFragment(containerId: Int, toFragment: ISupportFragment?) {
        mDelegate.loadRootFragment(containerId, toFragment)
    }

    open fun loadRootFragment(
        containerId: Int,
        toFragment: ISupportFragment?,
        addToBackStack: Boolean,
        allowAnim: Boolean
    ) {
        mDelegate.loadRootFragment(containerId, toFragment, addToBackStack, allowAnim)
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

    /**
     * show一个Fragment,hide其他同栈所有Fragment
     * 使用该方法时，要确保同级栈内无多余的Fragment,(只有通过loadMultipleRootFragment()载入的Fragment)
     *
     *
     * 建议使用更明确的[.showHideFragment]
     *
     * @param showFragment 需要show的Fragment
     */
    open fun showHideFragment(showFragment: ISupportFragment?) {
        mDelegate.showHideFragment(showFragment)
    }

    /**
     * show一个Fragment,hide一个Fragment ; 主要用于类似微信主页那种 切换tab的情况
     */
    open fun showHideFragment(showFragment: ISupportFragment?, hideFragment: ISupportFragment?) {
        mDelegate.showHideFragment(showFragment, hideFragment)
    }

    open fun start(toFragment: ISupportFragment?) {
        mDelegate.start(toFragment)
    }

    /**
     * @param launchMode Similar to Activity's LaunchMode.
     */
    open fun start(toFragment: ISupportFragment?, @ISupportFragment.LaunchMode launchMode: Int) {
        mDelegate.start(toFragment, launchMode)
    }

    /**
     * Launch an fragment for which you would like a result when it poped.
     */
    open fun startForResult(toFragment: ISupportFragment?, requestCode: Int) {
        mDelegate.startForResult(toFragment, requestCode)
    }

    /**
     * Start the target Fragment and pop itself
     */
    open fun startWithPop(toFragment: ISupportFragment?) {
        mDelegate.startWithPop(toFragment)
    }

    /**
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


    open fun replaceFragment(toFragment: ISupportFragment?, addToBackStack: Boolean) {
        mDelegate.replaceFragment(toFragment, addToBackStack)
    }

    open fun pop() {
        mDelegate.pop()
    }

    /**
     * Pop the child fragment.
     */
    open fun popChild() {
        mDelegate.popChild()
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     *
     *
     * 出栈到目标fragment
     *
     * @param targetFragmentClass   目标fragment
     * @param includeTargetFragment 是否包含该fragment
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

    open fun popToChild(targetFragmentClass: Class<*>?, includeTargetFragment: Boolean) {
        mDelegate.popToChild(targetFragmentClass, includeTargetFragment)
    }

    open fun popToChild(
        targetFragmentClass: Class<*>?,
        includeTargetFragment: Boolean,
        afterPopTransactionRunnable: Runnable?
    ) {
        mDelegate.popToChild(
            targetFragmentClass,
            includeTargetFragment,
            afterPopTransactionRunnable
        )
    }

    open fun popToChild(
        targetFragmentClass: Class<*>?,
        includeTargetFragment: Boolean,
        afterPopTransactionRunnable: Runnable?,
        popAnim: Int
    ) {
        mDelegate.popToChild(
            targetFragmentClass,
            includeTargetFragment,
            afterPopTransactionRunnable,
            popAnim
        )
    }

    /**
     * 得到位于栈顶Fragment
     */
    open fun getTopFragment(): ISupportFragment? = SupportHelper.getTopFragment(fragmentManager)

    open fun getTopChildFragment(): ISupportFragment? = SupportHelper.getTopFragment(
        childFragmentManager
    )

    /**
     * @return 位于当前Fragment的前一个Fragment
     */
    open fun getPreFragment(): ISupportFragment? = SupportHelper.getPreFragment(this)

    /**
     * 获取栈内的fragment对象
     */
    open fun <T : ISupportFragment?> findFragment(fragmentClass: Class<T>?): T? = SupportHelper.findFragment(
        fragmentManager,
        fragmentClass
    )

    /**
     * 获取栈内的fragment对象
     */
    open fun <T : ISupportFragment?> findChildFragment(fragmentClass: Class<T>?): T = SupportHelper.findFragment(
        childFragmentManager,
        fragmentClass
    )

    open fun startDontHideSelf(fragment: ISupportFragment?) {
        extraTransaction()
            .setCustomAnimations(
                R.anim.anim_current_list_enter,
                0,
                0,
                R.anim.anim_current_list_exit
            )
            .startDontHideSelf(fragment, ISupportFragment.SINGLETASK)
    }

}