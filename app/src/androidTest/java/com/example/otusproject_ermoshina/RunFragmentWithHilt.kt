package com.example.otusproject_ermoshina

import android.content.ComponentName
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider

    inline fun <reified T : Fragment> launchFragment(
        noinline creator: (() -> T)? = null): AutoCloseable {
        val intent = Intent.makeMainActivity(
            ComponentName(
                ApplicationProvider.getApplicationContext(),
                TestMainActivity::class.java
            )
        )
        return ActivityScenario.launch<TestMainActivity>(intent).onActivity {
            val fragment = creator?.invoke() ?: it.supportFragmentManager.fragmentFactory.instantiate(
                T::class.java.classLoader!!,
                T::class.java.name
            )
            it.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .commitNow()
        }
    }

