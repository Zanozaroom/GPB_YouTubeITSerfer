package com.example.otusproject_ermoshina.ui.base

import android.os.Parcelable
import androidx.fragment.app.Fragment


fun Fragment.navigator() : ContractNavigator{
    return requireActivity() as ContractNavigator
}

interface ContractNavigator {
    fun startFragmentMainStack(fragment: Fragment)
    fun startFragmentUserStack(fragment: Fragment)
    fun setActionBarNavigateBack()
    fun removeActionBarNavigateBack()

}