package org.example


// clase principal configurada como open para que las clases hijas puedan heredar datos de esta sin problemas.
// se definen variables de tipo String y Double.

open class ClaseGrupal(val nombre: String, val valorClase: Double) {

    /* hace una operación aritmética que calcula el valor de la inscripción, multiplicando
    el valor de cada clase por la cantidad de clases tomadas */

    // se declara como open para que spinning y yoga lo puedan cambiar con override

    open fun calcularCostoInscripcion(cantidadClases: Int): Double {
        return valorClase * cantidadClases
    }
}