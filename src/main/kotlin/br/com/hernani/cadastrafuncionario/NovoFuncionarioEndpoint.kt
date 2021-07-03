package br.com.hernani.cadastrafuncionario

import br.com.hernani.FuncionarioRequest
import br.com.hernani.FuncionarioResponse
import br.com.hernani.FuncionarioServiceGrpc
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import javax.inject.Singleton

@Singleton
class NovoFuncionarioEndpoint(
    val funcionarioRepository: FuncionarioRepository
) : FuncionarioServiceGrpc.FuncionarioServiceImplBase() {
    //instancia logger
    val logger = LoggerFactory.getLogger(NovoFuncionarioEndpoint::class.java)
    override fun cadastraFuncionario(
        request: FuncionarioRequest,
        responseObserver: StreamObserver<FuncionarioResponse>
    ) {
        logger.info("Recebendo os dados de request funcionario ${request.nome}")
        val funcionario = request.converte()

        funcionarioRepository.save(funcionario)

        val response = FuncionarioResponse
            .newBuilder()
            .setId(funcionario.id ?: throw IllegalStateException("Id nao pode ser nulo"))
            .build()

        logger.info("Funcionario salvo com Id: ${response.id}")
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}

fun FuncionarioRequest.converte(): Funcionario {

    return Funcionario(nome, cpf, idade, cargo)
}