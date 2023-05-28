package com.app.base

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.app.base.databinding.ActivityBaseBinding
import com.app.data.remote.NetWorkState
import com.app.data.utils.ConstantsApi
import com.app.data.utils.CustomErrorThrow
import com.app.data.utils.ErrorAPI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
abstract class BaseActivity(private val layoutResource: Int) : AppCompatActivity() {
    private var viewBase: ActivityBaseBinding? = null
    var savedInstanceState: Bundle? = null

    private val baseViewModel: BaseViewModel by viewModels()

    open fun setActions() {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        viewBase = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(viewBase?.root)
        val activityView = LayoutInflater.from(this)
            .inflate(layoutResource, viewBase?.flContent, false) as ViewGroup
        viewBase?.flContent?.addView(activityView)

        //setContentView(layoutResource)
        this.savedInstanceState = savedInstanceState
        setActions()
        observeUnAuthorized()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBase = null
    }


    private fun inflateLayout(progressRes: Int) {
        //viewBase?.flProgress?.show()
        //viewBase?.flContent?.removeAllViews()
        val progressViewRes = progressRes
        val progress = LayoutInflater.from(this)
            .inflate(progressViewRes, viewBase?.flContent, false) as ViewGroup
        viewBase?.flContent?.addView(progress)
    }

    private fun inflateMain(layoutResource: Int) {
        //viewBase?.flProgress?.show()
        viewBase?.flContent?.removeAllViewsInLayout()

        val progress = LayoutInflater.from(this)
            .inflate(layoutResource, viewBase?.flContent, false) as ViewGroup
        viewBase?.flContent?.addView(progress)
    }


    private fun observeUnAuthorized() {
        GlobalScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                baseViewModel.unAuthorizedFlow
                    .collect {

                    }
            }
        }


    }


    fun handleErrorGeneral(th: Throwable, func: (() -> Unit)? = null): CustomErrorThrow? {
        th.printStackTrace()

        when (th.message) {
            ErrorAPI.BAD_REQUEST -> {
                Toast.makeText(this, "bad request", Toast.LENGTH_SHORT).show()
            }
            ErrorAPI.UNAUTHRIZED -> {
                Toast.makeText(this, "unauth", Toast.LENGTH_SHORT).show()
            }
            ErrorAPI.CONNECTION_ERROR -> {
                Toast.makeText(this, "error connection", Toast.LENGTH_SHORT).show()
            }
            else -> {
                if (th.cause is CustomErrorThrow) {
                    val cause = th.cause as CustomErrorThrow
                    Toast.makeText(this, cause.value, Toast.LENGTH_SHORT).show()
                    return cause

                } else {
                    Toast.makeText(this, "${th.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return null
    }


    fun showProgressFullScreen() {
        inflateLayout(R.layout.progress_dialog)

    }

    fun hideProgressFullScreen(res: Int) {

        inflateMain(res)

    }


    fun handleSharedFlow(
        userFlow: SharedFlow<NetWorkState>,
        onShowProgress: (() -> Unit)? = null,
        onHideProgress: (() -> Unit)? = null,
        onSuccess: (data: Any) -> Unit,
        onError: ((th: Throwable) -> Unit)? = null
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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