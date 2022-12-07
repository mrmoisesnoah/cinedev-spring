package br.com.dbc.vemser.cinedev.controller.documentinterface;

import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteCreateDTO;
import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteDTO;
import br.com.dbc.vemser.cinedev.dto.relatorios.RelatorioCadastroIngressoClienteDTO;
import br.com.dbc.vemser.cinedev.exception.BancoDeDadosException;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

public interface OperationControllerCliente {

    @Operation(summary = "Realiza Listagem dos Clientes Cadastrados", description = "Realiza a Listagem dos dados referente a busca por Clientes.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "A algo de errado com as inserções de sua pesquisa"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    List<ClienteDTO> list() throws RegraDeNegocioException, BancoDeDadosException;

    @Operation(summary = "Realiza retorno do Cliente cadastrado com um Id específico.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Não há um cliente cadastrado com esse Id."),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro ao retornar o Cliente, tente de novo mais tarde.")})
    ClienteDTO listarClientePeloId(@PathVariable Integer idCliente) throws RegraDeNegocioException;

//    @Operation(summary = "Realiza o cadastramento de Clientes", description = "Realiza o cadastramento de dados dos Clientes!")
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Cadastro realizado com sucesso!"),
//            @ApiResponse(responseCode = "403", description = "Erro na inserção de dados!"),
//            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
//    ResponseEntity<ClienteDTO> cadastrarCliente(@Valid @RequestBody ClienteCreateDTO clienteCreateDTO)
//            throws BancoDeDadosException, RegraDeNegocioException;

    @Operation(summary = "Realiza a atualização das informações dos Clientes", description = "Realiza a alteração de dados dos usuários a partir da busca por ID!")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Atualização de dados realizada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso!!"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    ResponseEntity<ClienteDTO> updateUsuario(@Valid @RequestBody ClienteCreateDTO clienteCreateDTO) throws RegraDeNegocioException, BancoDeDadosException;

    @Operation(summary = "Realiza a remoção de dados cliente", description = "Realiza a remoção dos Cliente a partir do 'ID'!")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Remoção do Cliente realizada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso!!"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    void delete() throws RegraDeNegocioException, BancoDeDadosException;

    @Operation(summary = "Realiza a listagem do relatório de clientes ", description = "Realiza a listagens dos filmes e ingressos do cliente selecionado pelo 'id'!")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Remoção de dados realizada!"),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso!!"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    public ResponseEntity<List<RelatorioCadastroIngressoClienteDTO>> listarRelatorioCadastroIngressoClienteDTO(@RequestParam(required = false, name = "idCliente") Integer idCliente);
}
