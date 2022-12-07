package br.com.dbc.vemser.cinedev.controller.documentinterface;

import br.com.dbc.vemser.cinedev.dto.ingressodto.IngressoCompradoDTO;
import br.com.dbc.vemser.cinedev.dto.ingressodto.IngressoCreateDTO;
import br.com.dbc.vemser.cinedev.dto.ingressodto.IngressoDTO;
import br.com.dbc.vemser.cinedev.dto.paginacaodto.PageDTO;
import br.com.dbc.vemser.cinedev.dto.relatorios.RelatorioCadastroIngressoClienteDTO;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface OperationControllerIngresso {

    @Operation(summary = "Listar todos os Ingressos disponíveis. ", description = "Lista os Ingressos que ainda estão disponíveis para compra")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Listagem executada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "A algo de errado com as inserções de sua pesquisa"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    List<IngressoDTO> listarIngressos() throws RegraDeNegocioException;

    @Operation(summary = "Listagem de Ingressos por idIngresso. ", description = "Lista os Ingressos por IdIngresso")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Listagem executada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "A algo de errado com as inserções de sua pesquisa"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    IngressoDTO listarIngressosPorId(@PathVariable("idIngresso") Integer id) throws RegraDeNegocioException;

    @Operation(summary = "Listagem de Ingressos comprados. ", description = "Lista os Ingressos comprados!")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Listagem executada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "A algo de errado com as inserções de sua pesquisa"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    public List<IngressoDTO> listarIngressosComprados() throws RegraDeNegocioException;

    @Operation(summary = "Listagem de Ingressos comprados por idCliente ", description = "Lista o banco de ingressos comprados pelo Cliente, a partir da IdCliente")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Listagem executada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "A algo de errado com as inserções de sua pesquisa"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    List<RelatorioCadastroIngressoClienteDTO> listarIngressosCompradosPorCliente(Integer id) throws RegraDeNegocioException;


    @Operation(summary = "Cadastro dos ingressos pelos cinemas.", description = "Area para cadastro e disponibiliação dos ingressos pelos cinemas")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Cadastro realizado com Sucesso!"),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso!!"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    ResponseEntity<IngressoDTO> createIngresso(@Valid @RequestBody IngressoCreateDTO ingresso) throws RegraDeNegocioException;

    @Operation(summary = "Realiza a compra de Ingressos", description = " Realiza a compra do IngressoEntity, considerando a IdCliente que ira realizar a compra, e o IdIngresso que será comprado")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Atualização de IngressoEntity realizada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso!!"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    ResponseEntity<IngressoCompradoDTO> comprarIngresso(@PathVariable("idCliente") Integer idCliente, @PathVariable("idIngresso") Integer idIngresso) throws RegraDeNegocioException, JsonProcessingException;

    @Operation(summary = "Remoção de Ingressos por ID. ", description = "Remoção do IngressoEntity  partir do 'id'!")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Remoção do IngressoEntity realizada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso!!"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    ResponseEntity<Void> removeIngresso(@PathVariable("idIngresso") Integer id) throws RegraDeNegocioException;

    @Operation(summary = "Listagem de Ingressos por Página. ", description = "Listagens dos ingressos por página de acordo com o usuário!")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Listagens de ingressos realizada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso!!"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    ResponseEntity<PageDTO<IngressoDTO>> listarIngressoPaginado(Integer paginaQueEuQuero, Integer tamanhoDeRegistrosPorPagina);

}