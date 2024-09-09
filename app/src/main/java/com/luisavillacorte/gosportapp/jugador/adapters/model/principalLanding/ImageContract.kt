package com.luisavillacorte.gosportapp.jugador.adapters.model.principalLanding

interface ImageContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun displayImages(images: List<ImageData>)
        fun showError(message: String)
    }
    interface Presenter {
        fun loadImages()
    }
}