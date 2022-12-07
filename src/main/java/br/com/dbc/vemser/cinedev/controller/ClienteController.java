package br.com.dbc.vemser.cinedev.controller;

import br.com.dbc.vemser.cinedev.controller.documentinterface.OperationControllerCliente;
import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteCreateDTO;
import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteDTO;
import br.com.dbc.vemser.cinedev.dto.relatorios.RelatorioCadastroIngressoClienteDTO;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.service.ClienteService;
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
@RequestMapping("/cliente")
public class ClienteController implements OperationControllerCliente {
    private final ClienteService clienteService;
    @Override
    @GetMapping
    public List<ClienteDTO> list() throws RegraDeNegocioException {
        return clienteService.listarTodosClientes();
    }

    @Override
    @GetMapping("/{idCliente}")
    public ClienteDTO listarClientePeloId(@PathVariable Integer idCliente) throws RegraDeNegocioException {
        return clienteService.listarClienteDTOPeloId(idCliente);
    }

//    @Override
//    @PostMapping
//    public ResponseEntity<ClienteDTO> cadastrarCliente(@Valid @RequestBody ClienteCreateDTO clienteCreateDTO)
//            throws  RegraDeNegocioException {
//        return new ResponseEntity<>(clienteService.cadastrarCliente(clienteCreateDTO), HttpStatus.OK);
//    }


    @PutMapping("/atualizar-cliente-usuario")
    public ResponseEntity<ClienteDTO> updateUsuario(@Valid @RequestBody ClienteCreateDTO clienteCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(clienteService.atualizarClienteLogado(clienteCreateDTO), HttpStatus.OK);
    }
    @Override
    @DeleteMapping("/delete-cliente-logado")
    public void delete() throws RegraDeNegocioException {
        clienteService.deletarClienteLogado();
    }

    @GetMapping("/cliente-relatorio")
    public ResponseEntity<List<RelatorioCadastroIngressoClienteDTO>> listarRelatorioCadastroIngressoClienteDTO(@RequestParam(required = false, name = "idCliente") Integer idCliente) {
        List<RelatorioCadastroIngressoClienteDTO> relatorioCadastroIngressoClienteDTO = clienteService.listarRelatorioPersonalizado(idCliente);
        return new ResponseEntity<>(relatorioCadastroIngressoClienteDTO, HttpStatus.OK);
    }
}


