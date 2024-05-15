package com.hotelhub.plugins

import Permissao
import kotlinx.serialization.Serializable

@Serializable
data class DTO_Cliente(
    var id: String,
    var userName: String,
    var password: String,
    var nome: String?,
    var apelido: String?,
    var email: String?,
    var telefone: String?,
    var permissoes: MutableList<Permissao>,
    var isClient: Boolean = true
) {


}