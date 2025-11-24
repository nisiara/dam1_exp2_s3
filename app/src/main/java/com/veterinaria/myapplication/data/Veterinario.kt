package com.veterinaria.myapplication.data

class Veterinario(
	nombre: String,
	val turno: List<String>
): Usuario(nombre) {}