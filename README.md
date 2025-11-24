# Proyecto Veterinaria - Experiencia 2

Este es un proyecto de Android nativo desarrollado en Kotlin que simula una aplicación para la gestión de citas en una clínica veterinaria. La aplicación demuestra una implementación moderna de la interfaz de usuario con Jetpack Compose y sigue una arquitectura limpia MVVM (Model-View-ViewModel).

## Descripción del Proyecto

La aplicación guía al usuario a través de un flujo de múltiples pasos para agendar una cita veterinaria, cubriendo desde el ingreso de los datos del dueño (Tutor) hasta los detalles de la mascota y el tipo de consulta. Adicionalmente, incluye un módulo de farmacia para la venta de medicamentos con descuentos.

El proyecto está diseñado para ser modular y escalable, separando claramente las responsabilidades entre la UI (capa de presentación), la lógica de negocio (capa de dominio) y los modelos de datos.

## Características Principales

*   **Flujo de Citas en Múltiples Pasos:**
    1.  **Ingreso de Datos del Tutor:** Formulario con validación en tiempo real para nombre, teléfono y email.
    2.  **Ingreso de Datos de la Mascota:** Formulario para nombre, especie, edad y peso, con validaciones específicas.
    3.  **Detalles de la Consulta:** Selección de tipo de consulta, asignación de veterinario y selección de hora.
    4.  **Resumen de la Cita:** Pantalla final que muestra un resumen completo y detallado de toda la información ingresada y los costos asociados.
*   **Módulo de Farmacia:** Un flujo independiente para simular la compra de medicamentos, aplicando descuentos basados en la categoría del cliente.
*   **Arquitectura MVVM:** Utiliza `ViewModel` para gestionar y persistir el estado de la UI, sobreviviendo a cambios de configuración.
*   **Navegación con Jetpack Compose:** Implementa un `NavHost` para gestionar la navegación entre las diferentes pantallas de la aplicación.
*   **Lógica de Negocio Aislada:** La capa de `domain` contiene toda la lógica de negocio, como el cálculo de costos, dosis, asignación de veterinarios y planificación de vacunas.
*   **Funcionalidades Avanzadas de Kotlin:**
    *   **Sobrecarga de Operadores:** Demostración de la combinación de pedidos de farmacia usando el operador `+`.
    *   **Reflexión (Reflection):** Una sección en el resumen de farmacia que analiza dinámicamente las propiedades y métodos de la clase `Medicamento` en tiempo de ejecución.

## Tecnologías y Librerías Utilizadas

*   **Lenguaje:** [Kotlin](https://kotlinlang.org/)
*   **Interfaz de Usuario:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Arquitectura:** MVVM (Model-View-ViewModel)
*   **Gestión de Estado:** `ViewModel` de Jetpack y `mutableStateOf` de Compose.
*   **Navegación:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
*   **Iconografía:** Material Design Icons (incluyendo la librería `material-icons-extended`).
*   **Componentes de UI:** [Material 3](https://m3.material.io/)
*   **Reflexión:** `kotlin-reflect`

## Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes principales dentro de `app/src/main/java/com/veterinaria/myapplication/`:

*   `data`: Contiene las clases de datos (data classes) como `Tutor`, `Mascota`, `Consulta`, `Medicamento`, etc.
*   `domain`: Contiene la lógica de negocio pura, aislada de Android. Ejemplos: `CalcularCosto.kt`, `AsignarVeterinario.kt`.
*   `presentation`:
    *   `ui`: Contiene todos los Composables que definen las pantallas (`TutorScreen.kt`, `MascotaScreen.kt`, etc.).
    *   `viewmodel`: Contiene las clases `ViewModel` (`ConsultaViewModel.kt`, `FarmaciaViewModel.kt`).
*   `ui.theme`: Define el tema de la aplicación, incluyendo colores y tipografía.
*   `utils`: Utilidades generales, como las relacionadas con el análisis de anotaciones por reflexión.
