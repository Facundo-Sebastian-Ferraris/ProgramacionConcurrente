# Programación Concurrente 🖥️
## Trabajo Práctico Obligatorio Final 🎿

Se desea simular la actividad diaria del Complejo Invernal **“Caída Rápida”** ❄️. Dicho complejo ha implementado un nuevo sistema de pases para el ingreso de los esquiadores. A continuación, se detalla el funcionamiento del sistema:

### Medios de Elevación 🚡
El complejo cuenta con **4 medios de elevación**, y cada uno tiene un grupo de **n molinetes** (donde *n* puede variar entre 1 y 4) que dan acceso al medio a los esquiadores. Por ejemplo: el medio de elevación **Silla Triple** posee **3 molinetes** que permiten el acceso a **3 esquiadores** por vez.

- 📍 **Molinetes**: Cada molinete tiene un **lector de tarjetas** que verifica los pases de los esquiadores y registra su uso.
- 📊 **Contador**: La suma del uso de todos los molinetes de un medio determina la cantidad de veces que fue utilizado.
- 🕒 **Horario**: Los medios de elevación están habilitados desde las **10:00** hasta las **17:00**, durante las cuales los esquiadores tienen acceso ilimitado.
- 🎿 **Esquiadores**: Cada esquiador experimentado esquía por un tiempo, utiliza los medios de elevación, descansa, visita la confitería y continúa esquiando.

### Clases Grupales 🏂
El complejo ofrece **clases grupales diarias** de esquí o snowboard, dictadas por un equipo de **5 instructores** 🧑‍🏫.

- **Condiciones**:
  - Una clase comienza cuando se forma un grupo de **4 alumnos** y hay **1 instructor** disponible.
  - Los instructores esperan en la **Cabina de Instructores** hasta que se forme un grupo.
  - ⏳ Si no se logra formar un grupo en un tiempo prudencial, los esquiadores desisten y se les devuelve el dinero.

### Confitería 🍽️
El complejo cuenta con una **confitería** con capacidad para **100 personas**, que dispone de:

- **2 mostradores** para servirse comidas rápidas 🍔.
- **1 mostrador** para postres 🍰.
- **Circuito**:
  1. La persona ingresa a la confitería y paga en una **única caja** el menú deseado (que puede incluir o no postre).
  2. Retira la comida en uno de los mostradores de comidas rápidas.
  3. Si el menú incluye postre, lo retira en el mostrador correspondiente.
- 🪑 **Disponibilidad**: Si una persona llega a la caja, es porque hay mesas disponibles.

### Requerimientos Técnicos 🔧
El sistema debe resolverse utilizando los mecanismos de sincronización vistos en la materia:

- **Obligatorios**: Semáforos, monitores y locks 🔒.
- **Al menos uno de los siguientes**: CyclicBarrier, CountDownLatch, Exchanger, implementaciones de BlockingQueue, etc.

### Objetivo 📈
Al final del día, se desea conocer **cuántos esquiadores utilizaron cada medio de elevación**.
