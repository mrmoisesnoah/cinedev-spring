package br.com.dbc.vemser.cinedev.controller.documentinterface;

import br.com.dbc.vemser.cinedev.dto.filmedto.FilmeCreateDTO;
import br.com.dbc.vemser.cinedev.dto.filmedto.FilmeDTO;
import br.com.dbc.vemser.cinedev.exception.BancoDeDadosException;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface OperationControllerFilme {

    @Operation(summary = "Realiza a listagem de Filmes.", description = "Lista todos os filmes cadastrados no Banco de Dados")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Listagem executada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "A algo de errado com as inserções de sua pesquisa!"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    List<FilmeDTO> list() throws RegraDeNegocioException;

    @Operation(summary = "Realiza busca de filmes por ID.", description = "Localização do filme de acordo com a ID informada")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Listagem executada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "A algo de errado com as inserções de sua pesquisa!"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    FilmeDTO listarByIdFilme(@PathVariable("idFilme") Integer idFilme) throws RegraDeNegocioException;


    @Operation(summary = "Realiza o Cadastro do FilmeEntity.", description = "Realiza o dadastramento de dados do FilmeEntity")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Cadastro realizado com Sucesso!"),
            @ApiResponse(responseCode = "403", description = "Erro na inserção de dados!"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    ResponseEntity<FilmeDTO> cadastrarFilme(@Valid @RequestBody FilmeCreateDTO filmeCreateDTO) throws RegraDeNegocioException,
            RegraDeNegocioException, BancoDeDadosException;

    @Operation(summary = "Realiza a atualização de dados do FilmeEntity ", description = "Realiza a alteração de dados dos Filmes a partir da referencia de pesquisa 'ID'!")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = " A atualização do FilmeEntity foi realizada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso!!"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    ResponseEntity<FilmeDTO> update(@PathVariable("idPessoa") Integer id,
                                    @Valid @RequestBody FilmeCreateDTO filmeCreateDTO) throws RegraDeNegocioException, BancoDeDadosException;

    @Operation(summary = "Realiza a remoção do FilmeEntity", description = "Realiza a remoção de filmes a partir do 'ID'!")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Remoção de dados realizada!"),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso!!"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")})
    ResponseEntity<Void> delete(@PathVariable("idFilme") Integer id) throws RegraDeNegocioException, BancoDeDadosException;

}
