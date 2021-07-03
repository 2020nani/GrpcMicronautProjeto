package br.com.hernani

import java.io.FileInputStream
import java.io.FileOutputStream

fun main(){
    val request = FuncionarioRequest.newBuilder()
        .setNome("hernani")
        .setCpf("111-111-111-11")
        .setIdade(33)
        .setCargo(Cargo.DEV)
        .build()
    //escrevemos o objeto e salvamos no arquivo em disco na rede
    println(request)
    request.writeTo(FileOutputStream("funcionario-request.bin"))
    //lemos o objeto e alteramos o atributo cargo
    val request2 = FuncionarioRequest.newBuilder().mergeFrom(FileInputStream("funcionario-request.bin"))
    request2.setCargo(Cargo.ANALISTA)
     println(request2)
}