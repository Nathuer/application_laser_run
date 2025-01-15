package com.example.application_laser_run

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("laserrun.json")  // Modifiez cette ligne pour correspondre à l'URL de l'API
    suspend fun getCategories(): Response<List<Category>>


}
