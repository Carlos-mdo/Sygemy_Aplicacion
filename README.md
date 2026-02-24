Requerimientos funcionales               //Requerimientos que debe cumplir el proyecto para que pueda considerarse terminado.

1 <Iniciar sesión>
1.1	Introducción:
El sistema permitirá el ingreso de usuarios mediante un inicio de sesión, validando la contraseña y usuario

1.2	Inputs:
•	Usuario
•	Contraseña

1.3	Processing:
•	Verificar usuario y contraseña.
•	Identificar el tipo de usuario.

1.4	Outputs:
•	Acceso al sistema según el rol.

1.5	Error Handling:
•	En caso de que el usuario o la contraseña sean incorrectos, se informa por pantalla.
2 <Consultar información del alumno>
1.6	Introducción:
El alumno podrá consultar su información académica correspondiente a su cursada

1.7	Inputs:
•	Solicitud de consulta del alumno

1.8	Processing:
•	Verificar las notas y correcciones del alumno
•	Verificar el material educativo disponible
•	Verificar las fechas de exames
•	Verificar de inasistencias.

1.9	Outputs:
•	Listado de notas y correcciones
•	Material educativo
•	Listado fechas de exámenes
•	Listado de inasistencias.

1.10	Error Handling:
•	Si no hay información cargada, se mostrará un mensaje por pantalla.
3<Consultar información del docente>
1.11	Introducción:
El docente podrá gestionar las notas, correcciones, inasistencias, fechas y material de los alumnos que pertenecen a los cursos a los que el docente fue asignado.

1.12	Inputs:
•	Selección de solicitud del docente.

1.13	Processing:
•	Verificar que el usuario que ingresa es docente
•	Validar los cursos asignados al docente
•	Verificar la información correspondiente al docente dependiendo de sus cursos
•	Registrar las acciones realizadas por el docente

1.14	Outputs:
•	Visualización de Notas
•	Visualización de Correcciones
•	Listado de Inasistencias
•	Listado de Fechas de exámenes
•	Confirmacion de las acciones realizadas por el docente

1.15	Error Handling:
•	Si no hay información cargada, se mostrará un mensaje por pantalla.
•	Mensaje de error en caso de fallar al modificar la información

3<Consultar información de la administración>
1.16	Introducción:
La administración va a poder gestionar todo lo relacionado a lo económico del sistema e informacion personal de los alumnos y docentes, como tambien permita administrar los roles de cada usuario y grados asignados. 

1.17	Inputs:
•	Selección de solicitud del usuario.

1.18	Processing:
•	Verificar que el usuario forma parte de la administracion
•	Verificar la informacion de los docentes y alumnos.
•	Registrar las acciones del usuario administrador.

1.19	Outputs:
•	Listado de alumnos y su informacion
•	Listado de docentes y su informacion
•	Listado de pagos realizados por los tutores.
•	Mensaje de confirmacion de las acciones realizadas
1.20	Error Handling:
•	Si no hay informacion cargada, se mostrara un mensaje por pantalla
•	Mensaje de error en caso de fallar al modificar la informacion.

Requerimientos No-Funcionales
1 Desempeño:
•	La aplicación deberá ofrecer un rendimiento estable y adecuado en el ABML de los datos, garantizando un servicio de funcionamiento fluido para los dispositivos móviles de android.
2 Confiabilidad:
•	El sistema ofrecerá la correcta persistencia de los datos al momento de acceder a la información de la base de datos usando SQLite.
3 Disponibilidad:
•	La aplicación estará disponible en todo momento sin requerir del uso de conexión a internet para los dispositivos en los que se encuentre instalada. 
4 Seguridad:
•	La información esta filtrada para ser accedida solo por los usuarios con el rol necesario, asegurando que la información más sensible solo esté disponible para los roles más altos del sistema garantizando un uso efectivo de la misma.
5 Mantenibilidad:
•	La aplicación va a ser desarrollada usando AndroidStudio, con el uso de buenas  prácticas y organización será posible modificar la aplicación sin pérdida de datos para correcciones o mejoras a largo y corto plazo. 
6 Portabilidad:
•	La aplicación será compatible con todo dispositivo Android, permitiendo instalarse y ejecutar con todo dispositivo que corresponda.

Restricciones
•	La aplicación será desarrollada usando Android Studio.
•	Como sistema gestor de base de datos se utilizará SQLite.
•	La aplicacion esta destinada a ser exclusiva de dispositivos Android.
•	El funcionamiento de la aplicación no requiere de conexion a internet.
•	El alcance del sistema se limitará a la gestión administrativa definida previamente en el documento.
