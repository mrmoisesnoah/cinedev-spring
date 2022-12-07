package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.dto.UsuarioDTO;
import br.com.dbc.vemser.cinedev.dto.login.LoginDTO;
import br.com.dbc.vemser.cinedev.dto.recuperarsenhadto.RecuperarSenhaDTO;
import br.com.dbc.vemser.cinedev.entity.CargoEntity;
import br.com.dbc.vemser.cinedev.entity.UsuarioEntity;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.UsuarioRepository;
import br.com.dbc.vemser.cinedev.security.TokenService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {
    @InjectMocks
    private UsuarioService usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CargoService cargoService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenService tokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private AuthenticationManager authenticationManager;
    private ObjectMapper objectMapper = new ObjectMapper();

    final String EMAIL = "test@demo.com.br";
    final int ROLE_CLIENTE_ID = 2;
    final int ROLE_RECCLIENTE_ID = 4;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(usuarioService, "objectMapper", objectMapper);
    }

    @Test
    public void deveEncontraUsuarioQuandoEmailEstiverNoBanco() throws RegraDeNegocioException {
        UsuarioEntity usuario = getUsuarioEntity();

        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        usuarioService.findByEmail(EMAIL);

        assertEquals(EMAIL, usuario.getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveGerarUmRegraDeNegocioExceptionQuandoEmailNaoEstiverNoBanco() throws RegraDeNegocioException {
        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        usuarioService.findByEmail(EMAIL);
    }

    @Test
    public void deveUsarRepositoryMetodoFindByEmailQuandoFindOptionalByEmail() {
        usuarioService.findOptionalByEmail(EMAIL);
        verify(usuarioRepository).findByEmail(EMAIL);
    }

    @Test
    public void deveMudarSenhaTeste() throws RegraDeNegocioException {
        final String senhaNova = "1234admin";

        CargoEntity cargoRecCliente = getCargoRecCliente();

        Set<CargoEntity> cargos = new HashSet<>();
        cargos.add(cargoRecCliente);

        UsuarioEntity usuario = getUsuarioEntity();
        usuario.setCargos(cargos);

        UsuarioEntity usuarioEsperado = getUsuarioEntity();
        usuarioEsperado.setSenha(senhaNova);

        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEsperado);
        when(cargoService.findById(ROLE_RECCLIENTE_ID)).thenReturn(cargoRecCliente);
        when(passwordEncoder.encode(senhaNova)).thenReturn(senhaNova);
        UsuarioEntity usuarioAtualizado = usuarioService.mudarSenha(senhaNova, ROLE_RECCLIENTE_ID, EMAIL);

        assertEquals(senhaNova, usuarioAtualizado.getSenha());
    }

    @Test
    public void deveMudarSenhaCorretamenteUsuarioLogado() throws RegraDeNegocioException {

        final String senhaNova = "1234admin";

        CargoEntity cargoRecCliente = getCargoRecCliente();

        Set<CargoEntity> cargos = new HashSet<>();
        cargos.add(cargoRecCliente);

        UsernamePasswordAuthenticationToken dto
                = new UsernamePasswordAuthenticationToken(1, cargos, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);

        UsuarioEntity usuario = getUsuarioEntity();
        usuario.setCargos(cargos);

        UsuarioEntity usuarioEsperado = getUsuarioEntity();
        usuarioEsperado.setSenha(senhaNova);

        when(usuarioRepository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEsperado);
        when(passwordEncoder.encode(senhaNova)).thenReturn(senhaNova);
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuario));
        when(cargoService.findById(ROLE_RECCLIENTE_ID)).thenReturn(cargoRecCliente);
        usuarioService.mudarSenhaUsuarioLogado(senhaNova, ROLE_RECCLIENTE_ID);
    }

    @Test
    public void deveRemoveCargo() throws RegraDeNegocioException {
        CargoEntity cargoRecCliente = getCargoRecCliente();
        CargoEntity cargoCliente = getCargoCliente();
        UsuarioEntity usuario = getUsuarioEntity();

        Set<CargoEntity> cargos = new HashSet<>();
        cargos.add(cargoCliente);
        cargos.add(cargoRecCliente);

        usuario.setCargos(cargos);

        when(cargoService.findById(ROLE_RECCLIENTE_ID)).thenReturn(cargoRecCliente);
        UsuarioEntity usuarioAtualizado = usuarioService.removerCargo(usuario, ROLE_RECCLIENTE_ID);

        assertEquals(1, usuarioAtualizado.getCargos().size());
        assertTrue(usuarioAtualizado.getCargos().contains(cargoCliente));
        assertFalse(usuarioAtualizado.getCargos().contains(cargoRecCliente));
    }

    @Test
    public void deveDesativaUsuarioCorretamente() {
        UsuarioEntity usuario = getUsuarioEntity();
        usuarioService.desativarUsuario(usuario);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    public void deveTestarOmetodoGetIdLoggedUserComSucesso() {
        UsernamePasswordAuthenticationToken dto
                = new UsernamePasswordAuthenticationToken(1, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(dto);

        Integer id = usuarioService.getIdLoggedUser();

        assertEquals(1, id);
    }

    @Test
    public void deveTestarOmetodoGetLoggedUserCorretamente() throws RegraDeNegocioException {
        UsuarioEntity usuario = getUsuarioEntity();

        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuario));
        UsuarioEntity usuarioResponse = usuarioService.getLoggedUser();

        assertEquals(usuarioResponse.getIdUsuario(), usuarioResponse.getIdUsuario());
    }

    @Test
    public void deveCadastrarUsuarioCorretamente() throws RegraDeNegocioException {
        CargoEntity cargo = getCargoCliente();
        final String email = "test@demo.com.br";
        final String senha = "123";

        UsuarioEntity usuario = getUsuarioEntity();
        usuario.setCargos(Set.of(cargo));

        when(cargoService.findById(2)).thenReturn(cargo);
        when(passwordEncoder.encode(senha)).thenReturn(senha);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuario);
        UsuarioEntity usuarioCadatrado = usuarioService.cadastrarUsuario(email, senha, ROLE_CLIENTE_ID);

        assertEquals(1, usuarioCadatrado.getCargos().size());
        assertEquals(senha, usuarioCadatrado.getSenha());
        assertEquals(email, usuarioCadatrado.getEmail());
    }

    @Test
    public void deveCadastrarUsuarioAdministradoCorretamente() throws RegraDeNegocioException {
        final String senha = "123";

        CargoEntity cargoAdmin = new CargoEntity();
        cargoAdmin.setIdCargo(1);
        cargoAdmin.setNome("ROLE_ADMIN");

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@demo.com.br");
        loginDTO.setSenha(senha);

        UsuarioEntity usuario = getUsuarioEntity();
        usuario.setCargos(Set.of(cargoAdmin));

        when(cargoService.findById(1)).thenReturn(cargoAdmin);
        when(passwordEncoder.encode(anyString())).thenReturn(senha);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuario);

        UsuarioDTO usuarioCadastrado = usuarioService.cadastrarAdministrador(loginDTO);

        assertEquals(usuario.getIdUsuario(), usuarioCadastrado.getIdUsuario());
        assertEquals(loginDTO.getEmail(), usuarioCadastrado.getEmail());
    }

    @Test
    public void deveRecuperaSenhaCorretamente() throws RegraDeNegocioException {
        final String email = "test@demo.com.br";
        RecuperarSenhaDTO recuperarSenhaDTO = new RecuperarSenhaDTO();
        recuperarSenhaDTO.setEmail(email);

        CargoEntity cargoCliente = getCargoCliente();
        Set<CargoEntity> cargos = new HashSet<>();
        cargos.add(cargoCliente);

        UsuarioEntity usuario = getUsuarioEntity();
        usuario.setCargos(cargos);

        CargoEntity cargo = getCargoRecCliente();

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));
        when(cargoService.findById(anyInt())).thenReturn(cargo);

        usuarioService.recuperarSenha(recuperarSenhaDTO, ROLE_RECCLIENTE_ID);

        verify(emailService).sendEmail(any(), any(), any());
        verify(usuarioRepository).save(any(UsuarioEntity.class));
    }

    @Test
    public void deveAutenticarCorretamente() throws RegraDeNegocioException {
        final String senha = "123";
        final String email = "test@demo.com.br";
        final String tokenEsperado = "token";

        UsuarioEntity usuario = getUsuarioEntity();

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setSenha(senha);

        Authentication authenticate = new UsernamePasswordAuthenticationToken(usuario, senha);


        when(tokenService.getToken(any())).thenReturn(tokenEsperado);
        when(authenticationManager.authenticate(any())).thenReturn(authenticate);
//        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        String token = usuarioService.autenticar(loginDTO);

        assertEquals(tokenEsperado, token);
    }

    private static CargoEntity getCargoCliente() {
        final int ROLE_CLIENTE_ID = 2;
        CargoEntity cargoCliente = new CargoEntity();

        cargoCliente.setIdCargo(ROLE_CLIENTE_ID);
        cargoCliente.setNome("ROLE_CLIENTE");

        return cargoCliente;
    }

    private static CargoEntity getCargoRecCliente() {
        final int ROLE_RECCLIENTE_ID = 4;

        CargoEntity cargoRecCliente = new CargoEntity();

        cargoRecCliente.setIdCargo(ROLE_RECCLIENTE_ID);
        cargoRecCliente.setNome("ROLE_RECCLIENTE");

        return cargoRecCliente;
    }

    private static UsuarioEntity getUsuarioEntity() {
        final String email = "test@demo.com.br";
        final String senha = "123";

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(1);
        usuario.setEmail(email);
        usuario.setAtivo('S');
        usuario.setSenha(senha);

        return usuario;
    }
}
