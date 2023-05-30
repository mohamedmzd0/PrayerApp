package com.mohamed.prayerapp.ui.qibla

import android.content.Context.SENSOR_SERVICE
import android.hardware.*
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.app.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mohamed.prayerapp.R
import com.mohamed.prayerapp.databinding.FragmentQiblaBinding
import com.mohamed.prayerapp.utils.LocationHelper
import kotlin.math.roundToInt


class QiblaFragment : BaseFragment(R.layout.fragment_qibla), SensorEventListener {

    private var userLoc: Location? = null
    private val locationHelper by lazy {
        LocationHelper(requireActivity(), {
            it?.let { it1 ->
                setCurrentLocation(it1)
                userLoc = Location("service Provider").apply {
                    latitude = it1.first; longitude = it1.second
                }
            }
        })
    }

    private fun setCurrentLocation(it: Pair<Double, Double>) {
        googleMap.addMarker(
            MarkerOptions().position(LatLng(it.first, it.second)).title("Your Location")
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.first, it.second), 15f))
    }

    private lateinit var googleMap: GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap

        locationHelper.getCurrentLocation()


    }


    private var _binding: FragmentQiblaBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQiblaBinding.bind(view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        setupSensor()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mSensorManager?.unregisterListener(this);

    }


    private fun setupSensor() {
        mSensorManager = requireContext().getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        mSensorManager?.registerListener(
            this,
            sensor,
            SensorManager.SENSOR_DELAY_GAME
        ); //SensorManager.SENSOR_DELAY_Fastest
    }

    // record the compass picture angle turned
    private var currentDegree = 0f
    private var currentDegreeNeedle = 0f

    // device sensor manager
    private var mSensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        if (sensorEvent == null) return
        if (userLoc == null) return
        val degree = sensorEvent.values[0].roundToInt().toFloat()
        var head = sensorEvent.values[0].roundToInt().toFloat()
        val destinationLoc = Location("service Provider")

        destinationLoc.latitude = 21.422487 //kaaba latitude setting

        destinationLoc.longitude = 39.826206 //kaaba longitude setting

        var bearTo: Float = userLoc!!.bearingTo(destinationLoc)

        //head = The angle that you've rotated your phone from true north. (jaise image lagi hai wo true north per hai ab phone jitne rotate yani jitna image ka n change hai us ka angle hai ye)
        val geoField = GeomagneticField(
            java.lang.Double.valueOf(userLoc!!.latitude).toFloat(), java.lang.Double
                .valueOf(userLoc!!.longitude).toFloat(),
            java.lang.Double.valueOf(userLoc!!.altitude).toFloat(),
            System.currentTimeMillis()
        )
        head -= geoField.declination // converts magnetic north into true north


        if (bearTo < 0) {
            bearTo += 360
            //bearTo = -100 + 360  = 260;
        }

//This is where we choose to point it

//This is where we choose to point it
        var direction = bearTo - head

// If the direction is smaller than 0, add 360 to get the rotation clockwise.

// If the direction is smaller than 0, add 360 to get the rotation clockwise.
        if (direction < 0) {
            direction += 360
        }

        val raQibla = RotateAnimation(
            currentDegreeNeedle,
            direction,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        raQibla.duration = 210
        raQibla.fillAfter = true

        binding.ivArrow.startAnimation(raQibla)

        currentDegreeNeedle = direction

// create a rotation animation (reverse turn degree degrees)

// create a rotation animation (reverse turn degree degrees)
        val ra = RotateAnimation(
            currentDegree,
            -degree,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )

// how long the animation will take place

// how long the animation will take place
        ra.duration = 210


// set the animation after the end of the reservation status


// set the animation after the end of the reservation status
        ra.fillAfter = true

// Start the animation

// Start the animation
        binding.ivArrow.startAnimation(ra)

        currentDegree = -degree

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}