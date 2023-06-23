package com.example.prmmp1poprawka

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.prmmp1poprawka.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentInfoBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.image.setImageResource(DataSource.animal.resId)
        binding.nameEdit.text = DataSource.animal.name
        binding.latNameEdit.text = DataSource.animal.latName
        binding.info.text = DataSource.animal.info

        binding.btnEdit.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Add)
        }

    }
}