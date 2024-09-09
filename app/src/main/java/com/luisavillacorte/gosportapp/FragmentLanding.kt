package com.luisavillacorte.gosportapp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.luisavillacorte.gosportapp.jugador.adapters.model.principalLanding.ImageAdapter
import com.luisavillacorte.gosportapp.jugador.adapters.model.principalLanding.ImageContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.principalLanding.ImageData
import com.luisavillacorte.gosportapp.jugador.adapters.model.principalLanding.ImagePresenter
import com.luisavillacorte.gosportapp.jugador.viewActivities.activities.activitiesAuth.ActivityLogin

class FragmentLanding : Fragment(), ImageContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: ImagePresenter
    private lateinit var imageAdapter: ImageAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private val autoScrollInterval: Long = 1700 // 1.7 segundos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_landing, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerViewImages)
        progressBar = rootView.findViewById(R.id.progressBar)
        val btnLanding: Button = rootView.findViewById(R.id.buttonlanding)

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Al hacer clic en el botón, se inicia la actividad de login.
        btnLanding.setOnClickListener {
            val intent = Intent(requireContext(), ActivityLogin::class.java)
            startActivity(intent)
        }

        imageAdapter = ImageAdapter(emptyList()) { imageData ->
            showModal(imageData)
        }
        recyclerView.adapter = imageAdapter

        presenter = ImagePresenter(this)
        presenter.loadImages()

        return rootView
    }

    private fun startAutoScroll() {
        val runnable = object : Runnable {
            override fun run() {
                if (imageAdapter.itemCount > 0) {
                    currentPage++

                    if (currentPage >= imageAdapter.itemCount) {
                        currentPage = 0
                        recyclerView.scrollToPosition(currentPage)
                    } else {
                        recyclerView.smoothScrollToPosition(currentPage)
                    }
                }
                handler.postDelayed(this, autoScrollInterval)
            }
        }
        handler.postDelayed(runnable, autoScrollInterval)
    }

    override fun displayImages(images: List<ImageData>) {
        imageAdapter.updateData(images)
        startAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }

    private fun showModal(imageData: ImageData) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.modalfoto)

        val imageViewModal: ImageView = dialog.findViewById(R.id.imageViewModal)
        val textViewNombreModal: TextView = dialog.findViewById(R.id.textViewNombreModal)
        val textViewDescripcionModal: TextView = dialog.findViewById(R.id.textViewDescripcionModal)

        Glide.with(requireContext()).load(imageData.ImageUrl).into(imageViewModal)
        textViewNombreModal.text = imageData.Nombre
        textViewDescripcionModal.text = imageData.Descripcion

        dialog.show()
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showError(message: String) {
        if (isAdded && context != null)
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
