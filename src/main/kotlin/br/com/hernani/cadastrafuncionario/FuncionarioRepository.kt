package br.com.hernani.cadastrafuncionario

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
open interface FuncionarioRepository: JpaRepository<Funcionario, Long> {
}