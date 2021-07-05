package br.com.hernani.cadastrafuncionario

import br.com.hernani.ErrorDetails
import br.com.hernani.FuncionarioRequest
import br.com.hernani.FuncionarioResponse
import br.com.hernani.FuncionarioServiceGrpc
import br.com.hernani.validationcustomizadas.isCPF
import com.google.protobuf.Any
import com.google.rpc.Code
import io.grpc.Status
import io.grpc.protobuf.StatusProto
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
        // simulando verificacao seguranca
        if (request.cpf.startsWith("336")){
            val statusProto = com.google.rpc.Status.newBuilder()
                .setCode(Code.PERMISSION_DENIED.number)
                .setMessage("Usuario nao pode acessar este recurso")
                .addDetails(
                    Any.pack(ErrorDetails.newBuilder()
                    .setCode(401)
                    .setMessage("Token inexpirado")
                    .build())
                )
                .build()
            val e = StatusProto.toStatusRuntimeException(statusProto)
            responseObserver.onError(e)
        }
        //tratando erros
        val nome = request.nome
        if (nome == null || nome.isBlank()) {
            logger.info("Dados request invalido")
            val error = Status.INVALID_ARGUMENT
                .withDescription("Campo nome e obrigatorio")
                .asRuntimeException()
            responseObserver.onError(error)
        }
        val cpf = request.cpf
        if (!isCPF(cpf) || cpf == null || cpf.isBlank()) {
            logger.info("Cpf invalido")
            val error = Status.INVALID_ARGUMENT
                .withDescription("Campo cpf esta invalido\n")
                .augmentDescription("Formato cpf deve ser xxxxxxxxxxx e ser valido")
                .asRuntimeException()
            responseObserver.onError(error)
        }
        logger.info("Recebendo os dados de request funcionario ${request.nome}")

        val funcionario = request.converte()

        try {
            funcionarioRepository.save(funcionario)
        } catch (e: Exception) {
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription(e.message)
                    .withCause(e) //anexado status de erro porem nao e enviado ao client
                    .asRuntimeException()
            )
        }

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