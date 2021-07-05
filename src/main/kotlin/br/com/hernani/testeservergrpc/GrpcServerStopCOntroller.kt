package br.com.hernani.testeservergrpc

import io.micronaut.grpc.server.GrpcEmbeddedServer
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import javax.inject.Inject

@Controller
class GrpcServerStopCOntroller(@Inject val grpcServer: GrpcEmbeddedServer) {

    @Get("/stopgrpc")
    fun stop (): MutableHttpResponse<String>? {
        grpcServer.stop()
        return HttpResponse.ok("Status: ${grpcServer.isRunning}")
    }
}