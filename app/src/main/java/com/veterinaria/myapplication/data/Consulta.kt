package com.veterinaria.myapplication.data



/**
 * Modelo de Datos (Data Class) limpio para Consulta.
 * Se elimina la lógica de cálculo y presentación ('calcularTotalConsulta', 'resumenConsulta').
 */
class Consulta(
	val idConsulta: Int,
	val tipo: TipoConsulta,
	val valorTotal: Double, // Almacena el valor FINAL después de descuentos
	val hora: String,
	val mascota: Mascota,
	val nombreVeterinario: String
) {}