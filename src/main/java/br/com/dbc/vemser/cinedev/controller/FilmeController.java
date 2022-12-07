package br.com.dbc.vemser.cinedev.controller;


import br.com.dbc.vemser.cinedev.controller.documentinterface.OperationControllerFilme;
import br.com.dbc.vemser.cinedev.dto.filmedto.FilmeCreateDTO;
import br.com.dbc.vemser.cinedev.dto.filmedto.FilmeDTO;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.service.FilmeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/filme")
public class FilmeController implements OperationControllerFilme {
    private final FilmeService filmeService;

    @Override
    @GetMapping
    public List<FilmeDTO> list() throws RegraDeNegocioException {
        return filmeService.listarTodosFilmes();
    }

    @Override
    @GetMapping("/{idFilme}")
    public FilmeDTO listarByIdFilme(@PathVariable("idFilme") Integer idFilme) throws RegraDeNegocioException {
        return filmeService.listarDTOPeloId(idFilme);
    }

    @Override
    @PostMapping
    public ResponseEntity<FilmeDTO> cadastrarFilme(@Valid @RequestBody FilmeCreateDTO filme) throws RegraDeNegocioException {
        return new ResponseEntity<>(filmeService.adicionarFilme(filme), HttpStatus.OK);
    }

    @Override
    @PutMapping("/{idFilme}")
    public ResponseEntity<FilmeDTO> update(@PathVariable("idFilme") Integer id,
                                           @Valid @RequestBody FilmeCreateDTO filmeAtualizar) throws RegraDeNegocioException {
        return new ResponseEntity<>(filmeService.editarFilme(id, filmeAtualizar), HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{idFilme}")
    public ResponseEntity<Void> delete(@PathVariable("idFilme") Integer id) throws RegraDeNegocioException {
        filmeService.removerFilme(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
