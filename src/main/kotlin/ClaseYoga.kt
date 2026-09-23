package org.example

// hereda de ClaseGrupal, al igual que spinning
class ClaseYoga(nombre: String, valorClase: Double, val duracionExtendida: Boolean)
    : ClaseGrupal(nombre, valorClase) {


    /*
    se utiliza el override para sobrescribir el cálculo aritmético que
    se produce en la clase "padre" (ClaseGrupal).
     */
    override fun calcularCostoInscripcion(cantidadClases: Int): Double {

        /*
         obtiene el calculo base desde ClaseGrupal usando super (valor de la clase por la cantidad).
         este valor se usa en los dos casos: se devuelve tal cual si es yoga con duracion normal,
         o se le suman $2.500 pesos si es una clase de duración extendida.
        */
        val base: Double = super.calcularCostoInscripcion(cantidadClases)

        /*
        si la clase es de duracion extendida,
        se suman 2500 por cada clase
        */
        if (duracionExtendida) {
            return base + 2500 * cantidadClases
        }
        return base
    }
}