import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import java.util.*
@Serializable
class Horario (
    var id: String,
    var diaSemana: Int,
    var inicioTurno1: LocalTime,
    var fimTurno1: LocalTime,
    var inicioTurno2: LocalTime,
    var fimTurno2: LocalTime)
{
}