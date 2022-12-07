package br.com.dbc.vemser.cinedev.controller;

import br.com.dbc.vemser.cinedev.dto.log.LogDTO;
import br.com.dbc.vemser.cinedev.dto.log.LogDTOContador;
import br.com.dbc.vemser.cinedev.entity.enums.TipoLog;
import br.com.dbc.vemser.cinedev.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/log")
public class LogController {
    private final LogService logService;

    @GetMapping("/list")
    public List<LogDTO> list() {
        return logService.listAllLogs();
    }

    @GetMapping("/list-by-tipo-log/")
    public ResponseEntity<List<LogDTO>> listByTipoLog(@RequestParam(required = false) TipoLog tipoLog,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        log.info("Listando Logs");
        List<LogDTO> lista = logService.listLogsByTipoLog(tipoLog, data);
        log.info("Logs listados com sucesso!");
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/group-by-tipo-and-count")
    public ResponseEntity<List<LogDTOContador>> groupByTipoLogAndCount() {
        return new ResponseEntity<>(logService.groupByTipoLogAndCount(), HttpStatus.OK);
    }

    @GetMapping("/count-logs-by-tipo")
    public ResponseEntity<LogDTOContador> getCountByTipoLog(TipoLog tipoLog) {
        return new ResponseEntity<>(logService.countLogsByTipo(tipoLog), HttpStatus.OK);
    }

    @GetMapping("/find-all-by-data")
    public ResponseEntity<List<LogDTO>> getByData(String data) throws Exception {
        return new ResponseEntity<>(logService.listAllByData(data), HttpStatus.OK);
    }
}