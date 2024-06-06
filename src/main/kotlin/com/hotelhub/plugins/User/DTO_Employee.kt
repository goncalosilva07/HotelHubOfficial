package com.hotelhub.plugins.User

import Horario
import Permissao
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class DTO_Employee(
    var id: String?,
    var userName: String,
    var password: String,
    var nome: String?,
    var apelido: String?,
    var email: String?,
    var telefone: String?,
    var permissoes: MutableList<Permissao>?,
    var isClient: Boolean,
    var nif: String,
    var dataDeNascimento: LocalDate,
    var genero: String,
    var salario: Double,
    var cargo: String,
    var horario: MutableList<Horario>?
)