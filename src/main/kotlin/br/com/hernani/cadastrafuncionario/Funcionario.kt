package br.com.hernani.cadastrafuncionario

import br.com.hernani.Cargo
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDate
import javax.persistence.*

@Entity
class Funcionario(
    val nome: String,
    val cpf: String,
    val idade: Int,
    val cargo: Cargo,

)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @CreationTimestamp
    val criadoEm: LocalDate? = null
}
