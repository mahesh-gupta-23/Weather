package com.mahesh.weather.app.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import java.util.concurrent.atomic.AtomicBoolean

abstract class BasePresenterImpl<View : BaseView> : ViewModel(), BasePresenter<View> {

    private var viewInstance: View? = null
    private var viewLifecycle: Lifecycle? = null
    private val isViewResumed = AtomicBoolean(false)


    protected fun injectDependencies() {
        onInjectDependencies()
    }

    protected open fun onInjectDependencies() {
        // Nothing to do here. This is an event handled by the subclasses.
    }

    protected fun view(): View? {
        if (isViewResumed.get()) {
            viewInstance?.let { return it }
        }
        return null
    }

    @Synchronized
    override fun attachView(view: View, viewLifecycle: Lifecycle) {
        viewInstance = view
        this.viewLifecycle = viewLifecycle
    }

    @Synchronized
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    private fun onViewStateChanged() {
        isViewResumed.set(viewLifecycle?.currentState?.isAtLeast(Lifecycle.State.RESUMED) ?: false)
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
    }

    abstract fun onResume()


}