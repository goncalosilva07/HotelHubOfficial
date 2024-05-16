package com.hotelhub.plugins

import Cliente
import Permissao
import Pessoa
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.security.Permissions

fun Application.configureRouting() {
    routing {

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }

        /* ************************** DATA CLASS ************************** */
        @Serializable
        data class RegisterInfo(val userName: String, val password: String, val repeatPassword: String)

        @Serializable
        data class LoginInfo(val userName: String, val password: String)

        /* ************************** PAGES ************************** */
        get("/") {
            call.respondFile(File("pages/login.html"))
        }

        get("/register") {
            call.respondFile(File("pages/register.html"))
        }

        get("/contentHub") {
            call.respondFile(File("pages/contentHub.html"))
        }

        get("/dashboard") {
            call.respondFile(File("pages/dashboard.html"))
        }

        get("/reserve") {
            call.respondFile(File("pages/reserve.html"))
        }

        /* ************************** METHODS ************************** */
        post("/registerFun") {

            val jsonData = call.receive<String>()
            println("JSON Received: $jsonData")
            val registerData = Json.decodeFromString<RegisterInfo>(jsonData)
            println(registerData)

            val cliente: Cliente = Cliente("", registerData.userName, registerData.password, null, null, null, null, mutableListOf())
            val responseCreateClient: Pair<Boolean, String> = cliente.register()

            if (responseCreateClient.first){
                call.respondText("Conta criada com sucesso!", status = HttpStatusCode.Created)
            }else{
                call.respondText(responseCreateClient.second, status = HttpStatusCode.NotAcceptable)
            }
        }

        post("/login") {

            val jsonData = call.receive<String>()
            println("JSON Received: $jsonData")
            val loginData = Json.decodeFromString<LoginInfo>(jsonData)
            println(loginData)

            //val responseData = LoginPage().login(loginData.userName, loginData.password)
            val responseData = Pessoa.login(loginData.userName, loginData.password)
            if (responseData.first){

                val receiveData: DTO_LoginData = DTO_LoginData((responseData.second as DTO_LoginData).id, (responseData.second as DTO_LoginData).userName, (responseData.second as DTO_LoginData).enc)

                val json = Json.encodeToString(receiveData)
                call.respondText(json, ContentType.Application.Json)
            }else{
                call.respondText((responseData.second as String), status = HttpStatusCode.Unauthorized)
            }
        }

        post("/getUserInitialData") {

            val jsonData = call.receive<String>()
            val loginData = Json.decodeFromString<DTO_LoginData>(jsonData)

            val responseData = Pessoa.getUserData(loginData.id, loginData.userName, loginData.enc)

            if (responseData.first){

                val cliente: DTO_Cliente = DTO_Cliente(
                    (responseData.second as Cliente).id,
                    (responseData.second as Cliente).userName,
                    (responseData.second as Cliente).password,
                    (responseData.second as Cliente).nome,
                    (responseData.second as Cliente).apelido,
                    (responseData.second as Cliente).email,
                    (responseData.second as Cliente).telefone,
                    (responseData.second as Cliente).permissoes
                )

                val permissionsList: MutableList<Permissao> = Permissao.getAllPermissions()

                val userInfo: DTO_getUserInitalData = DTO_getUserInitalData(cliente, permissionsList)

                val json = Json.encodeToString(userInfo)
                call.respondText(json, ContentType.Application.Json)

            }else{
                call.respondText((responseData.second as String), status = HttpStatusCode.NotFound)
            }
        }

        post("/getReserveInitialData") {

            val jsonData = call.receive<String>()
            val loginData = Json.decodeFromString<DTO_LoginData>(jsonData)

        }

    }
}
