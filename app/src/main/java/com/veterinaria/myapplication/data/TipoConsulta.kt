package com.veterinaria.myapplication.data

enum class TipoConsulta(val valorBase: Double, val descripcion: String, val descuento: Double) {
	PELUQUERIA(15000.0, "Peluqueria", 0.10), // 10% de descuento
	CONTROL(10000.0, "Control", 0.15),       // 15% de descuento
	OTRO(10000.0, "Otro", 0.0)             // Sin descuento
}