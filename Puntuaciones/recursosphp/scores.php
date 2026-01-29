<?php
header('Content-Type: application/json');

// Simulación de datos de puntuaciones de usuarios
$scores = [
    ["username" => "user1", "score" => 1500],
    ["username" => "user2", "score" => 2000],
    ["username" => "user3", "score" => 1800],
	 ["username" => "alan12", "score" => 2010],
    ["username" => "rick20", "score" => 1600],
	 ["username" => "martha12", "score" => 2100],
    ["username" => "lisa543", "score" => 1860],
	 ["username" => "james", "score" => 2600],
    ["username" => "karla98", "score" => 1600],
    ["username" => "siham765", "score" => 6200]
];

// Devolver el JSON
echo json_encode($scores);
