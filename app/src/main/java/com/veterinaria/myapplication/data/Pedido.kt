package com.veterinaria.myapplication.data
import java.time.LocalDate

/*
 * Representa un Pedido de farmacia.
 * Modelo de Datos (Data Layer) - Solo almacena información.
 * Se ha eliminado la sobrecarga del operador '+' (plus) para moverla a la capa Domain.
 */
class Pedido(
	val cliente: Cliente,
	val medicamento: Medicamento,
	val total: Int, // Total final a pagar
	val precioNormal: Int, // Precio base sin descuento
	val inicioPromocion: LocalDate,
	val terminoPromocion: LocalDate,
) {
	
	// Component functions para desestructuración (Se mantienen para tu requerimiento original)
	operator fun component1(): Int = this.total
	operator fun component2(): String = this.cliente.nombre
	operator fun component3(): String = this.medicamento.nombre
}