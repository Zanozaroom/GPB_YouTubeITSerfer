package com.example.otusproject_ermoshina.di
import androidx.fragment.app.FragmentActivity
import com.example.otusproject_ermoshina.ui.base.ContractNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object NavigatorModule {

    @Provides
    fun provideNavigator(activity: FragmentActivity): ContractNavigator {
        return activity as ContractNavigator
    }

}