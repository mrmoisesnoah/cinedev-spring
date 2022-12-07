package br.com.dbc.vemser.cinedev.controller;

import br.com.dbc.vemser.cinedev.controller.documentinterface.OperationControllerAuth;
import br.com.dbc.vemser.cinedev.dto.UsuarioDTO;
import br.com.dbc.vemser.cinedev.dto.cinemadto.CinemaCreateDTO;
import br.com.dbc.vemser.cinedev.dto.cinemadto.CinemaDTO;
import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteCreateDTO;
import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteDTO;
import br.com.dbc.vemser.cinedev.dto.login.LoginDTO;
import br.com.dbc.vemser.cinedev.dto.recuperarsenhadto.RecuperarSenhaDTO;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.service.CinemaService;
import br.com.dbc.vemser.cinedev.service.ClienteService;
import br.com.dbc.vemser.cinedev.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController implements OperationControllerAuth {
    public static final int ROLE_RECCLIENTE_ID = 4;
    public static final int ROLE_RECCINEMA_ID = 5;
    public static final int ROLE_RECADMIN_ID = 6;
    private final CinemaService cinemaService;
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;

    @PostMapping("/fazer-login")
    public ResponseEntity<String> autenticar(@RequestBody @Valid LoginDTO loginDTO) throws RegraDeNegocioException {
        String token = usuarioService.autenticar(loginDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/recuperar-senha-cliente")
    public ResponseEntity<Void> recuperarSenhaCliente(@Valid @RequestBody RecuperarSenhaDTO email) throws RegraDeNegocioException {
        usuarioService.recuperarSenha(email, ROLE_RECCLIENTE_ID);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recuperar-senha-cinema")
    public ResponseEntity<Void> recuperarSenhaCinema(@Valid @RequestBody RecuperarSenhaDTO email) throws RegraDeNegocioException {
        usuarioService.recuperarSenha(email, ROLE_RECCINEMA_ID);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recuperar-senha-admin")
    public ResponseEntity<Void> recuperarSenhaAdminstrador(@Valid @RequestBody RecuperarSenhaDTO email) throws RegraDeNegocioException {
        usuarioService.recuperarSenha(email, ROLE_RECADMIN_ID);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar-senha-cinema")
    public ResponseEntity<Void> atualizarSenhaCinema(@Valid @RequestBody String senha) throws RegraDeNegocioException {
        usuarioService.mudarSenhaUsuarioLogado(senha, ROLE_RECCINEMA_ID);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar-senha-cliente")
    public ResponseEntity<Void> atualizarSenhaCliente(@Valid @RequestBody String senha) throws RegraDeNegocioException {
        usuarioService.mudarSenhaUsuarioLogado(senha, ROLE_RECCLIENTE_ID);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar-senha-admin")
    public ResponseEntity<Void> atualizarSenhaAdministrador(@Valid @RequestBody String senha) throws RegraDeNegocioException {
        usuarioService.mudarSenhaUsuarioLogado(senha, ROLE_RECADMIN_ID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario-logado")
    public ResponseEntity<UsuarioDTO> retornarUsuario() throws RegraDeNegocioException {
        return new ResponseEntity<>(objectMapper.convertValue(usuarioService.getLoggedUser(), UsuarioDTO.class), HttpStatus.OK);
    }

    @PostMapping("/novo-administrador")
    public ResponseEntity<UsuarioDTO> criarAdministrador(@RequestBody @Valid LoginDTO loginDTO) throws RegraDeNegocioException {
        UsuarioDTO usuarioDTO = usuarioService.cadastrarAdministrador(loginDTO);
        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @PostMapping("/novo-cliente")
    public ResponseEntity<ClienteDTO> criarCliente(@RequestBody @Valid ClienteCreateDTO criarClienteDTO) throws RegraDeNegocioException {
        ClienteDTO clienteDTO = clienteService.cadastrarCliente(criarClienteDTO);
        return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
    }

    @PostMapping("/novo-cinema")
    public ResponseEntity<CinemaDTO> criarcinema(@RequestBody @Valid CinemaCreateDTO criarCinema) throws RegraDeNegocioException {
        CinemaDTO cinemaDTO = cinemaService.adicionarCinema(criarCinema);
        return new ResponseEntity<>(cinemaDTO, HttpStatus.OK);
    }
}
