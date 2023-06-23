package com.example.prmmp1poprawka

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prmmp1poprawka.databinding.FragmentListBinding


class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: AnimalsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logo.setImageResource(R.drawable.animals)
        adapter = AnimalsAdapter().apply {
            DataSource.animals.sortBy { animal: Animal -> animal.name.lowercase() }
            replace(DataSource.animals)
            setOnItemClickListener(object : AnimalsAdapter.OnItemClickListener {
                override fun onItemClick(animal: Animal) {
                    (activity as? Navigable)?.navigate(Navigable.Destination.Info)
                    DataSource.animal = animal
                    DataSource.flag = "edit"
                }
            })
            setOnItemLongClickListener { animal ->
                showDeleteConfirmationDialog(animal)
                true
            }
        }
        binding.list.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.counter.text = DataSource.animals.size.toString()
        binding.btnAdd.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Add)
            DataSource.flag = "add"
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.replace(DataSource.animals)
    }

    private fun showDeleteConfirmationDialog(animal: Animal) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirm_delete_title))
            .setMessage(getString(R.string.confirm_delete_message, animal.name))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                DataSource.animals.remove(animal)
                adapter.replace(DataSource.animals)
                binding.counter.text = DataSource.animals.size.toString()
            }
            .setNegativeButton(getString(R.string.no), null)
            .create()
            .show()
    }
}