package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.dto.UsuarioDTO;
import br.com.dbc.vemser.cinedev.dto.login.LoginDTO;
import br.com.dbc.vemser.cinedev.dto.recuperarsenhadto.RecuperarSenhaDTO;
import br.com.dbc.vemser.cinedev.entity.CargoEntity;
import br.com.dbc.vemser.cinedev.entity.UsuarioEntity;
import br.com.dbc.vemser.cinedev.enums.TipoEmails;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.UsuarioRepository;
import br.com.dbc.vemser.cinedev.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    public static final int ROLE_ADMIN_ID = 1;
    public static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado!";
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;
    private final CargoService cargoService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;


    public Integer getIdLoggedUser() {
        return Integer.parseInt(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
    }

    public UsuarioEntity getLoggedUser() throws RegraDeNegocioException {
        return findById(getIdLoggedUser());
    }

    public UsuarioEntity findByEmail(String email) throws RegraDeNegocioException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RegraDeNegocioException(USUARIO_NAO_ENCONTRADO));
    }

    public Optional<UsuarioEntity> findOptionalByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioEntity findById(Integer idLoggedUser) throws RegraDeNegocioException {
        return usuarioRepository.findById(idLoggedUser)
                .orElseThrow(() -> new RegraDeNegocioException(USUARIO_NAO_ENCONTRADO));
    }

    public UsuarioDTO cadastrarAdministrador(LoginDTO loginDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioSalvo = cadastrarUsuario(loginDTO.getEmail(), loginDTO.getSenha(), ROLE_ADMIN_ID);
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioSalvo, UsuarioDTO.class);
        return usuarioDTO;
    }

    public UsuarioEntity cadastrarUsuario(String email, String senha, Integer idRole) throws RegraDeNegocioException {
        CargoEntity cargo = cargoService.findById(idRole);
        String senhaEncode = passwordEncoder.encode(senha);

        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setEmail(email);
        usuarioEntity.setSenha(senhaEncode);
        usuarioEntity.setCargos(Set.of(cargo));
        usuarioEntity.setAtivo('S');

        return usuarioRepository.save(usuarioEntity);
    }

    public String autenticar(@RequestBody @Valid LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getSenha()
                );
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        // UsuarioEntity
        Object principal = authenticate.getPrincipal();

        UsuarioEntity usuarioEntity = (UsuarioEntity) principal;

        return tokenService.getToken(usuarioEntity);
    }

    public void recuperarSenha(RecuperarSenhaDTO emailDTO, Integer idRoleRec) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = findByEmail(emailDTO.getEmail());
        String token = tokenService.getTokenTrocarSenha(usuarioEntity);
        UsuarioDTO usuarioDTO = objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
        emailService.sendEmail(usuarioDTO, TipoEmails.REC_SENHA, token);
        CargoEntity cargo = cargoService.findById(idRoleRec);
        usuarioEntity.getCargos().add(cargo);
        usuarioRepository.save(usuarioEntity);
    }

    public void mudarSenhaUsuarioLogado(String senha, Integer idRoleRec) throws RegraDeNegocioException {
        String email = getLoggedUser().getEmail();
        mudarSenha(senha, idRoleRec, email);
    }

    public UsuarioEntity mudarSenha(String senha, Integer idRoleRec, String email) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = this.findByEmail(email);
        UsuarioEntity usuarioAtualizado = removerCargo(usuarioEntity, idRoleRec);
        String senhaNova = passwordEncoder.encode(senha);
        usuarioEntity.setSenha(senhaNova);
        return usuarioRepository.save(usuarioEntity);
    }

    public UsuarioEntity removerCargo(UsuarioEntity usuario, Integer idCargo) throws RegraDeNegocioException {
        CargoEntity cargoRemovido = cargoService.findById(idCargo);
        usuario.getCargos().stream()
                .filter(cargo -> cargo.getIdCargo() == cargoRemovido.getIdCargo())
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Senha já foi alterada!"));
        usuario.getCargos().remove(cargoRemovido);
        return usuario;
    }

    public void desativarUsuario(UsuarioEntity usuario) {
        usuario.setAtivo('N');
        usuarioRepository.save(usuario);
    }
}
