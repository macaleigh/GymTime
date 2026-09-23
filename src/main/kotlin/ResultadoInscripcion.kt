package org.example

/*
clase sellada con los estados posibles de la inscripción.
solo pueden existir tres tipos de estados debido a sealed.
 */
sealed class ResultadoInscripcion {

    /*
    corresponde al estado mientras se espera la respuesta de la pasarela de pago.
     */
    object Procesando : ResultadoInscripcion()

    /*
    corresponde al estado cuando el pago sale bien, guarda la clase y el total pagado.
     */
    data class Exitosa(val nombreClase: String, val total: Double) : ResultadoInscripcion()

    /*
    corresponde al estado cuando el pago falla, guarda el mensaje del motivo del rechazo.
     */
    data class Rechazada(val mensaje: String) : ResultadoInscripcion()
}