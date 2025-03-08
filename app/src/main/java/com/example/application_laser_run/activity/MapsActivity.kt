package com.example.application_laser_run.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.application_laser_run.R
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

class MapsActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_maps)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mapView = findViewById(R.id.mapView)

        addMarker()
        listenToNewMarker()
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mapView.setMultiTouchControls(true)

        Configuration.getInstance().setUserAgentValue(getPackageName())
        val mapController = mapView.controller
        mapController.setZoom(18.0)
        mapController.setCenter(GeoPoint(46.5802596, 0.340196))
    }

    private fun addMarker() {
        val clickListener = { marker: Marker, mapView: MapView ->
            Toast.makeText(this, marker.snippet, Toast.LENGTH_SHORT).show()
            marker.showInfoWindow()
            true
        }

        listOf(
            Triple(
                "Stade Paul-Rébeilleau",
                "54 Av. Jacques Cœur, 86000 Poitiers",
                GeoPoint(46.5688389, 0.3733891)
            ),
            Triple(
                "Stade Poitevin Omnisports",
                "Rue de la Devinière, 86000 Poitiers",
                GeoPoint(46.56896, 0.377033)
            ),
            Triple(
                "Stade Poitevin Club",
                "2 Rue de la Fraternité, 86180 Buxerolles",
                GeoPoint(46.6167, 0.4833)
            )
        ).forEach {
            val startMaker = Marker(mapView)
            startMaker.title = it.first
            startMaker.snippet = it.second
            startMaker.position = it.third
            startMaker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            startMaker.setOnMarkerClickListener(clickListener)
            mapView.overlays.add(startMaker)
        }
    }

    private fun listenToNewMarker() {
        val evOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                return false
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                val startMarker = Marker(mapView)
                startMarker.title = "Nouveau Point"
                startMarker.position = p
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                mapView.overlays.add(startMarker)
                mapView.invalidate()
                return true
            }
        })
        mapView.overlays.add(evOverlay)
    }
}