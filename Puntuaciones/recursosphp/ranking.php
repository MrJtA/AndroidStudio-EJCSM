<?php
header('Content-Type: application/json');

$servidor = "localhost";
$usuario = "raquel";
$password = "gEEDr(q5!vX.r-ar";
$base_datos = "juegobbdd";

// Conexión
$conexion = new mysqli($servidor, $usuario, $password, $base_datos);

// Verificar conexión
if ($conexion->connect_error) {
	die("Error de conexión: " . $conexion->connect_error);
}


$sql ="SELECT p.puntos as score, p.id_jugador as id, j.alias as username FROM partida p, jugador j WHERE j.id=id_jugador order by puntos desc LIMIT 10";

$resultado = mysqli_query($conexion, $sql);
$scores = [];

if ($resultado && mysqli_num_rows($resultado) > 0) {
	// Procesar cada fila
	while ($fila = mysqli_fetch_assoc($resultado)) {
		$scores[] = ["score" => $fila["score"] , "username" => $fila["username"]];
	}
	// Liberar el conjunto de resultados
	mysqli_free_result($resultado);
} else {
echo "No se encontraron resultados.";
}


$conexion->close();

// Devolver el JSON
echo json_encode($scores); 