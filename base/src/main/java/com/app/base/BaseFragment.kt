package com.app.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.data.remote.NetWorkState
import com.app.data.utils.CustomErrorThrow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
abstract class BaseFragment(private val layout: Int) : Fragment() {

    private var fragmentView: ViewGroup? = null
    private var baseActivity: BaseActivity? = null
    private val baseViewModel: BaseViewModel by activityViewModels()
    var savedInstanceState: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(layout, container, false)
    }


    fun setTitleToolbar(tvTitle: TextView, title: String) {
        tvTitle.text = title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentView = null
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            val activity = context as BaseActivity?
            this.baseActivity = activity
        }
    }


    override fun onDetach() {
        super.onDetach()
        baseActivity = null
    }


    protected fun handleErrorGeneral(th: Throwable, func: (() -> Unit)? = null): CustomErrorThrow? {
        return baseActivity?.handleErrorGeneral(th, func)
    }


    fun Fragment.handleSharedFlow(
        userFlow: SharedFlow<NetWorkState>,
        lifeCycle: Lifecycle.State = Lifecycle.State.STARTED,
        onShowProgress: (() -> Unit)? = null,
        onHideProgress: (() -> Unit)? = null,
        onSuccess: (data: Any) -> Unit,
        onError: ((th: Throwable) -> Unit)? = null
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(lifeCycle) {
                userFlow.collect { networkState ->
                    when (networkState) {
                        is NetWorkState.Success<*> -> {
                            onSuccess(networkState.data!!)
                        }
                        is NetWorkState.Error -> {
                            if (onError == null) handleErrorGeneral(networkState.th) else onError(
                                networkState.th
                            )
                        }

                        else -> {
                        }
                    }

                }
            }
        }
    }

}
