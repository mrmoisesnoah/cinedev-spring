package br.com.dbc.vemser.cinedev.controller.documentinterface;

import br.com.dbc.vemser.cinedev.dto.log.LogDTO;
import br.com.dbc.vemser.cinedev.dto.log.LogDTOContador;
import br.com.dbc.vemser.cinedev.entity.enums.TipoLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface OperationControllerLog {
    @Operation(summary = "Listar todos os Logs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista todos Logs com sucesso"),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para realizar essa operação.")
    })
    List<LogDTO> list();

    @Operation(summary = "Listar todos os Logs pelo tipo", description = "Retorna lista com sucesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista todos os Logs com sucesso"),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para realizar essa operação.")
    })
    ResponseEntity<List<LogDTO>> listByTipoLog(@RequestParam(required = false) TipoLog tipoLog,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data);

    @Operation(summary = "Lista a quantidade de Logs pelo tipo", description = "Retorna uma lista com a quantidade de Logs que tem por cada tipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista com sucesso."),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para realizar essa operação.")
    })
    ResponseEntity<List<LogDTOContador>> groupByTipoLogAndCount();

    @Operation(summary = "Retorna quantidade de um tipo de log")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna quantidade de um tipo de Log com sucesso."),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para realizar essa operação.")
    })
    ResponseEntity<LogDTOContador> getCountByTipoLog(TipoLog tipoLog);

    @Operation(summary = "Listar todos os Logs de uma data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna lista com sucesso"),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para realizar essa operação.")
    })
    ResponseEntity<List<LogDTO>> getByData(String data) throws Exception;
}
