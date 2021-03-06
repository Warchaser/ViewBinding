package com.warchaser.libbase.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragmentActivity
import com.warchaser.libcommonutils.AppManager
import com.warchaser.libcommonutils.PackageUtil
import me.yokeyword.fragmentation.*
import me.yokeyword.fragmentation.anim.FragmentAnimator

abstract class BaseVMActivity<VB: ViewBinding>(private val inflate : (LayoutInflater) -> VB)  : RxFragmentActivity(), ISupportActivity {

    protected lateinit var TAG : String

    private lateinit var mDelegate : SupportActivityDelegate

    private lateinit var mViewBond : VB

    private val mActivityProvider : ViewModelProvider by lazy{
        ViewModelProvider(this)
    }

//    val mApplicationProvider : ViewModelProvider by lazy{
//        ViewModelProvider(this.applicationContext as App)
//    }

    open fun getApplicationFactory(activity : Activity) : ViewModelProvider.Factory{
        activity.let {
            it.application.let { application ->
                return ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            }
        }
    }

    protected open fun <T : ViewModel> getActivityScopeViewModel(modelClass : Class<T>) : T = mActivityProvider.get(modelClass)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = PackageUtil.getSimpleClassName(this)
        mDelegate = SupportActivityDelegate(this)
        mDelegate.onCreate(savedInstanceState)
        AppManager.getInstance().addActivity(this)

        beforeSetContentView(savedInstanceState)
        mViewBond = inflate(layoutInflater)
        setContentView(mViewBond.root)
        afterSetContentView(savedInstanceState)
        setVariable()
        startObserve()
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

    protected open fun finishAction() {
        AppManager.getInstance().removeActivity(this)
    }

    /**
     * setContent????????????
     */
    protected open fun beforeSetContentView(savedInstanceState: Bundle?){}

    /**
     * setContent????????????
     */
    protected open fun afterSetContentView(savedInstanceState: Bundle?) {}

    /**
     * ??????????????????
     * */
    protected abstract fun startObserve()

    protected abstract fun setVariable()

    override fun getSupportDelegate(): SupportActivityDelegate? = mDelegate

    /**
     * Perform some extra transactions.
     * ???????????????????????????Tag?????????SharedElement???????????????????????????Fragment
     */
    override fun extraTransaction(): ExtraTransaction? = mDelegate.extraTransaction()

    /**
     * Note??? return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean = mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev)

    /**
     * ????????????????????????,????????? [.onBackPressedSupport] ??????
     */
    override fun onBackPressed() {
        mDelegate.onBackPressed()
    }

    /**
     * ????????????????????????,Activity????????????Fragment????????? ????????????1 ???,??????finish Activity
     * ????????????????????????,????????????onBackPress(),?????????SupportFragment??????onBackPressedSupport()????????????????????????
     */
    override fun onBackPressedSupport() {
        mDelegate.onBackPressedSupport()
    }

    /**
     * ??????????????????????????? copy
     *
     * @return FragmentAnimator
     */
    override fun getFragmentAnimator(): FragmentAnimator? = mDelegate.fragmentAnimator

    /**
     * Set all fragments animation.
     * ??????Fragment??????????????????
     */
    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator?) {
        mDelegate.fragmentAnimator = fragmentAnimator
    }

    /**
     * Set all fragments animation.
     * ??????Fragment????????????
     *
     *
     * ????????????Activity?????????,???????????????Activity?????????Fragment???????????????,
     * ????????????Fragment?????????,??????????????????Fragment???????????????,??????????????? > Activity???onCreateFragmentAnimator()
     *
     * @return FragmentAnimator??????
     */
    override fun onCreateFragmentAnimator(): FragmentAnimator? = mDelegate.onCreateFragmentAnimator()

    /**
     * Causes the Runnable r to be added to the action queue.
     *
     *
     * The runnable will be run after all the previous action has been run.
     *
     *
     * ?????????????????????????????? ?????????Action
     */
    override fun post(runnable: Runnable?) {
        mDelegate.post(runnable)
    }

    /****************************************?????????????????????(Optional methods) */ // ???????????????????????????
    open fun loadRootFragment(containerId: Int, toFragment: ISupportFragment) {
        mDelegate.loadRootFragment(containerId, toFragment)
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

    /**
     * ??????????????????Fragment
     */
    open fun getTopFragment(): ISupportFragment? = SupportHelper.getTopFragment(supportFragmentManager)

    /**
     * ???????????????fragment??????
     */
    open fun <T : ISupportFragment?> findFragment(fragmentClass: Class<T>?): T? = SupportHelper.findFragment(supportFragmentManager, fragmentClass)

    /**
     * show??????Fragment,hide??????Fragment ; ???????????????????????????????????? ??????tab?????????
     */
    open fun showHideFragment(showFragment: ISupportFragment?, hideFragment: ISupportFragment?) {
        mDelegate.showHideFragment(showFragment, hideFragment)
    }

    open fun showHideFragment(showFragment: ISupportFragment?) {
        mDelegate.showHideFragment(showFragment)
    }
}