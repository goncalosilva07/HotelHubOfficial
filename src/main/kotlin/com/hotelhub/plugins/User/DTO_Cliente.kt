package com.hotelhub.plugins.User

import com.hotelhub.plugins.Permission.Permissao
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
    var isClient: Boolean
) {


}