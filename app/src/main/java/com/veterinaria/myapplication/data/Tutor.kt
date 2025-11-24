package com.veterinaria.myapplication.data

class Tutor(
  nombre: String,
  val telefono: String,
  val email: String
): Usuario(nombre) {


  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Tutor) return false
    return this.nombre == other.nombre && this.email == other.email
  }

  override fun hashCode(): Int {
    var result = nombre.hashCode()
    result = 31 * result + email.hashCode()
    return result
  }
}