package br.com.dbc.vemser.cinedev.controller;

import br.com.dbc.vemser.cinedev.controller.documentinterface.OperationControllerAvaliacao;
import br.com.dbc.vemser.cinedev.dto.avaliacoes.AvaliacoesCreateDTO;
import br.com.dbc.vemser.cinedev.dto.avaliacoes.AvaliacoesDTO;
import br.com.dbc.vemser.cinedev.dto.avaliacoes.AvaliacoesDTOContador;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.service.AvaliacoesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/avaliacoes")
public class AvaliacoesController implements OperationControllerAvaliacao {

    private final AvaliacoesService avaliacoesService;

    @GetMapping("/group-by-nota")
    public ResponseEntity<List<AvaliacoesDTOContador>> groupByNotas() {
        return new ResponseEntity<>(avaliacoesService.groupByNotaAndCount(), HttpStatus.OK);
    }

    @GetMapping("/list-by-nota/{nota}")
    public ResponseEntity<List<AvaliacoesDTO>> listByNotaContains(@PathVariable("nota") Double nota) {
        return new ResponseEntity<>(avaliacoesService.listByNotaContains(nota), HttpStatus.OK);
    }

    @PostMapping("/criar-avaliacao")
    public ResponseEntity<AvaliacoesCreateDTO> criarAvaliacao(
            @Valid @RequestBody AvaliacoesCreateDTO avaliacoesCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(avaliacoesService.adicionarAvaliacao(avaliacoesCreateDTO), HttpStatus.OK);
    }
}
