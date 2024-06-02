package com.hotelhub.plugins
import com.hotelhub.plugins.RoomService.RoomService.Companion.getRoomServiceInitialData


import CaracteristicaQuarto
import Permissao
import Quarto.Companion.getAvailableRooms
import com.hotelhub.plugins.Reserve.Reserva.Companion.createReserve
import com.hotelhub.plugins.Reserve.DTO_DataSerchRoomsForReserve
import com.hotelhub.plugins.Reserve.DTO_ReserveData
import com.hotelhub.plugins.RoomService.DTO_CreateRoomService
import com.hotelhub.plugins.RoomService.DTO_GetRoomServiceInitialData
import com.hotelhub.plugins.RoomService.RoomService.Companion.createRoomService
import com.hotelhub.plugins.User.*
import com.hotelhub.plugins.User.Cliente.Companion.getClientDashBoardInitialData
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

        /* ************************** DATA CLASS ************************** */
        @Serializable
        data class RegisterInfo(val userName: String, val password: String, val repeatPassword: String)

        @Serializable
        data class LoginInfo(val userName: String, val password: String)

        @Serializable
        data class DTO_ReserveInitialData(val roomFeatures: MutableList<CaracteristicaQuarto>)

        //@Serializable
        //data class DTO_DataSerchRoomsForReserve(val startDate: String, val endDate: String, val numberOfPeople: Int, val additionalServices: MutableList<CaracteristicaQuarto>)

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

        get("/roomsToReserve") {
            call.respondFile(File("pages/roomsToReserve.html"))
        }

        get("/reserve") {
            call.respondFile(File("pages/reserve.html"))
        }

        get("/roomService") {
            call.respondFile(File("pages/roomService.html"))
        }

        /* ************************** METHODS ************************** */
        post("/registerFun") {

            val jsonData = call.receive<String>()
            println("JSON Received: $jsonData")
            val registerData = Json.decodeFromString<RegisterInfo>(jsonData)
            println(registerData)

            val cliente: Cliente = Cliente("", registerData.userName, registerData.password, null, null, null, null, mutableListOf(), true)
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

                if ((responseData.second as Pessoa).isClient) {

                    val client: DTO_Cliente = DTO_Cliente(
                        (responseData.second as Cliente).id,
                        (responseData.second as Cliente).userName,
                        (responseData.second as Cliente).password,
                        (responseData.second as Cliente).nome,
                        (responseData.second as Cliente).apelido,
                        (responseData.second as Cliente).email,
                        (responseData.second as Cliente).telefone,
                        (responseData.second as Cliente).permissoes,
                        (responseData.second as Cliente).isClient
                    )

                    client.nome = if (client.nome == "null") null else client.nome
                    client.apelido = if (client.apelido == "null") null else client.apelido
                    client.email = if (client.email == "null") null else client.email
                    client.telefone = if (client.telefone == "null") null else client.telefone

                    val permissionsList: MutableList<Permissao> = Permissao.getAllPermissions()

                    val userInfo: DTO_GetClientInitalData = DTO_GetClientInitalData(client, permissionsList)

                    val json = Json.encodeToString(userInfo)
                    call.respondText(json, ContentType.Application.Json)
                }else{

                    val employee: DTO_Employee = DTO_Employee(
                        (responseData.second as Employee).id,
                        (responseData.second as Employee).userName,
                        (responseData.second as Employee).password,
                        (responseData.second as Employee).nome,
                        (responseData.second as Employee).apelido,
                        (responseData.second as Employee).email,
                        (responseData.second as Employee).telefone,
                        (responseData.second as Employee).permissoes,
                        (responseData.second as Employee).isClient,
                        (responseData.second as Employee).nif,
                        (responseData.second as Employee).dataDeNascimento,
                        (responseData.second as Employee).genero,
                        (responseData.second as Employee).salario,
                        (responseData.second as Employee).cargo,
                        (responseData.second as Employee).horario
                    )

                    employee.nome = if (employee.nome == "null") null else employee.nome
                    employee.apelido = if (employee.apelido == "null") null else employee.apelido
                    employee.email = if (employee.email == "null") null else employee.email
                    employee.telefone = if (employee.telefone == "null") null else employee.telefone

                    val permissionsList: MutableList<Permissao> = Permissao.getAllPermissions()

                    val userInfo: DTO_GetEmployeeInitialData = DTO_GetEmployeeInitialData(employee, permissionsList)

                    val json = Json.encodeToString(userInfo)
                    call.respondText(json, ContentType.Application.Json)
                }

            }else{
                call.respondText((responseData.second as String), status = HttpStatusCode.NotFound)
            }
        }

        post("/getRoomsToReserveInitialData") {
            try {
                val roomFeatures = CaracteristicaQuarto.getRoomFeatures()
                val responseData = DTO_ReserveInitialData(roomFeatures)

                val json = Json.encodeToString(responseData)
                call.respondText(json, ContentType.Application.Json)

            }catch (exception: Exception){
                call.respondText(exception.toString(), status = HttpStatusCode.InternalServerError)
            }
        }

        post("/getRoomsForReserve") {
            try {
                val jsonData = call.receive<String>()
                val data = Json.decodeFromString<DTO_DataSerchRoomsForReserve>(jsonData)
                val responseData = getAvailableRooms(data)

                val json = Json.encodeToString(responseData)
                call.respondText(json, ContentType.Application.Json)
            }catch (exception: Exception){
                call.respondText(exception.toString(), status = HttpStatusCode.InternalServerError)
            }
        }

        post("/createUserReserve") {
            try {
                val jsonData = call.receive<String>()
                val data = Json.decodeFromString<DTO_ReserveData>(jsonData)
                val responseData = createReserve(data)

                if (responseData.first){
                    call.respondText("Reserva criada com sucesso!", status = HttpStatusCode.Created)
                }else{
                    call.respondText(responseData.second, status = HttpStatusCode.NotAcceptable)
                }

            }catch (exception: Exception){
                call.respondText(exception.toString(), status = HttpStatusCode.InternalServerError)
            }
        }

        post("/getRoomServiceInitialData") {
            try {
                val jsonData = call.receive<String>()
                val idUser: String = jsonData
                val responseData = getRoomServiceInitialData(idUser)

                if (responseData.first){

                    val roomServiceInitialData: DTO_GetRoomServiceInitialData = DTO_GetRoomServiceInitialData(
                        (responseData.second as DTO_GetRoomServiceInitialData).idReserve,
                        (responseData.second as DTO_GetRoomServiceInitialData).menu,
                    )

                    val json = Json.encodeToString(roomServiceInitialData)
                    call.respondText(json, ContentType.Application.Json)
                }else{
                    call.respondText(responseData.second.toString(), status = HttpStatusCode.NotFound)
                }
            }catch (exception: Exception){
                call.respondText(exception.toString(), status = HttpStatusCode.InternalServerError)
            }
        }

        post("/createRoomServiceInitialData") {
            try {
                val jsonData = call.receive<String>()
                val data = Json.decodeFromString<DTO_CreateRoomService>(jsonData)
                val responseData = createRoomService(data)

                if (responseData.first){
                    call.respondText(responseData.second.toString(), status = HttpStatusCode.Created)
                }else{
                    call.respondText(responseData.second.toString(), status = HttpStatusCode.InternalServerError)
                }
            }catch (exception: Exception){
                call.respondText(exception.toString(), status = HttpStatusCode.InternalServerError)
            }
        }

        post("/getClientDashBoardInitialData") {
            try {
                val jsonData = call.receive<String>()
                val idUser: String = jsonData
                val responseData = getClientDashBoardInitialData(idUser)

                val json = Json.encodeToString(responseData)
                call.respondText(json, ContentType.Application.Json)
            }catch (exception: Exception){
                call.respondText(exception.toString(), status = HttpStatusCode.InternalServerError)
            }
        }
    }
}
