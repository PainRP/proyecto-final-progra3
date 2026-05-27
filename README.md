# Documentación del Proyecto Final - Programación 3
## Sistema de Gestión de Estructuras Jerárquicas con Árboles

## 🔗 Enlaces Importantes
* **Tablero Trello:** [Reemplazar con link al tablero]
* **Repositorio GitHub:** [Reemplazar con link al repo]
* **Documentación OpenAPI / Swagger:** [Reemplazar con la URL local de Swagger, ej. `http://localhost:8080/swagger-ui.html`]

---

## 🛠 Requisitos Previos e Instrucciones de Ejecución (Talvez se necesite hacer cambios en esta sección)
Para ejecutar este proyecto de manera local, se requiere lo siguiente:
* **JDK:** Versión 17 o superior.
* **Gestor de dependencias:** Maven.
* **Bases de datos:** Instancias activas de PostgreSQL y MongoDB (se recomienda usar Docker mediante el archivo `docker-compose.yml` provisto en el proyecto).

### Ejecución con cambio dinámico de configuraciones

Para levantar el proyecto completo y alternar entre las distintas combinaciones de motores y persistencias, sigue estos pasos:

#### 1. Levantar la infraestructura base (Solo la primera vez)
Antes de iniciar el backend, es necesario levantar los contenedores de Docker que alojan las bases de datos (**PostgreSQL** y **MongoDB**). Ejecuta el siguiente comando en la raíz del proyecto:

```bash
docker compose up -d
```
*> **Nota:** Si usas una versión antigua de Docker, el comando puede ser `docker-compose up -d`.*

> **Solución a errores de internet/red:** Si Docker lanza un error al descargar las imágenes por problemas de conexión, se debe cambiar la configuración de Docker para asignarle un DNS público (como el de Google). Esto se hace agregando `"dns": ["8.8.8.8", "8.8.4.4"]` en el archivo de configuración del Daemon (`daemon.json`) o desde el menú de configuración de Docker Desktop (Settings > Docker Engine).

---

#### 2. Configurar el motor y almacenamiento
La flexibilidad del sistema permite cambiar la estrategia del árbol y el tipo de persistencia editando el archivo `application.properties` del backend sin necesidad de recompilar el código.

Abre `src/main/resources/application.properties` y ajusta las siguientes propiedades según la combinación que desees probar:

```properties
# ===================================================================
# CONFIGURACIÓN DINÁMICA DEL MOTOR Y PERSISTENCIA
# ===================================================================

# Estrategias de algoritmo disponibles:
# - custom      (Estructura propia, sin librerías externas)
# - collections (Estructura basada en el JDK Collections API)
app.tree-strategy=custom

# Almacenamientos disponibles:
# - memory      (Estructura volátil en la JVM)
# - postgres    (Base de datos relacional)
# - mongo       (Base de datos documental)
app.storage=memory

# Perfil activo de Spring (debe coincidir con el almacenamiento elegido)
spring.profiles.active=memory
```

---

#### 3. Ejecutar el Backend (Spring Boot con Maven)
Una vez guardada la configuración, inicia el servidor de desarrollo de Spring Boot desde la terminal de tu IDE o usando el Wrapper de Maven en la raíz del backend:

```bash
./mvnw spring-boot:run
```

---

#### 4. Ejecutar el Frontend
Para levantar la interfaz gráfica y visualizar el comportamiento del árbol, abre una nueva terminal y dirígete a la carpeta del frontend:

* **Instalación de dependencias** (solo es necesario hacerlo la primera vez o si cambian los paquetes):
    ```bash
    cd frontend
    npm install
    ```
* **Iniciar el entorno de desarrollo:**
    ```bash
    npm run dev
    ```

Una vez encendido, abre el navegador en la ruta local que te indique la consola (usualmente `http://localhost:5173`) para interactuar con el sistema de gestión jerárquica.


## ⚖️ Validación de Equivalencia de Motores
Para asegurar que tanto la estrategia `custom` como `collections` procesen la jerarquía del árbol de forma idéntica, se ha implementado un conjunto de pruebas funcionales automatizadas (`DualEngineStrategyFunctionalTest.java`).

Estas pruebas validan de forma cruzada que:
1. Ambas estrategias del motor generen los mismos recorridos DFS y BFS para un árbol idéntico.
2. El cálculo de altura y profundidad arroje exactamente el mismo número.
3. La validación de ciclos (grafos malformados) se detecte correctamente en ambas implementaciones.

---

## 🏢 Caso de Uso: Plan de Cuentas Contable (Caso #5)
El dominio elegido para la demostración y validación de este sistema es un **Plan de Cuentas Contable**. La aplicación permite estructurar, gestionar y consultar de forma jerárquica la información financiera de una organización, modelando la relación entre cuentas de distintos niveles (activos, pasivos, patrimonios, ingresos, costos y gastos).

### Ejemplo de la Estructura Jerárquica Soportada:
* **1** Activo (Nodo Raíz)
    * **1.1** Activo Corriente (Hijo de 1)
        * **1.1.1** Caja y Bancos (Hijo de 1.1)
            * **1.1.1.01** Caja General (Hijo de 1.1.1 / Cuenta de detalle)
            * **1.1.1.02** Banco Moneda Nacional (Hijo de 1.1.1 / Cuenta de detalle)
    * **1.2** Activo No Corriente (Hijo de 1)
* **2** Pasivo (Nodo Raíz)

### Estandarización del Modelo de Dominio:
Para representar fielmente este dominio, el modelo genérico de datos utiliza atributos específicos que permiten clasificar cada elemento contable:
* `id`: Identificador único del sistema.
* `code`: Código contable estructurado (ej. `"1.1.1.01"`).
* `name`: Nombre de la cuenta (ej. `"Caja General"`).
* `type`: Tipo o nivel de la cuenta (ej. `"Rubro"`, `"Cuenta Mayor"`, `"Subcuenta"`).
* `description`: Notas aclaratorias sobre la naturaleza de la cuenta.

