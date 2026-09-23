package org.example

/*esta clase es una clase "hija" que hereda directamente desde ClaseGrupal. Los datos que hereda
  son: nombre y valorClase.

*/

class ClaseSpinning(nombre: String, valorClase: Double, val intensidadAlta: Boolean)
    : ClaseGrupal(nombre, valorClase) {

    /*
    se utiliza el override para sobrescribir el cálculo aritmético que
    se produce en la clase "padre" (ClaseGrupal).
     */

    override fun calcularCostoInscripcion(cantidadClases: Int): Double {
        /*
         obtiene el calculo base desde ClaseGrupal usando super (valor de la clase por la cantidad).
         este valor se usa en los dos casos: se devuelve tal cual si es spinning normal,
         o se le suma el 10% si es de alta intensidad.
        */
        val base: Double = super.calcularCostoInscripcion(cantidadClases)

        /*
         esta fórmula aritmética hace que se le sume un 10%
         al valor final de la clase si esta es de spinning de alta intensidad.
        */
        if (intensidadAlta) {
            return base * 1.10
        }
        return base
    }
}