package com.example.myapplication.fragments

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.myapplication.interfaces.Navigable
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.FragmentPaintBinding
import java.io.Closeable
import java.io.IOException

class PaintFragment : Fragment() {
    private lateinit var binding: FragmentPaintBinding
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var imageUri: Uri? = null

    private val onTakePhoto: (Boolean) -> Unit = { photo ->
        if (!photo) {
            (activity as? Navigable)?.navigate(Navigable.Destination.Edit)
        } else {
            loadPhoto()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaintBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        createPicture()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun save() {
        val imageUri = imageUri ?: return

        val bmp = binding.paintView.generateBitmap()

        requireContext().contentResolver.openOutputStream(imageUri)?.use { outputStream ->
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        }

        DataSource.savedImageUri = imageUri
        DataSource.saved = true
        (activity as? Navigable)?.navigate(Navigable.Destination.Edit, DataSource.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture(),
            onTakePhoto
        )
    }

    private fun loadPhoto() {
        val imageUri = imageUri ?: return

        requireContext().contentResolver.openInputStream(imageUri)?.use { inputStream ->
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.paintView.background = bitmap
        }
    }

    private fun createPicture() {
        val imagesUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }

        imageUri = requireContext().contentResolver.insert(imagesUri, contentValues)
        cameraLauncher.launch(imageUri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        closeResources()
    }

    private fun closeResources() {
        imageUri?.let { uri ->
            try {
                requireContext().contentResolver.openFileDescriptor(uri, "r")?.use { fileDescriptor ->
                    (fileDescriptor as? Closeable)?.close()
                }
            } catch (_: IOException) {
            }
        }
    }
}
