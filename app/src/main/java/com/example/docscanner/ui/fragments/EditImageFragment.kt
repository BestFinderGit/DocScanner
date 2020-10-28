package com.example.docscanner.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.docscanner.R
import com.example.docscanner.data.models.Document
import com.example.docscanner.ui.adapters.EditImageAdapter
import com.example.docscanner.ui.viewmodels.CameraViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.edit_images_fragment.*
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class EditImageFragment : Fragment(R.layout.edit_images_fragment),EditImageAdapter.EditImageListener {

    private lateinit var editImageAdapter: EditImageAdapter
    private val vm: CameraViewModel by activityViewModels()

    private lateinit var callback: EditImageInteractor

    @Inject
    lateinit var glide:RequestManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpEditImageAdapter()

        subscribeToObservers()

        Timber.d("vm : $vm")

        imgBack.setOnClickListener {
            Timber.d("edit image: $callback")
            callback.onNavigateToCapture()
        }

    }

    private fun subscribeToObservers() {
        vm.docList.observe(requireActivity(), Observer {
            it?.let {
                if (imgEditDoc!=null){
                    editImageAdapter.setData(it)
                    editImageAdapter.setCurrentPosition(0)
                    glide.apply {
                        load(it[0].bitmap)
                                .into(imgEditDoc)
                    }
                }
            }
        })
    }

    private fun setUpEditImageAdapter() {
        editImageAdapter = EditImageAdapter(requireContext(), this, glide)
        rvImages.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = editImageAdapter
        }
    }

    override fun onImageClicked(document: Document) {
        glide.apply {
            load(document.bitmap)
                    .into(imgEditDoc)
        }
    }

    interface EditImageInteractor{
        fun onNavigateToCapture()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as EditImageInteractor
    }

}