package br.com.dbc.vemser.cinedev.controller.documentinterface;

import br.com.dbc.vemser.cinedev.dto.cinemadto.CinemaCreateDTO;
import br.com.dbc.vemser.cinedev.dto.cinemadto.CinemaDTO;
import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteCreateDTO;
import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteDTO;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface OperationControllerAdministrador {
    @Operation(summary = "Atualizar Cinema", description = "Atualiza um Cinema filtrado pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Cliente não cadastrado"),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para realizar essa operação.")
    })
    ResponseEntity<CinemaDTO> updateCinema(@PathVariable Integer idCinema, @Valid @RequestBody CinemaCreateDTO cinemaCreateDTO) throws RegraDeNegocioException;

    @Operation(summary = "Atualizar Cliente", description = "Atualiza um Cliente filtrado pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Cinema não cadastrado."),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para realizar essa operação.")
    })
    ResponseEntity<ClienteDTO> updateCliente(@PathVariable Integer idCliente,
                                                    @Valid @RequestBody ClienteCreateDTO clienteCreateDTO) throws RegraDeNegocioException;

    @Operation(summary = "Deletar Cliente", description = "Deleta um Cliente filtrado pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente deletado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Cliente não cadastrado"),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para realizar essa operação.")
    })
    ResponseEntity<Void> deletarUsuarioCliente(@PathVariable Integer idCliente) throws RegraDeNegocioException;

    @Operation(summary = "Deletar Cinema", description = "Deleta um Cinema filtrado pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema deletado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Cinema não cadastrado."),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para realizar essa operação.")
    })
    ResponseEntity<Void> deletarCinemaPorUsuario(@PathVariable Integer idCinema) throws RegraDeNegocioException;
}
