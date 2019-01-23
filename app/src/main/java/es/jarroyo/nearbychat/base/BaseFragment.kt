package es.jarroyo.nearbychat.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife

abstract class  BaseFragment: Fragment(){

    abstract var layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun inflateView(inflater: LayoutInflater, container: ViewGroup?): View {
        val view = inflater.inflate(layoutId, container, false)
        ButterKnife.bind(this, view)
        return view
    }


}