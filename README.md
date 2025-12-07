# **MedReminder**

Aplicación móvil para gestionar y recordar medicamentos.

MedReminder es una aplicación Android diseñada para ayudar a los usuarios a registrar medicamentos, programar horarios, recibir notificaciones y mantener un historial organizado.  
Los recordatorios funcionan incluso cuando la aplicación está cerrada, gracias al uso de almacenamiento persistente y alarmas del sistema.

---
## Integrantes:
- Condorios Yllapuma Jorge Enrique
- Umasi Coaguila Geraldine Marjorie

---
# **1. Tecnologías utilizadas**

- **Kotlin**
- **Jetpack Compose**
- **Room (SQLite)**
- **ViewModel y StateFlow**
- **AlarmManager**
- **API de notificaciones**
- **Material Design 3**

---

# **2. Almacenamiento y persistencia de datos**

La aplicación utiliza **Room**, una capa de abstracción de SQLite.

## **2.1 ¿Por qué se usa Room?**

- Mantiene la información aun después de cerrar la app.  
- Ofrece acceso seguro y estructurado a los datos.  
- Facilita la reactividad mediante `Flow<List<Medicine>>`.  
- Garantiza que los medicamentos, horarios y días seleccionados se conserven de forma persistente.  

## **2.2 ¿Cómo recuerda la aplicación los datos después de cerrarse?**

Una app recuerda los datos gracias al **almacenamiento persistente**, que incluye:

- Bases de datos internas (Room/SQLite)  
- Preferencias (SharedPreferences o DataStore)  
- Archivos internos  

En este proyecto, la información de medicamentos se guarda en una base de datos SQLite manejada mediante Room, lo que permite que **persista incluso tras cerrar o reiniciar la aplicación**.

---

# **3. Funcionamiento de los recordatorios**

Al guardar un medicamento, la aplicación programa alarmas mediante `AlarmManager`.

**Proceso:**

1. El usuario registra un medicamento.  
2. La app programa una alarma para cada día seleccionado.  
3. Cuando llega el horario, Android ejecuta `AlarmReceiver`.  
4. El receptor genera una notificación informando el medicamento a tomar.  

Las alarmas funcionan incluso si:

- La app está cerrada  
- La app fue expulsada de memoria  
- El dispositivo está bloqueado  

Esto es posible gracias a `setRepeating` con `RTC_WAKEUP`.

---

# **4. Notificaciones**

La aplicación define un canal de notificaciones (Android 8+):

- **ID:** `med_reminder_channel`
- **Nombre:** “Recordatorios de Medicinas”
- **Importancia:** Alta  

En Android 13+ se solicita el permiso `POST_NOTIFICATIONS`.

---

# **5. Comportamiento tras desinstalar la aplicación**

Cuando la app se desinstala:

- La base de datos Room se elimina.  
- Los archivos internos desaparecen.  
- Las preferencias se borran.  
- Las alarmas programadas se cancelan automáticamente.  

Esto responde la pregunta:  
**¿Qué ocurre con los archivos generados por la app tras desinstalarla?**  
→ *Android elimina todo lo perteneciente a la aplicación.*

---

# **6. Tipos de almacenamiento en Android**

Android maneja varios tipos principales:

## **6.1 Almacenamiento interno**
- Privado para la app  
- Se borra al desinstalarla  
- Más seguro  
- Aquí se guarda la base de datos Room  

## **6.2 Almacenamiento externo**
- Accesible al usuario  
- Puede permanecer tras la desinstalación  
- Requiere permisos  

## **6.3 Preferencias (SharedPreferences / DataStore)**
- Datos clave–valor  
- Ideal para configuraciones del usuario  

## **6.4 Bases de datos (SQLite / Room)**
- Información estructurada y persistente  
- Ideal para listas, horarios e información compleja  

MedReminder utiliza este tipo de almacenamiento.

---

# **7. Almacenamiento en iOS (Swift)**

Equivalentes a los sistemas de Android:

- **Core Data:** similar a Room  
- **UserDefaults:** equivalente a SharedPreferences  
- **Property Lists (plist):** archivos clave–valor  
- **FileManager:** manejo manual de archivos  

Ambos sistemas buscan almacenar datos privados y persistentes.

---

# **8. Modelo de datos**

```kotlin
@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dose: String,
    val hour: Int,
    val minute: Int,
    val days: List<String>,
    val reminderEnabled: Boolean,
    val quantityLeft: Int?
)
