package com.luisavillacorte.gosportapp.jugador.adapters.apiService.landingService

import com.luisavillacorte.gosportapp.jugador.adapters.model.principalLanding.ImageData
import retrofit2.Call
import retrofit2.http.GET

interface ImageApiService {
    @GET("photo")
    fun getImages(): Call<List<ImageData>>
}