package br.com.dbc.vemser.cinedev.controller.documentinterface;

import br.com.dbc.vemser.cinedev.dto.avaliacoes.AvaliacoesCreateDTO;
import br.com.dbc.vemser.cinedev.dto.avaliacoes.AvaliacoesDTO;
import br.com.dbc.vemser.cinedev.dto.avaliacoes.AvaliacoesDTOContador;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface OperationControllerAvaliacao {
    @Operation(summary = "Lista o número de avaliações por cada nota",
            description = "Lista a quantidade avaliações agrupadas pela nota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista avalições agrupada pela nota com sucesso"),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para realizar essa operação")
    })
    ResponseEntity<List<AvaliacoesDTOContador>> groupByNotas();


    @Operation(summary = "Listar notas agrupadas pela Nota", description = "Lista todas as notas com a mesma nota")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista notas com sucesso."),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para realizar essa operação")
    })
    ResponseEntity<List<AvaliacoesDTO>> listByNotaContains(@PathVariable("nota") Double nota);

    @Operation(summary = "Criar uma nova avaliação", description = "Criar uma nova avaliação para um filme.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação criada com sucesso."),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para realizar essa operação")
    })
    ResponseEntity<AvaliacoesCreateDTO> criarAvaliacao(
            @Valid @RequestBody AvaliacoesCreateDTO avaliacoesCreateDTO) throws RegraDeNegocioException;
}
