package com.veterinaria.myapplication.domain.annotations
import kotlin.annotation.AnnotationTarget
/*
 * Anotación utilizada para marcar medicamentos que califican para promociones especiales.
 * Migrada de 'annotations/Promocionable.kt'.
 * Se usa @Repeatable para poder aplicar múltiples promociones al mismo medicamento.
 */
@Target(AnnotationTarget.CLASS) // Solo se aplica a clases (Medicamento)
@Retention(AnnotationRetention.RUNTIME) // Necesaria para usar Reflection en tiempo de ejecución
@Repeatable
annotation class Promocionable(
	val nombre: String,
	val tipo: String,
	val descuento: Double // Descuento aplicado (ej: 0.1 para 10%)
)