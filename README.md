# 📱 N-Proyect — Tienda de Moda E-Commerce para Android

**N-Proyect** es una aplicación móvil nativa de comercio electrónico para Android desarrollada en **Java** con base de datos local **SQLite**. Es un proyecto completo de fin de ciclo para DAM (Desarrollo de Aplicaciones Multiplataforma) que incluye una experiencia integral tanto para el cliente final como para el personal de administración de la tienda.

---

## 🚀 Tecnologías y Arquitectura

| Componente | Tecnología |
|---|---|
| **Lenguaje** | Java (JDK 17) |
| **Plataforma** | Android SDK (Min SDK: 26 / Target SDK: 34 / API 31+) |
| **Base de Datos** | SQLite (base de datos pre-cargada desde `assets/n_proyect.db`) |
| **Diseño UI** | Material Design Component Library + XML layouts |
| **Construcción** | Gradle (Kotlin DSL) |
| **Recursos Multimedia** | Videos interactivos (`.mp4`) integrados para previsualización de productos |

---

## ✨ Características y Funcionalidades

La aplicación cuenta con dos roles principales completamente funcionales que se diferencian automáticamente al iniciar sesión:

### 🛍️ Panel del Cliente (Rol: `cliente`)
* **Registro y Autenticación Segura**: Sistema de inicio de sesión persistente controlado por sesión local.
* **Catálogo Organizado**: Exploración interactiva clasificada por género (**Hombre** / **Mujer**) y por categorías (**Camisetas, Sudaderas, Pantalones, Polos, Abrigos y Punto**).
* **Ficha de Producto Premium**: Detalle con descripción, precio, selección de talla obligatoria y **reproducción de video de demostración local** en formato `.mp4`.
* **Carrito de Compras Reactivo**: Permite añadir productos, ver el total, ajustar cantidades y eliminar artículos guardados directamente en la base de datos SQLite.
* **Pasarela de Pago Simulada**: Soporte para pago interactivo mediante **Tarjeta de Crédito** y **Bizum**.
* **Seguimiento e Historial**: Consulta de pedidos realizados con el detalle de los productos adquiridos.
* **Mi Perfil**: Editor de datos personales (nombre, teléfono, correo y dirección).

### ⚙️ Panel de Administración (Rol: `admin`)
* **Panel de Control Integral**: Vista unificada para la gestión comercial.
* **Gestión de Usuarios**: Listado de todos los usuarios registrados con opción de cambiar sus roles (ascender a administrador / degradar a cliente) y eliminarlos del sistema.
* **Gestión de Productos**: Altas, bajas y modificaciones del catálogo.
* **Control de Stock y Inventario**: Actualización y visualización rápida del número de existencias disponibles de cada producto y talla.
* **Gestión de Pedidos**: Visualización de los pedidos realizados por todos los clientes con la capacidad de cambiar su estado en tiempo real (**Pendiente, Enviado, Entregado, Cancelado**).

---

## 📦 Guía de Instalación y Uso

Sigue estos pasos para arrancar el proyecto en tu entorno de desarrollo local usando **Android Studio**:

### Requisitos Previos
* **Android Studio** (versión Jellyfish / Koala o superior recomendada).
* **JDK 17** instalado y configurado en Android Studio.
* Emulador Android (mínimo API 26) o un dispositivo físico Android conectado por USB en modo depuración.

### Paso 1: Clonar e Importar
1. Abre **Android Studio**.
2. Selecciona **File > New > Import Project** (o selecciona *Open* desde la pantalla de bienvenida).
3. Busca la carpeta del proyecto en tu disco local:
   `/home/nacho/DAW/docker-dwes/www/nacho/Proyecto_intermoludar/Robledo_Pedroviejo_Nacho_ProyectoFinalDAM/RobledoPedroviejoNacho_Proyecto/N_Proyect`
4. Haz clic en **OK**. Android Studio importará el proyecto y sincronizará Gradle automáticamente (puede tardar unos minutos en la primera sincronización).

### Paso 2: Ejecutar en el Emulador / Dispositivo
1. Asegúrate de tener seleccionado el módulo `app` en la configuración de ejecución.
2. Selecciona el emulador o dispositivo de prueba en el menú superior.
3. Haz clic en el botón de reproducción verde **Run 'app'** (o presiona `Shift + F10`).

---

## 🔐 Cuentas de Acceso y Pruebas

Dado que la aplicación incluye una base de datos SQLite pre-cargada (`n_proyect.db`), puedes registrarte libremente como **cliente** desde la pantalla de registro, o utilizar cuentas ya guardadas en el sistema.

> [!NOTE]
> Puedes consultar o registrar usuarios administradores directamente en la base de datos pre-cargada. Los nuevos registros que se realicen a través de la aplicación se guardarán por defecto con el rol de **cliente**, pudiendo ser ascendidos desde el panel de administración.

---

## 🗂️ Estructura del Proyecto

El código fuente está estructurado de manera modular y limpia en los siguientes paquetes principales:

```
├── app/src/main/
│   ├── java/com/example/n_proyect/
│   │   ├── Admin/              # Actividades y vistas de Administración (pedidos, productos, usuarios, stock)
│   │   ├── BD/                 # Controlador de Base de datos SQLite (GestorN_Proyect.java)
│   │   ├── Clases/             # Modelos de datos (Usuario, Producto, Pedido, Carrito, etc.)
│   │   ├── Cliente/            # Actividades del Cliente (carrito, catálogo, compras, pago, perfil)
│   │   ├── ProductosHombre/    # Actividades de catálogo de hombre por categoría
│   │   ├── ProductosMujer/     # Actividades de catálogo de mujer por categoría
│   │   ├── inicio/             # Pantallas de Login, Registro y Presentación Principal
│   │   ├── ControlSesion.java  # Clase auxiliar para control de persistencia de sesión
│   │   ├── ProductoAdaptador.java # Adaptador para el RecyclerView del catálogo de productos
│   │   └── ProductoImagen.java # Actividad para visor de imágenes de producto
│   ├── assets/
│   │   └── n_proyect.db        # Base de datos SQLite inicial pre-poblada con datos
│   ├── res/
│   │   ├── layout/             # Diseños de interfaz XML (cliente, admin y genéricos)
│   │   ├── raw/                # Videos interactivos .mp4 para las vistas de productos
│   │   └── values/             # Recursos de colores, strings y temas Material Design
│   └── AndroidManifest.xml     # Manifiesto de Android con todas las actividades registradas
```

---

## 📁 Documentación del Proyecto

* **Memoria del Proyecto**: El documento PDF explicativo y memoria técnica formal del proyecto se encuentra disponible en la raíz del repositorio con el nombre: `Robledo_Pedroviejo_Nacho_Memoria.pdf`.
