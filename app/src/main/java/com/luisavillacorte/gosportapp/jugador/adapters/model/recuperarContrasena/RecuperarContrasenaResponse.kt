package com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena



data class SolicitarCodigoRequest(val correo: String)
data class SolicitarCodigoResponse(val message: String)

data class VerificarCodigoRequest(val correo: String, val codigo: String)
data class VerificarCodigoResponse(val message: String)

data class CambiarContrasenaRequest(val correo: String, val nuevaContrasena: String)
data class CambiarContrasenaResponse(val message: String)

