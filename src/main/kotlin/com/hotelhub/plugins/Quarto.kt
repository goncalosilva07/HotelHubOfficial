class Quarto(var id: Int, var numero: Int,
             var tipoQuarto: String,
             var preco: Double,
             var capacidade: Int,
             var caracteristicas: List<CaracteristicaQuarto>,
             var estadoLimpeza: String,
             var estadoManutencao: String,
             var disponibilidade: Boolean) {
}