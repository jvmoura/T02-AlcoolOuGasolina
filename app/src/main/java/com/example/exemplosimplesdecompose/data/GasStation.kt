package com.example.exemplosimplesdecompose.data

data class GasStation(
    var name: String,
    val precoAlcool: Double,
    val precoGasolina: Double,
    val coord: Coordinates,
    val dataCadastro: Long
) {
    constructor(nome: String, coord: Coordinates) : this(
        nome,
        0.0,
        0.0,
        coord,
        System.currentTimeMillis()
    )
    constructor(nome: String) : this(
        nome,
        0.0,
        0.0,
        Coordinates(41.40338, 2.17403),
        System.currentTimeMillis()
    )
}