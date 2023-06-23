package com.example.myapplication.fragments

import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.ResourceUriHelper
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.DataSource.getAddress
import com.example.myapplication.data.ProductDatabase
import com.example.myapplication.data.model.ProductEntity
import com.example.myapplication.databinding.FragmentEditBinding
import com.example.myapplication.interfaces.Navigable
import com.google.android.gms.maps.model.LatLng
import kotlin.concurrent.thread

const val ARG_EDIT_ID = "edit_id"

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var db: ProductDatabase
    private var product: ProductEntity? = null
    private lateinit var curr: Uri
    private var resId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = ProductDatabase.open(requireContext())
        resId = R.drawable.camera
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEditBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        curr = ResourceUriHelper.getUriFromDrawableId(requireContext(), resId)

        if (DataSource.dbId != -1) {
            thread {
                product = db.products.getProduct(DataSource.dbId)
                requireActivity().runOnUiThread {
                    setupProductData(product)
                    if (DataSource.location != null) {
                        getAddressFromLocation(DataSource.location!!) { country, city, street ->
                            if (country != null) {
                                val addressString = getString(R.string.address_placeholder, country, city, street)
                                binding.latNameEdit.setText(addressString)
                            }
                        }
                    }
                }
            }
        } else {
            if (DataSource.saved) {
                binding.photo.setImageURI(DataSource.savedImageUri)
                curr = DataSource.savedImageUri!!
            }
            if (DataSource.location != null) {
                getAddressFromLocation(DataSource.location!!) { country, city, street ->
                    if (country != null) {
                        val addressString = getString(R.string.address_placeholder, country, city, street)
                        binding.latNameEdit.setText(addressString)
                    }
                }
            }
        }
        binding.save.setOnClickListener {

            val product = product?.copy(
                name = binding.nameEdit.text.toString(),
                loc = binding.latNameEdit.text.toString(),
                icon = curr,
                info = binding.editInfo.text.toString(),
                lat = DataSource.location?.latitude ?: 0.0,
                len = DataSource.location?.longitude ?: 0.0
            ) ?: ProductEntity(
                name = binding.nameEdit.text.toString(),
                loc = binding.latNameEdit.text.toString(),
                icon = DataSource.savedImageUri ?: curr,
                info = binding.editInfo.text.toString(),
                lat = DataSource.location?.latitude ?: 0.0,
                len = DataSource.location?.longitude ?: 0.0
            )
            this.product = product

            thread {
                db.products.addProduct(product)


            }
            DataSource.saved = false
            DataSource.savedImageUri = null
            DataSource.dbId = -1
            DataSource.location = null
            (activity as? Navigable)?.navigate(Navigable.Destination.List)
        }
        binding.map.setOnClickListener{
            (activity as? Navigable)?.navigate(Navigable.Destination.Map)
        }
        binding.photo.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Paint)
        }
        binding.delete.setOnClickListener {
            if (DataSource.dbId != -1) {
                thread {
                    db.products.remove(DataSource.dbId)
                }
            }
            (activity as? Navigable)?.navigate(Navigable.Destination.List)
        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

    private fun setupProductData(product: ProductEntity?) {
        binding.nameEdit.setText(product?.name.orEmpty())
        binding.editInfo.setText(product?.info.orEmpty())
        if (DataSource.location == null) {
            binding.latNameEdit.setText(product?.loc.orEmpty())
        }
        setImageAndCurr(product?.icon)
    }

    private fun getAddressFromLocation(location: LatLng, callback: (String?, String?, String?) -> Unit) {
        Geocoder(requireContext()).getAddress(location.latitude, location.longitude) { country, city, street ->
            callback(country, city, street)
        }
    }

    private fun setImageAndCurr(imageUri: Uri?) {
        if (DataSource.savedImageUri != null) {
            binding.photo.setImageURI(DataSource.savedImageUri)
            curr = DataSource.savedImageUri!!
        } else {
            binding.photo.setImageURI(imageUri)
            if (imageUri != null) {
                curr = imageUri
            }
        }
    }
}
