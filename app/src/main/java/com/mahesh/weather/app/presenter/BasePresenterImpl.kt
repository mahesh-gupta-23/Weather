package com.mahesh.weather.app.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.mahesh.weather.app.coroutines.CoroutinesManager
import java.util.concurrent.atomic.AtomicBoolean

abstract class BasePresenterImpl<View : BaseView>
constructor(coroutinesManager: CoroutinesManager) : ViewModel(), CoroutinesManager by coroutinesManager,
    BasePresenter<View> {

    private var viewInstance: View? = null
    private var viewLifecycle: Lifecycle? = null
    private val isViewResumed = AtomicBoolean(false)

    protected fun view(): View? {
        System.out.println("resume ${isViewResumed.get()}")
        if (isViewResumed.get()) {
            viewInstance?.let { return it }
        }
        return null
    }

    @Synchronized
    override fun attachView(view: View, viewLifecycle: Lifecycle) {
        viewInstance = view
        this.viewLifecycle = viewLifecycle
        viewLifecycle.addObserver(this)
    }

    @Synchronized
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    private fun onViewStateChanged() {
        isViewResumed.set(viewLifecycle?.currentState?.isAtLeast(Lifecycle.State.RESUMED) ?: false)
    }

    @Synchronized
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onViewCreate() {
        onCreate()
    }

    @Synchronized
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onViewResumed() {
        onResume()
    }

    @Synchronized
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onViewDestroyed() {
        viewInstance = null
        viewLifecycle = null
        cancelAllCoroutines()
    }

    abstract fun onCreate()

    abstract fun onResume()


}