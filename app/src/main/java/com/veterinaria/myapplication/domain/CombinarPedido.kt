package com.veterinaria.myapplication.domain

import com.veterinaria.myapplication.data.Cliente
import com.veterinaria.myapplication.data.Medicamento
import com.veterinaria.myapplication.data.Pedido

/*
 * Contiene la lógica para combinar dos objetos Pedido.
 * Esta lógica fue migrada de la sobrecarga del operador '+' en la clase Pedido original.
 * Utiliza las sobrecargas de operador definidas en Cliente y Medicamento (capa Data).
 */
object CombinarPedido {
	
	/*
	 * Combina dos pedidos en uno solo, sumando totales y combinando nombres.
	 * @param pedido1 Primer pedido.
	 * @param pedido2 Segundo pedido.
	 * @return Un nuevo objeto Pedido que representa la combinación.
	 */
	fun combinar(pedido1: Pedido, pedido2: Pedido): Pedido {
		// Usa la sobrecarga de Cliente y Medicamento (Cliente + otroCliente)
		val nombreClienteCombinado = pedido1.cliente + pedido2.cliente
		val nombreMedicamentoCombinado = pedido1.medicamento + pedido2.medicamento
		
		// Creamos un nuevo cliente combinado (solo para el nombre y manteniendo datos de contacto del primero)
		val clienteCombinado = Cliente(
			nombre = nombreClienteCombinado,
			telefono = pedido1.cliente.telefono,
			email = pedido1.cliente.email
		)
		
		// Creamos un nuevo medicamento combinado (solo para el nombre y manteniendo tipo/precio del primero)
		val medicamentoCombinado = Medicamento(
			nombre = nombreMedicamentoCombinado,
			tipo = pedido1.medicamento.tipo,
			precio = pedido1.medicamento.precio,
		)
		
		// Devolvemos el Pedido combinado con el total sumado
		return Pedido(
			cliente = clienteCombinado,
			medicamento = medicamentoCombinado,
			total = pedido1.total + pedido2.total,
			precioNormal = pedido1.precioNormal + pedido2.precioNormal,
			inicioPromocion = pedido1.inicioPromocion,
			terminoPromocion = pedido1.terminoPromocion
		)
	}
}