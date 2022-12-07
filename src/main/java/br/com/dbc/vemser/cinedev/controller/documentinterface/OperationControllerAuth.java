package br.com.dbc.vemser.cinedev.controller.documentinterface;

import br.com.dbc.vemser.cinedev.dto.UsuarioDTO;
import br.com.dbc.vemser.cinedev.dto.cinemadto.CinemaCreateDTO;
import br.com.dbc.vemser.cinedev.dto.cinemadto.CinemaDTO;
import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteCreateDTO;
import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteDTO;
import br.com.dbc.vemser.cinedev.dto.login.LoginDTO;
import br.com.dbc.vemser.cinedev.dto.recuperarsenhadto.RecuperarSenhaDTO;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface OperationControllerAuth {
    @Operation(summary = "Login", description = "Realiza o seu login com email e senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Email ou senha incorretos. Login não concluído.")
    })
    ResponseEntity<String> autenticar(@RequestBody @Valid LoginDTO loginDTO) throws RegraDeNegocioException;

    @Operation(summary = "Esqueci a senha do Cliente",
            description = "Caso você tenha um Cliente cadastrado no nosso banco de dados, envia um email com um token para trocar a senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recuperação de senha do Cliente realiza com sucesso."),
            @ApiResponse(responseCode = "403", description = "Email incorreto. Recuperação da senha não foi concluída")
    })
    ResponseEntity<Void> recuperarSenhaCliente(@Valid @RequestBody RecuperarSenhaDTO email) throws RegraDeNegocioException;

    @Operation(summary = "Esqueci a senha do Cinema",
            description = "Caso você tenha um Cinema cadastrado no nosso banco de dados, envia um email com um token para trocar a senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recuperação de senha do Cinema realiza com sucesso."),
            @ApiResponse(responseCode = "403", description = "Email incorreto. Recuperação da senha não foi concluída")
    })
    ResponseEntity<Void> recuperarSenhaCinema(@Valid @RequestBody RecuperarSenhaDTO email) throws RegraDeNegocioException;

    @Operation(summary = "Esqueci a senha do Admin",
            description = "Caso você tenha um Admin cadastrado no nosso banco de dados, envia um email com um token para trocar a senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recuperação de senha do Admin realiza com sucesso."),
            @ApiResponse(responseCode = "403", description = "Email incorreto. Recuperação da senha não foi concluída")
    })
    ResponseEntity<Void> recuperarSenhaAdminstrador(@Valid @RequestBody RecuperarSenhaDTO email) throws RegraDeNegocioException;

    @Operation(summary = "Atualiza a senha do Cinema", description = "Realiza a mudança de senha do Cinema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualiza senha de um Cinema com sucesso."),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para mudar a senha.")
    })
    ResponseEntity<Void> atualizarSenhaCinema(@Valid @RequestBody String senha) throws RegraDeNegocioException;

    @Operation(summary = "Atualiza a senha do Cliente", description = "Realiza a mudança de senha dou Clinte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualiza senha de um Cliente com sucesso."),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para mudar a senha.")
    })
    ResponseEntity<Void> atualizarSenhaCliente(@Valid @RequestBody String senha) throws RegraDeNegocioException;

    @Operation(summary = "Atualiza a senha do Admin", description = "Realiza a mudança de senha dou Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualiza senha de um Admin com sucesso."),
            @ApiResponse(responseCode = "403", description = "Você não tem autorização para mudar a senha.")
    })
    ResponseEntity<Void> atualizarSenhaAdministrador(@Valid @RequestBody String senha) throws RegraDeNegocioException;

    @Operation(summary = "Retorna usuário logado", description = "Retorna usuário que está logado no momento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna algumas informação do Usuário logado."),
            @ApiResponse(responseCode = "500", description = "Não foi informado um Usuário logado.")
    })
    ResponseEntity<UsuarioDTO> retornarUsuario() throws RegraDeNegocioException;

    @Operation(summary = "Criar um Admin", description = "Cria um usuário com a Role de Admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrado criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Algum campo foi informado de forma incorreta, tente de novo.")
    })
    ResponseEntity<UsuarioDTO> criarAdministrador(@RequestBody @Valid LoginDTO loginDTO) throws RegraDeNegocioException;

    @Operation(summary = "Criar um Cliente", description = "Cria um usuário com a Role de Cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Algum campo foi informado de forma incorreta, tente de novo.")
    })
    ResponseEntity<ClienteDTO> criarCliente(@RequestBody @Valid ClienteCreateDTO criarClienteDTO) throws RegraDeNegocioException;

    @Operation(summary = "Criar um Cinema", description = "Cria um usuário com a Role de Cinema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Algum campo foi informado de forma incorreta, tente de novo.")
    })
    ResponseEntity<CinemaDTO> criarcinema(@RequestBody @Valid CinemaCreateDTO criarCinema) throws RegraDeNegocioException;
}