---

## 👤 Rol: Integrante A (Persona A)

### Resumen de Responsabilidades Asignadas
* **Persistencia:** Memoria (Estructura volátil en JVM).
* **Motor de Algoritmos:** Estrategia **Custom** (Estructura de árbol propio, sin librerías externas del dominio de árboles).
* **Transversal:** Diseño de la interfaz del motor, contrato OpenAPI, creación del esqueleto multimódulo y **desarrollo del frontend visual (en colaboración con Persona C)**.

### Artefactos y Trabajo Realizado
* **Arquitectura Transversal y Configuración Inicial:** Se configuró el proyecto principal (`pom.xml` de la raíz) separando las responsabilidades en los módulos independientes `app` (aplicación web y persistencia) y `tree-engine` (lógica agnóstica de algoritmos). Se diseñó el contrato OpenAPI 3.0.3 en `app/src/main/resources/static/openapi.yaml`, definiendo estrictamente los 11 endpoints del backend. Además, se definieron la interfaz común `TreeAlgorithmStrategy.java` y el modelo de dominio `Node.java`.
* **Implementación del Motor de Algoritmos (Estrategia Custom):** Desarrollo completo de `CustomTreeStrategy.java` en `tree-engine`. Se resolvió toda la lógica jerárquica usando colecciones básicas de Java (`ArrayList`, `LinkedList`, `HashSet`) sin librerías externas para modelar la jerarquía de árboles, implementando búsquedas DFS por recursividad pura, BFS mediante colas iterativas, cálculo de métricas (altura, profundidad) y detección de ciclos (`hasCycle`).
* **Persistencia en Memoria (Volátil):** Implementación de `InMemoryTreeRepository.java` utilizando un `ConcurrentHashMap` para asegurar operaciones thread-safe en el almacenamiento local de la JVM, activo mediante la propiedad `app.storage=memory`.
* **Implementación Visual (Frontend - Trabajo Conjunto con Persona C):** Construcción de la estructura base en HTML (`index.html`) y desarrollo del cliente JavaScript para el consumo asíncrono vía `fetch` del controlador estandarizado del backend (`TreeController.java`), permitiendo la comunicación fluida y la renderización dinámica del árbol jerárquico en el DOM.

---

## 👤 Rol: Integrante B (Persona B)

### Resumen de Responsabilidades Asignadas
* **Persistencia:** Base de Datos Relacional (PostgreSQL).
* **Motor de Algoritmos:** Estrategia **Collections** (Implementación con clases nativas como `TreeMap`, `TreeSet`, etc.).
* **Transversal:** Modelo Entidad-Relación, scripts SQL iniciales y carga de datos de prueba.

### Artefactos y Trabajo Realizado
* **Implementación del Motor de Algoritmos (Estrategia Collections):** Desarrollo de `CollectionsTreeStrategy.java` en el módulo `tree-engine`. Se usaron las API avanzadas del JDK Java Collections (`TreeMap`, `TreeSet`, colas y pilas de `ArrayDeque`) para estructurar y recorrer jerárquicamente la información del catálogo contable.
* **Persistencia (PostgreSQL):** Creación del mapeo de persistencia relacional con `NodeEntity.java` empleando una estructura autorreferenciada (`parent_id`), acoplado con `PostgresTreeRepository.java` y `JpaNodeRepository.java` para el control de operaciones sobre la base de datos relacional PostgreSQL.
* **Scripts SQL y Datos Semilla:** Diseño y configuración de los scripts de inicialización `schema.sql` y `data.sql` bajo la propiedad `app.storage=postgres`. Provee la inserción automática de datos iniciales para el plan de cuentas con más de 3 niveles de profundidad para validaciones end-to-end.

---

## 👤 Rol: Integrante C (Persona C)

### Resumen de Responsabilidades Asignadas
* **Persistencia:** Base de Datos NoSQL orientada a documentos (MongoDB).
* **Motor de Algoritmos:** Integración en Spring Boot de ambas estrategias mediante inyección condicional de dependencias.
* **Transversal:** Selectores de propiedades (`app.tree-strategy` y `app.storage`), **pruebas de integración** y **desarrollo del frontend visual (en colaboración con Persona A)**.

### Artefactos y Trabajo Realizado
* **Arquitectura Transversal e Inyección de Dependencias:** Desarrollo de `TreeStrategyConfig.java` para habilitar el uso de beans condicionales (`@ConditionalOnProperty`). Permite al orquestador `TreeOrchestratorService.java` inyectar dinámicamente la estrategia del motor (`custom` o `collections`) y la persistencia activa según los properties de configuración sin recompilar el proyecto.
* **Persistencia (MongoDB) y Pruebas de Integración:** Creación de la capa de almacenamiento documental NoSQL con `NodeDocument.java` y `RootDocument.java`. Implementación de la persistencia de lectura y escritura jerárquica a través de `MongoTreeRepository.java`. Adicionalmente, se desarrollaron pruebas de integración (ej. `MongoTreeRepositoryTest` en la rama `feature/C9-mongo-integration-tests`) para validar la correcta conexión y operaciones CRUD entre Spring Boot y MongoDB.
* **Implementación Visual (Frontend - Trabajo Conjunto con Persona A):** Diseño estético de la interfaz de usuario en `index.html` e integración de scripts para el manejo de formularios, estilos CSS interactivos del árbol contable, validación de formularios y la habilitación de controles gráficos en la UI que demuestran el intercambio en caliente de las propiedades `app.tree-strategy` y `app.storage`.
