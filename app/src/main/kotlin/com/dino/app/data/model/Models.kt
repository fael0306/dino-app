package com.dino.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Dinosaur(
    val Nome: String, val Periodo: String, val Dieta: String,
    val Comprimento: Double, val Altura: Double, val Peso: Double,
    val Postura: String, val Curiosidade: String = ""
)

@Serializable
data class QuizQuestion(
    val pergunta: String, val opcoes: List<String>, val resposta: Int, val explicacao: String = ""
)

@Serializable
data class Periodo(
    val nome: String, val inicio: Int, val fim: Int, val cor: Long, val eventos: List<String>
)

@Serializable
data class ClimaData(
    val temperatura: Double, val co2: Int, val vegetacao: String, val nivelMar: String, val descricao: String
)

@Serializable
data class Icnofossil(
    val nome: String, val dedos: Int, val garras: Boolean,
    val tamanho: String? = null, val forma: String? = null, val proporcao: String? = null
)

@Serializable
data class Coordenada(val lat: Double, val lon: Double)

@Serializable
data class BatalhaPergunta(
    val pergunta: String, val opcoes: List<String>, val resposta: Int, val explicacao: String
)