package com.hotelhub.plugins

import Cliente
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

fun Application.configureRouting() {
    routing {

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }

        get("/") {
            call.respondFile(File("pages/login.html"))
        }

        get("/register") {
            call.respondFile(File("pages/register.html"))
        }

        post("/registerFun") {
            @Serializable
            data class RegisterInfo(val userName: String, val password: String, val repeatPassword: String)

            val jsonData = call.receive<String>()
            println("JSON Received: $jsonData")
            val registerData = Json.decodeFromString<RegisterInfo>(jsonData)
            println(registerData)

            val cliente: Cliente = Cliente("", registerData.userName, registerData.password, null, null, null, null)
            val responseCreateClient: Pair<Boolean, String> = cliente.register()

            call.respondText("Conta criada com sucesso!", status = HttpStatusCode.Created)
        }

        post("/login") {
            @Serializable
            data class LoginInfo(val userName: String, val password: String)

            val jsonData = call.receive<String>()
            println("JSON Received: $jsonData")
            val loginData = Json.decodeFromString<LoginInfo>(jsonData)
            println(loginData)

            val responseData = LoginPage().login(loginData.userName, loginData.password)
            if (responseData.first){

                val receiveData: LoginData = LoginData((responseData.second as LoginData).id, (responseData.second as LoginData).userName, (responseData.second as LoginData).enc)

                val json = Json.encodeToString(receiveData)
                call.respondText(json, ContentType.Application.Json)
            }else{
                call.respondText((responseData.second as String), status = HttpStatusCode.Unauthorized)
            }
        }

        get("/contentHub") {
            call.respondFile(File("pages/contentHub.html"))
        }

    }
}
