package com.example.prmmp1poprawka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prmmp1poprawka.databinding.FragmentEditBinding

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var adapter: AnimalImageAdapter
    private var position: Int = 0

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
        if (DataSource.flag == "edit") {
            position = DataSource.animal.id
            binding.nameEdit.setText(DataSource.animal.name)
            binding.latNameEdit.setText(DataSource.animal.latName)
            binding.editInfo.setText(DataSource.animal.info)
        } else {
            binding.nameEdit.setText("")
            binding.latNameEdit.setText("")
            binding.editInfo.setText("")
        }
        adapter = AnimalImageAdapter()

        binding.images.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.save.setOnClickListener {
            val newAnimal = Animal(
                DataSource.animals.maxOf { it.id } + 1,
                binding.nameEdit.text.toString(),
                binding.latNameEdit.text.toString(),
                adapter.selectedIdRes,
                binding.editInfo.text.toString()
            )
            if (DataSource.flag == "add") {
                DataSource.animals.add(newAnimal)
                (activity as? Navigable)?.navigate(Navigable.Destination.List)
            } else if (DataSource.flag == "edit") {
                val a = DataSource.animals.find { it.id == position }
                DataSource.animals.remove(a)
                DataSource.animals.add(newAnimal)
                (activity as? Navigable)?.navigate(Navigable.Destination.List)
            }
        }
    }
}