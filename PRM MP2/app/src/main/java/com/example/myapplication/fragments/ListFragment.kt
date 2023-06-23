package com.example.myapplication.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapters.ProductAdapter
import com.example.myapplication.data.DataSource
import com.example.myapplication.data.ProductDatabase
import com.example.myapplication.data.model.ProductEntity
import com.example.myapplication.databinding.FragmentListBinding
import com.example.myapplication.interfaces.Navigable
import com.example.myapplication.location.Geofencing
import com.google.android.gms.maps.model.LatLng
import kotlin.concurrent.thread

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: ProductAdapter
    private lateinit var db: ProductDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = ProductDatabase.open(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        loadData()
    }

    private fun setupViews() {
        binding.logo.setImageResource(R.drawable.wish_list)

        adapter = ProductAdapter().apply {
            setOnItemClickListener(object : ProductAdapter.OnItemClickListener {
                override fun onItemClick(product: ProductEntity) {
                    DataSource.dbId = product.id
                    (activity as? Navigable)?.navigate(Navigable.Destination.Edit)
                }
            })
            setOnItemLongClickListener { product ->
                showDeleteConfirmationDialog(product)
                true
            }
        }

        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(requireContext())

        binding.btnAdd.setOnClickListener {
            thread {
                (activity as? Navigable)?.navigate(Navigable.Destination.Add)
                loadData()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData() {
        thread {
            val products = db.products.getAll()
            products.forEach { productEntity ->
                val latLng = LatLng(productEntity.lat, productEntity.len)
                Geofencing.createGeofence(requireContext(), latLng, productEntity.name)
            }

            requireActivity().runOnUiThread {
                adapter.replace(products)
            }
        }
    }

    private fun showDeleteConfirmationDialog(product: ProductEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirm_delete_title))
            .setMessage(getString(R.string.confirm_delete_message, product.name))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                thread {
                    db.products.remove(product.id)
                    loadData()
                }
            }
            .setNegativeButton(getString(R.string.no), null)
            .create()
            .show()
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}
