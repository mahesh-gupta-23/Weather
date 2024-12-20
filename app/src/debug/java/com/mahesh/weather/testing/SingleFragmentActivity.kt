package com.mahesh.weather.testing

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.support.HasSupportFragmentInjector

class SingleFragmentActivity : AppCompatActivity(), HasSupportFragmentInjector {
    private lateinit var injector: AndroidInjector<Fragment>

    override fun supportFragmentInjector() = injector

    fun startFragment(fragment: Fragment, injector: AndroidInjector<Fragment>) {
        this.injector = injector
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment, "TAG")
            .commitNow()
    }

    inline fun <reified T : Fragment> startFragment(fragment: T, crossinline injector: (T) -> Unit) {
        startFragment(fragment, AndroidInjector { if (it is T) injector(it) })
    }
}