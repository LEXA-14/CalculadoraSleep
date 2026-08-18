DeepSlepp

Es una aplicación Android nativa que calcula las mejores horas para dormir o despertar según los ciclos de sueño de 90 minutos.
Resuelve un problema simple pero real: despertarse a mitad de un ciclo REM te deja agotado, así que la app te dice exactamente a qué hora acostarte (o levantarte) para despertar descansado, 
y además te deja programar alarmas, registrar tu historial de sueño y calificar cómo dormiste cada noche.

 Descripción General

DeepSleep es una app de Android construida con Jetpack Compose y Clean Architecture. A partir de una hora objetivo (para dormir o para despertar), calcula tres opciones de horario basadas en ciclos completos de sueño de 90 minutos, permite programar alarmas nativas para cumplirlas, guarda un historial de sesiones de sueño en una base de datos local y sincroniza la identidad del usuario mediante Firebase Authentication.

 Características
Calculadora de ciclos de sueño: ingresa la hora a la que quieres despertar o la hora a la que planeas dormir, y la app sugiere 3 horarios (4, 5 y 6 ciclos), marcando el de 5 ciclos como el "ideal".
Alarmas nativas: crea, edita, activa/desactiva y elimina alarmas que disparan una notificación de pantalla completa con sonido, incluso con la app cerrada o el teléfono reiniciado.
Registro de sesiones de sueño: cada sesión (hora de dormir, hora de despertar, ciclos completados) se guarda automáticamente en el historial.
Calificación de calidad del sueño: al detener la alarma de despertar, se abre una pantalla para calificar de 1 a 5 cómo dormiste.
Historial y estadísticas: consulta tus noches pasadas y estadísticas agregadas (duración promedio, ciclos promedio, calidad promedio) de los últimos N días.
Autenticación de usuario: registro e inicio de sesión con correo/contraseña, inicio de sesión con Google (Credential Manager) y recuperación de contraseña, todo respaldado por Firebase Auth.
Modo oscuro: tema claro/oscuro persistente controlado desde la app.

 Uso
 
1-Regístrate o inicia sesión con correo/contraseña o con tu cuenta de Google.

2-En la pantalla principal (Calculadora), elige si quieres calcular tu hora de dormir (dado un despertar objetivo) o tu hora de despertar (dado que te acuestas ahora).

3-Ingresa la hora objetivo y la app te mostrará 3 opciones (4, 5 y 6 ciclos de sueño), destacando la de 5 ciclos como recomendada.

4-Si lo deseas, ve a la pestaña Alarmas y crea una alarma para la hora elegida; puedes editarla, activarla/desactivarla o borrarla en cualquier momento.

5-Cuando la alarma suena, aparece una notificación de pantalla completa con opción de Detener; al detenerla se abre la pantalla de Calidad del sueño para calificar tu descanso de 1 a 5.

6-Cada sesión registrada aparece en la pestaña Historial, junto con estadísticas agregadas de los últimos días.
