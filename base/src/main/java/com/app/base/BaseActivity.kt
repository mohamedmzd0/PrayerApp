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
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBase = null
    }


    fun handleErrorGeneral(th: Throwable, func: (() -> Unit)? = null): CustomErrorThrow? {
        th.printStackTrace()

        when (th.message) {
            ErrorAPI.BAD_REQUEST -> {
                Toast.makeText(this, "bad request", Toast.LENGTH_SHORT).show()
            }
            ErrorAPI.UNAUTHRIZED -> {
                // no need
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


}