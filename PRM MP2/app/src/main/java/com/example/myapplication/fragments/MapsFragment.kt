package com.example.myapplication.fragments

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.interfaces.Navigable
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.LocationRep
import com.example.myapplication.data.SharedPrefLocationRep
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng

const val RANGE = 200.0
const val STROKE_WIDTH = 10f

class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private lateinit var place: LatLng

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        turnOnLocation()
        googleMap.setOnMapClickListener (::onMapClick)
    }

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var locationRep: LocationRep

    @SuppressLint("MissingPermission")
    private val onPermissionResult = { _: Map<String, Boolean> ->
        if(ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            map.isMyLocationEnabled = true
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            onPermissionResult
        )
        locationRep = SharedPrefLocationRep(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
    private fun turnOnLocation() {
        if(ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            map.isMyLocationEnabled = true
        } else {
            permissionLauncher.launch(
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
                )
            )
        }
    }
    private fun onMapClick(latLng: LatLng){
        drawCircle(latLng)
        place = latLng
        this.setHasOptionsMenu(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.save -> {
                askForSave(place)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun askForSave(latLng: LatLng) {
        save(latLng)
        (activity as? Navigable)?.navigate(Navigable.Destination.Edit, DataSource.id)
    }

    private fun save(latLng: LatLng) {
        DataSource.location = latLng
        locationRep.savedLocation = latLng
    }

    private fun drawCircle(latLng: LatLng) {
        val circle = CircleOptions()
            .strokeColor(Color.BLACK)
            .radius(RANGE)
            .center(latLng)
            .strokeWidth(STROKE_WIDTH)

        map.apply {
            clear()
            addCircle(circle)
        }
    }

}