package br.com.dbc.vemser.cinedev.controller;

import br.com.dbc.vemser.cinedev.controller.documentinterface.OperationControllerCinema;
import br.com.dbc.vemser.cinedev.dto.cinemadto.CinemaCreateDTO;
import br.com.dbc.vemser.cinedev.dto.cinemadto.CinemaDTO;
import br.com.dbc.vemser.cinedev.dto.paginacaodto.PageDTO;
import br.com.dbc.vemser.cinedev.dto.relatorios.RelatorioCadastroCinemaFilmeDTO;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.service.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cinema")
public class CinemaController implements OperationControllerCinema {

    private final CinemaService cinemaService;

    @Override
    @GetMapping
    public List<CinemaDTO> list() throws RegraDeNegocioException {
        return cinemaService.listarCinema();
    }

    @Override
    @GetMapping("/{idCinema}")
    public CinemaDTO listaPorId(@PathVariable("idCinema") Integer id) throws RegraDeNegocioException {
        return cinemaService.listarCinemaPorId(id);
    }

//    @Override
//    @PostMapping
//    public ResponseEntity<CinemaDTO> cadastrarCinema(@Valid @RequestBody CinemaCreateDTO cinemaCreateDTO)
//            throws RegraDeNegocioException {
//        return new ResponseEntity<>(cinemaService.adicionarCinema(cinemaCreateDTO), HttpStatus.OK);
//    }
//

    @PutMapping("/atualizar-cinema-usuario")
    public ResponseEntity<CinemaDTO> updateCinemaPorUsuario(@Valid @RequestBody CinemaCreateDTO cinemaCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(cinemaService.atualizarCinemaLogado(cinemaCreateDTO), HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/deletar-cinema-logado")
    public ResponseEntity<Void> deletarCinemaLogado() throws
            RegraDeNegocioException {
        cinemaService.deletarCinemaLogado();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/cinema-relatorio")
    public ResponseEntity<List<RelatorioCadastroCinemaFilmeDTO>> listarRelatorioPersonalizado(@RequestParam(required = false, name = "idCinema") Integer idCinema) {
        return new ResponseEntity<>(cinemaService.listarRelatorioPersonalizado(idCinema), HttpStatus.OK);
    }

    @GetMapping("/cinema-relatorio/cinema-logado")
    public ResponseEntity<List<RelatorioCadastroCinemaFilmeDTO>> listarRelatorioPersonalizadoCinemaLogado() throws RegraDeNegocioException {
        return new ResponseEntity<>(cinemaService.listarRelatorioPersonalizadoCinemaLogado(), HttpStatus.OK);
    }

    @GetMapping("/find-cinema-paginado")
    public ResponseEntity<PageDTO<CinemaDTO>> listarCinemasPaginados(Integer paginaQueEuQuero, Integer tamanhoDeRegistrosPorPagina) {
        return new ResponseEntity<>(cinemaService.listCinemaPaginado(paginaQueEuQuero, tamanhoDeRegistrosPorPagina), HttpStatus.OK);
    }
}