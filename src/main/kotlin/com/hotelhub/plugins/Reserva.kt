import java.util.Date

class Reserva(var id: Int,
              var numero: Int,
              var cliente: Cliente,
              var quarto: Quarto,
              var checkIn: Date,
              var checkOut: Date,
              var preco: Double,
              var estado: String) {

}