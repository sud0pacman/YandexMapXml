package com.example.yandexmaptest

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.yandexmaptest.source.AddressData
import com.example.yandexmaptest.utils.MyAddress
import com.google.android.material.imageview.ShapeableImageView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {


    private val drivingRouteListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            routes = drivingRoutes
        }

        override fun onDrivingRoutesError(error: Error) {
            when (error) {
                is NetworkError -> showToast("Routes request error due network issues")
                else -> showToast("Routes request unknown error")
            }
        }
    }

    private var routePoints = emptyList<AddressData>()
        set(value) {
            field = value
            onRoutePointsUpdated()
        }

    private var routes = emptyList<DrivingRoute>()
        set(value) {
            field = value
            onRoutesUpdated()
        }

    private lateinit var drivingRouter: DrivingRouter
    private var drivingSession: DrivingSession? = null
    private lateinit var placemarksCollection: MapObjectCollection
    private lateinit var routesCollection: MapObjectCollection


    private lateinit var mapView: MapView
    private lateinit var map: Map


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapview)
        map = mapView.mapWindow.map
//        map.addInputListener(inputListener)


        placemarksCollection = map.mapObjects.addCollection()
        routesCollection = map.mapObjects.addCollection()

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.COMBINED)


        val map = mapView.mapWindow.map
        map.move(START_POSITION)

        if (MyAddress.from_address != null && MyAddress.to_address != null) {
            Log.d("TTT", "${MyAddress.from_address?.name} ${MyAddress.to_address?.name}")
            routePoints = listOf(
                MyAddress.from_address!!,
                MyAddress.to_address!!
            )
        }
    }


    private fun onRoutePointsUpdated() {
        placemarksCollection.clear()

        if (routePoints.isEmpty()) {
            drivingSession?.cancel()
            routes = emptyList()
            return
        }

        val imageProvider = ImageProvider.fromResource(this, R.drawable.ic_pin)
        routePoints.forEach {

            placemarksCollection.addPlacemark().apply {
                geometry = it.location
                setIcon(imageProvider, IconStyle().apply {
                    scale = 0.5f
                    zIndex = 20f
                })

                addTapListener { p0, p1 ->
                    Log.d("TTT", "Imenno meni nutqam bosildi")
                    val addressDialog = Dialog(this@MainActivity)

                    addressDialog.setContentView(R.layout.dialog_address)
                    addressDialog.window!!.findViewById<TextView>(R.id.tv_name).text = it.name
                    addressDialog.window!!.findViewById<TextView>(R.id.tv_description).text = it.description
                    addressDialog.window!!.findViewById<ShapeableImageView>(R.id.img_address).setImageResource(it.imageResId)


                    addressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    addressDialog.window?.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    addressDialog.window?.setGravity(Gravity.CENTER)

                    addressDialog.show()

                    true
                }
            }
        }

        if (routePoints.size < 2) return

        val requestPoints = buildList {
            add(RequestPoint(routePoints.first().location, RequestPointType.WAYPOINT, null, null))
            addAll(routePoints.subList(1, routePoints.size - 1).map { RequestPoint(it.location, RequestPointType.VIAPOINT, null, null) })
            add(RequestPoint(routePoints.last().location, RequestPointType.WAYPOINT, null, null))
        }

        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()

        drivingSession = drivingRouter.requestRoutes(
            requestPoints,
            drivingOptions,
            vehicleOptions,
            drivingRouteListener,
        )
    }

    private fun onRoutesUpdated() {
        routesCollection.clear()
        if (routes.isEmpty()) return

        routes.forEachIndexed { index, route ->
            routesCollection.addPolyline(route.geometry).apply {
                if (index == 0) styleMainRoute() else styleAlternativeRoute()
            }
        }
    }

    private fun PolylineMapObject.styleMainRoute() {
        zIndex = 8f
        setStrokeColor(ContextCompat.getColor(this@MainActivity, R.color.gray))
        strokeWidth = 3f
        outlineColor = ContextCompat.getColor(this@MainActivity, R.color.black)
        outlineWidth = 1f
    }

    private fun PolylineMapObject.styleAlternativeRoute() {
        zIndex = 3f
        setStrokeColor(ContextCompat.getColor(this@MainActivity, R.color.light_blue))
        strokeWidth = 2f
        outlineColor = ContextCompat.getColor(this@MainActivity, R.color.black)
        outlineWidth = 2f
    }

    companion object {

        private val START_POSITION = if (MyAddress.from_address != null) {
            CameraPosition(MyAddress.to_address!!.location, 13.0f, 0f, 0f)
        } else {
            CameraPosition(Point(59.941282, 30.308046), 13.0f, 0f, 0f)
        }

    }

    private fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}