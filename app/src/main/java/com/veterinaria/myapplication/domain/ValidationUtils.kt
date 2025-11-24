package com.veterinaria.myapplication.domain


object ValidationUtils {

  // Regex migrada del proyecto viejo: verifica @ y al menos un punto en el dominio.
  private val EMAIL_REGEX = Regex("^.+@.+\\..+$")
  // Regex migrada para 12 dígitos exactos.
  private val TELEFONO_REGEX = Regex("^\\d{12}$")

  /**
   * Valida el formato de un correo electrónico.
   */
  fun validarEmail(email: String): Boolean {
    return email.matches(EMAIL_REGEX)
  }

  /**
   * Valida que el teléfono contenga exactamente 12 dígitos.
   */
  fun validarTelefono(telefono: String): Boolean {
    return telefono.matches(TELEFONO_REGEX)
  }

  /**
   * Formatea el teléfono de 12 dígitos a +XX (XXX) XXX-XXXX (migrado de formatearTelefono).
   * @param telefono Cadena de 12 dígitos puros.
   * @return Cadena formateada o la cadena original si la longitud es incorrecta.
   */
  fun formatearTelefono(telefono: String): String {
    if (telefono.length != 12) return telefono

    val codigoPais = telefono.substring(0, 2)
    val codigoArea = telefono.substring(2, 5)
    val parteCentral = telefono.substring(5, 8)
    val parteFinal = telefono.substring(8, 12)
    return "+$codigoPais ($codigoArea) $parteCentral-$parteFinal"
  }

  /**
   * Aplica la regla de negocio para el nombre del tutor: 'Tutor desconocido' si está vacío.
   */
  fun getNombreTutor(nombreIngresado: String): String {
    return if (nombreIngresado.isBlank()) {
      "Tutor desconocido"
    } else {
      nombreIngresado
    }
  }
}