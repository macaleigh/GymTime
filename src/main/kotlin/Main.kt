package org.example

// se importan las librerías para poder hacer uso de corrutinas.
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/*
este bloque revisa que el valor de la clase no sea negativo,
si este llega a ser negativo, lanza un error que después se atrapa con un try-catch.
*/
fun validarClase(clase: ClaseGrupal) {
    if (clase.valorClase < 0) {
        throw IllegalArgumentException("${clase.nombre} tiene un valor negativo")
    }
}

/*
se calcula el total de la inscripción
con descuentos y recargos.
 */
fun calcularTotal(clase: ClaseGrupal, cantidadClases: Int, esSocio: Boolean, hora: Int): Double {

    /*
    se define como var, ya que el total puede ir cambiando,
    por lo que se necesita que tenga la capacidad de mutar.
    */
    // cada clase usa su propio cálculo (spinning o yoga).
    var total: Double = clase.calcularCostoInscripcion(cantidadClases)

    /*
    se calcula el descuento del 15% solo si son
    2 clases o más y además es socio del gimnasio.
     */
    if (cantidadClases >= 2 && esSocio) {
        total = total - total * 0.15
    }

    /*
     si la clase es entre las 18:00 y las 21:00,
     se cobran 2000 extra por clase.
     */
    if (hora in 18..21) {
        total = total + 2000 * cantidadClases
    }

    return total
}


/*
este bloque tiene la funcionalidad de simular que se conecta
con una pasarela de pago.
 */
/*
se utiliza un suspend, ya que
también está siendo utilizado un delay para esperar un segundo.
 */
suspend fun confirmarInscripcion(monto: Double): Boolean {
    delay(1000)
    return monto > 0
}


/*
 llama a la corrutina y convierte el true/false en un estado de la clase sellada.
 */
suspend fun inscribir(clase: ClaseGrupal, total: Double): ResultadoInscripcion {
    val exito: Boolean = confirmarInscripcion(total)
    if (exito) {
        return ResultadoInscripcion.Exitosa(clase.nombre, total)
    }
    return ResultadoInscripcion.Rechazada("${clase.nombre}: el pago no fue aprobado")
}

/*
 muestra un mensaje distinto según el estado de cada solicitud.
 como es sealed class, el when obliga a cubrir los 3 casos.
 */
fun mostrarResultado(resultado: ResultadoInscripcion) {
    when (resultado) {
        is ResultadoInscripcion.Procesando -> println("  Procesando pago...")
        is ResultadoInscripcion.Exitosa -> println("  ${resultado.nombreClase} confirmada, total pagado $${String.format("%.0f", resultado.total)}")
        is ResultadoInscripcion.Rechazada -> println("  Inscripcion rechazada: ${resultado.mensaje}")
    }
}

/*
 este bloque muestra en pantalla el nombre
 y valor de cada clase disponible de la lista.
 */
fun mostrarClases(clases: List<ClaseGrupal>) {
    println("\n--- Clases disponibles ---")
    for (clase in clases) {
        println("${clase.nombre}: $${clase.valorClase}")
    }
}

/*
 se utiliza 'runBlocking' para poder llamar a las
 funciones suspend desde el main.
 */
fun main() = runBlocking {

    /*
    lista con todas las clases disponibles.
    la clase 5 tiene un valor negativo para utilizar el try-catch.
     */
    val todasLasClases: List<ClaseGrupal> = listOf(
        ClaseSpinning("Clase 1", 12000.0, true),
        ClaseSpinning("Clase 2", 8000.0, false),
        ClaseYoga("Clase 3", 10000.0, true),
        ClaseYoga("Clase 4", 7000.0, false),
        ClaseSpinning("Clase 5", -3000.0, true),
        ClaseYoga("Clase 6", 9000.0, false)
    )

    /*
     aqui se guardan solo las clases que están y no provocan errores.
     es mutable porque se van agregando valores con add.
     */
    val clasesValidas: MutableList<ClaseGrupal> = mutableListOf()

    /*
     se recorren todas las clases y se validan,
     si una de ellas falla, no se agrega a la lista y el programa sigue sin caerse.
     */
    for (clase in todasLasClases) {
        try {
            validarClase(clase)
            clasesValidas.add(clase)
        } catch (e: IllegalArgumentException) {
            println("Clase no valida: ${e.message}, la clase no se considera")
        }
    }

    println("Clases ingresadas: ${todasLasClases.size} - Clases validas: ${clasesValidas.size}")

    /*
    se muestran en la consola solo las clases válidas
     */
    mostrarClases(clasesValidas)

    /*
     se utiliza un filter para dejar solo las clases de menos de 10.000.
     se utiliza map para convertirlas en texto con nombre y valor.
     */
    val montoLimite: Double = 10000.0
    val clasesBaratas: List<String> = clasesValidas
        .filter { it.valorClase < montoLimite }
        .map { "${it.nombre} - $${it.valorClase}" }

    println("\n--- Clases de menos de $$montoLimite ---")
    for (texto in clasesBaratas) {
        println(texto)
    }

    /*
    se usa un sumOf para sacar cuanto se ganaría
    si un cliente toma 2 clases de cada una.
     */
    val ingresoTotal: Double = clasesValidas.sumOf { it.calcularCostoInscripcion(2) }
    println("\nIngreso total con 2 clases de cada una: $$ingresoTotal")

    /*
    se prueba inscribir a un cliente que es socio del gimnasio
    en cada clase, con 2 clases a las 19 hrs.
     */
    println("\n--- Inscripciones (2 clase, socio, 19 hrs) ---")
    for (clase in clasesValidas) {
        val total: Double = calcularTotal(clase, 2, true, 19)

        /*
         se muestra en consola el estado Procesando,
         para después imprimir en pantalla el resultado final.
         */
        mostrarResultado(ResultadoInscripcion.Procesando)
        val resultado: ResultadoInscripcion = inscribir(clase, total)
        mostrarResultado(resultado)
    }

    /*
    se hace la prueba con otros casos para mostrar que las condiciones cambian el resultado.
     */
    println("\n--- Otros casos con la Clase 1 ---")

    /*
     en este caso se utiliza:
     1 clase, cliente socio, 10 hrs: no hay descuento, ni recargo.
     */
    val caso1: Double = calcularTotal(clasesValidas[0], 1, true, 10)
    println("1 clase, socio, 10 hrs: $${String.format("%.0f", caso1)}")

    /*
    en este caso se utiliza:
    3 clases, no es cliente socio, 21 hrs: no hay descuento de membresía pero sí recargo.
     */
    val caso2: Double = calcularTotal(clasesValidas[0], 3, false, 21)
    println("3 clases, no socio, 21 hrs: $${String.format("%.0f", caso2)}")
}