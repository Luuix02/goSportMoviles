package com.luisavillacorte.gosportapp.jugador.adapters.model.principalLanding

import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.landingService.ImageApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImagePresenter(private val view: ImageContract.View) : ImageContract.Presenter {

    private val apiService = RetrofitInstance.createService(ImageApiService::class.java)

    override fun loadImages() {
        view.showLoading()
        apiService.getImages().enqueue(object : Callback<List<ImageData>> {
            override fun onResponse(call: Call<List<ImageData>>, response: Response<List<ImageData>>) {
                view.hideLoading()
                if (response.isSuccessful) {
                    response.body()?.let {
                        view.displayImages(it)
                    } ?: run {
                        view.showError("No images found")
                    }
                } else {
                    view.showError("Failed to load images")
                }
            }

            override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
                view.hideLoading()
                view.showError(t.message ?: "Unknown error")
            }
        })
    }


}
