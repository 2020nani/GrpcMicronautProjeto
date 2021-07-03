package br.com.hernani.cadastrafuncionario

import br.com.hernani.FuncionarioRequest
import br.com.hernani.FuncionarioResponse
import br.com.hernani.FuncionarioServiceGrpc
import io.grpc.stub.StreamObserver
import java.lang.IllegalStateException
import javax.inject.Singleton

@Singleton
class NovoFuncionarioEndpoint(
    val funcionarioRepository: FuncionarioRepository
) : FuncionarioServiceGrpc.FuncionarioServiceImplBase() {
    override fun cadastraFuncionario(
        request: FuncionarioRequest,
        responseObserver: StreamObserver<FuncionarioResponse>
    ) {
        val funcionario = request.converte()

        funcionarioRepository.save(funcionario)

        val response = FuncionarioResponse
            .newBuilder()
            .setId(funcionario.id ?: throw IllegalStateException("Id nao pode ser nulo"))
            .build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}

fun FuncionarioRequest.converte(): Funcionario {

    return Funcionario(nome, cpf, idade, cargo)
}