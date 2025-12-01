# Proyecto Veterinaria - Experiencia 2

Este es un proyecto de Android nativo desarrollado en Kotlin que simula una aplicación para la gestión de citas en una clínica veterinaria. La aplicación demuestra una implementación moderna de la interfaz de usuario con Jetpack Compose y sigue una arquitectura limpia MVVM (Model-View-ViewModel).

## Descripción del Proyecto

La aplicación guía al usuario a través de un flujo de múltiples pasos para agendar una cita veterinaria, cubriendo desde el ingreso de los datos del dueño (Tutor) hasta los detalles de la mascota y el tipo de consulta. Adicionalmente, incluye un módulo de farmacia para la venta de medicamentos con descuentos.

El proyecto ha sido actualizado con características modernas de UI/UX para ofrecer una experiencia de usuario más fluida, atractiva y profesional.

## Características Principales

### Funcionalidad Core

*   **Flujo de Citas en Múltiples Pasos:**
    1.  **Ingreso de Datos del Tutor:** Formulario con validación en tiempo real para nombre, teléfono y email.
    2.  **Ingreso de Datos de la Mascota:** Formulario para nombre, edad, peso y un **menú desplegable para la selección de especie**.
    3.  **Detalles de la Consulta:** Selección de tipo de consulta, asignación de veterinario y selección de hora.
    4.  **Resumen de la Cita:** Pantalla final que muestra un resumen completo y detallado, precedido por un indicador de carga.
*   **Módulo de Farmacia:** Un flujo independiente para simular la compra de medicamentos, aplicando descuentos basados en la categoría del cliente.
*   **Arquitectura MVVM:** Utiliza `ViewModel` para gestionar y persistir el estado de la UI, sobreviviendo a cambios de configuración.
*   **Lógica de Negocio Aislada:** La capa de `domain` contiene toda la lógica de negocio, como el cálculo de costos, dosis, asignación de veterinarios y planificación de vacunas.
*   **Funcionalidades Avanzadas de Kotlin:**
    *   **Sobrecarga de Operadores:** Demostración de la combinación de pedidos de farmacia usando el operador `+`.
    *   **Reflexión (Reflection):** Una sección en el resumen de farmacia que analiza dinámicamente las propiedades y métodos de la clase `Medicamento` en tiempo de ejecución.

### Mejoras de Interfaz de Usuario (UI/UX)

*   **SplashScreen Personalizado:** Una pantalla de inicio visualmente atractiva que incluye:
    *   El logo de la aplicación.
    *   Un `CircularProgressIndicator` para indicar el proceso de carga inicial.
    *   Un fondo personalizado consistente con el tema de la app.
*   **Animaciones de Navegación:** Transiciones suaves de tipo **Fade-In/Fade-Out** entre pantallas, aplicadas a toda la navegación de la aplicación para una experiencia más fluida.
*   **Indicadores de Carga Asíncronos:** Las pantallas de resumen (`ResumenScreen` y `ResumenFarmaciaScreen`) ahora muestran un `CircularProgressIndicator` con un texto descriptivo, simulando el procesamiento de datos y mejorando la percepción del usuario.
*   **Componentes Interactivos Mejorados:**
    *   Se ha reemplazado el campo de texto de "Especie" por un **`DropdownMenu`**, estandarizando la entrada de datos y evitando errores de escritura.
    *   Se ha añadido un **menú contextual** (ícono `MoreVert`) en la barra de navegación superior de todas las pantallas principales, preparado para futuras acciones como "Configuración" o "Iniciar Sesión".
*   **Diseño Visual Moderno y Consistente:**
    *   Tema de la aplicación con un **fondo blanco (`#FFFFFF`)** en todas las vistas para un look limpio y profesional.
    *   Uso de imágenes (`img_home.png` en la bienvenida) para enriquecer la interfaz y guiar visualmente al usuario.
    *   Diseño de `BienvenidaScreen` reestructurado para una mejor jerarquía visual, con una imagen principal sin márgenes y contenido centrado con padding.

## Tecnologías y Librerías Utilizadas

*   **Lenguaje:** [Kotlin](https://kotlinlang.org/)
*   **Interfaz de Usuario:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Arquitectura:** MVVM (Model-View-ViewModel)
*   **Gestión de Estado:** `ViewModel` de Jetpack y `mutableStateOf` de Compose.
*   **Navegación:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) con animaciones personalizadas.
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
*   `utils`: Utilidades generales (actualmente vacío).
