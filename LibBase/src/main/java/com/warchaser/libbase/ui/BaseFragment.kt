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
        super.onCreateView(inflater, container, savedInstanceState)
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
     * ???????????????????????????Tag?????????SharedElement???????????????????????????Fragment
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
     * ?????????????????????????????? ?????????Action
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
     * ?????????????????????????????? ?????????Action
     */
    override fun post(runnable: Runnable?) {
        mDelegate.post(runnable)
    }

    /**
     * Called when the enter-animation end.
     * ???????????? ?????????,??????
     */
    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        mDelegate.onEnterAnimationEnd(savedInstanceState)
    }

    /**
     * Lazy initial???Called when fragment is first called.
     * <p>
     * ???????????? ????????? ??? ViewPager???????????????  ?????????????????????
     */
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        mDelegate.onLazyInitView(savedInstanceState)
    }

    /**
     * Called when the fragment is visible.
     * ???Fragment????????????????????????
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
     * ????????????Fragmemt??????,???????????????SupportActivity??????
     */
    override fun onCreateFragmentAnimator(): FragmentAnimator = mDelegate.onCreateFragmentAnimator()

    /**
     * ??????????????????????????? copy
     *
     * @return FragmentAnimator
     */
    override fun getFragmentAnimator(): FragmentAnimator? {
        return mDelegate.fragmentAnimator
    }

    /**
     * ??????Fragment??????????????????
     */
    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator?) {
        mDelegate.fragmentAnimator = fragmentAnimator
    }

    /**
     * ??????????????????,?????????SupportActivity???onBackPressed()??????????????????
     *
     * @return false?????????????????????, true?????????????????????
     */
    override fun onBackPressedSupport(): Boolean = mDelegate.onBackPressedSupport()

    /**
     * ?????? {@link Activity#setResult(int, Intent)}
     * <p>
     * Similar to {@link Activity#setResult(int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    override fun setFragmentResult(resultCode: Int, bundle: Bundle?) {
        mDelegate.setFragmentResult(resultCode, bundle)
    }

    /**
     * ??????  {@link Activity#onActivityResult(int, int, Intent)}
     * <p>
     * Similar to {@link Activity#onActivityResult(int, int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        mDelegate.onFragmentResult(requestCode, resultCode, data)
    }

    /**
     * ???start(TargetFragment,LaunchMode)???,???????????????SingleTask/SingleTop, ??????TargetFragment????????????
     * ?????? {@link Activity#onNewIntent(Intent)}
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
     * ??????NewBundle,?????????????????????SingleTask/SingleTop???
     *
     * @see #start(ISupportFragment, int)
     */
    override fun putNewBundle(newBundle: Bundle?) {
        mDelegate.putNewBundle(newBundle)
    }

    /****************************************?????????????????????(Optional methods)******************************************************/

    /****************************************?????????????????????(Optional methods) */
    /**
     * ???????????????
     */
    protected open fun hideSoftInput() {
        mDelegate.hideSoftInput()
    }

    /**
     * ???????????????,??????????????????,??????onPause????????????????????????
     */
    protected open fun showSoftInput(view: View?) {
        mDelegate.showSoftInput(view)
    }

    /**
     * ?????????Fragment, ???Activity???????????????Fragment ??? Fragment??????????????????Fragment
     *
     * @param containerId ??????id
     * @param toFragment  ??????Fragment
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
     * ?????????????????????Fragment,??????Wechat, QQ???????????????
     */
    open fun loadMultipleRootFragment(
        containerId: Int,
        showPosition: Int,
        vararg toFragments: ISupportFragment?
    ) {
        mDelegate.loadMultipleRootFragment(containerId, showPosition, *toFragments)
    }

    /**
     * show??????Fragment,hide??????????????????Fragment
     * ??????????????????????????????????????????????????????Fragment,(????????????loadMultipleRootFragment()?????????Fragment)
     *
     *
     * ????????????????????????[.showHideFragment]
     *
     * @param showFragment ??????show???Fragment
     */
    open fun showHideFragment(showFragment: ISupportFragment?) {
        mDelegate.showHideFragment(showFragment)
    }

    /**
     * show??????Fragment,hide??????Fragment ; ???????????????????????????????????? ??????tab?????????
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
     * ???????????????fragment
     *
     * @param targetFragmentClass   ??????fragment
     * @param includeTargetFragment ???????????????fragment
     */
    open fun popTo(targetFragmentClass: Class<*>?, includeTargetFragment: Boolean) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment)
    }

    /**
     * If you want to begin another FragmentTransaction immediately after popTo(), use this method.
     * ????????????????????????, ????????????FragmentTransaction???????????????????????????
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
     * ??????????????????Fragment
     */
    open fun getTopFragment(): ISupportFragment? = SupportHelper.getTopFragment(fragmentManager)

    open fun getTopChildFragment(): ISupportFragment? = SupportHelper.getTopFragment(
        childFragmentManager
    )

    /**
     * @return ????????????Fragment????????????Fragment
     */
    open fun getPreFragment(): ISupportFragment? = SupportHelper.getPreFragment(this)

    /**
     * ???????????????fragment??????
     */
    open fun <T : ISupportFragment?> findFragment(fragmentClass: Class<T>?): T? = SupportHelper.findFragment(
        fragmentManager,
        fragmentClass
    )

    /**
     * ???????????????fragment??????
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