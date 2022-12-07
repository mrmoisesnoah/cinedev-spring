package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteCreateDTO;
import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteDTO;
import br.com.dbc.vemser.cinedev.dto.relatorios.RelatorioCadastroIngressoClienteDTO;
import br.com.dbc.vemser.cinedev.entity.ClienteEntity;
import br.com.dbc.vemser.cinedev.entity.UsuarioEntity;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.ClienteRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceTest {
    @InjectMocks
    private ClienteService clienteService;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private EmailService emailService;
    @Mock
    private LogService logService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(clienteService, "objectMapper", objectMapper);
    }

    @Test
    public void DeveRetornaClienteQuandoIdEstiverCadastradoNoBancoComSucesso() throws RegraDeNegocioException {
        final int ID_CLIENTE = 1;
        ClienteEntity clienteEsperado = new ClienteEntity();
        clienteEsperado.setIdCliente(ID_CLIENTE);

        when(clienteRepository.findById(ID_CLIENTE)).thenReturn(Optional.of(clienteEsperado));
        ClienteEntity clienteResponse = clienteService.findById(ID_CLIENTE);

        assertEquals(ID_CLIENTE, clienteResponse.getIdCliente());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void DeveRetornarUmaExceptionQuandoIdNaoEstiverCadastradoNoBanco() throws RegraDeNegocioException {
        final int ID_CLIENTE = 1;
        when(clienteRepository.findById(ID_CLIENTE)).thenReturn(Optional.empty());
        clienteService.findById(ID_CLIENTE);
    }

    @Test
    public void DeveConverterCorretamenteClienteEntityParaClienteDTO() {
        ClienteEntity cliente = getClienteEntity();
        ClienteDTO clienteDTOResponse = clienteService.converterParaClienteDTO(cliente);

        assertEquals(cliente.getIdCliente(), clienteDTOResponse.getIdCliente());
        assertEquals(cliente.getPrimeiroNome(), clienteDTOResponse.getPrimeiroNome());
        assertEquals(cliente.getUltimoNome(), clienteDTOResponse.getUltimoNome());
        assertEquals(cliente.getCpf(), clienteDTOResponse.getCpf());
        assertEquals(cliente.getDataNascimento(), clienteDTOResponse.getDataNascimento());
        assertEquals(cliente.getUsuario().getEmail(), clienteDTOResponse.getEmail());
    }

    @Test
    public void DeveRetornarListaDeClienteDTOCorretamente() {
        ClienteEntity cliente = getClienteEntity();

        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        List<ClienteDTO> clienteDTOList = clienteService.listarTodosClientes();

        assertEquals(1, clienteDTOList.size());
    }

    @Test
    public void DeveRetornarClienteEntityQuandoInformadoIdUsuarioCorretamente() throws RegraDeNegocioException {
        final int idUsuario = 1;
        ClienteEntity cliente = getClienteEntity();

        when(usuarioService.getIdLoggedUser()).thenReturn(idUsuario);
        when(clienteRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.of(cliente));
        ClienteEntity clienteRetornado = clienteService.listarClientePorUsuario();

        assertEquals(cliente, clienteRetornado);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void DeveRetornarUmaExcecaoQuandoIdUsuarioInformadoNaoEstiverCadastradoNoBanco() throws RegraDeNegocioException {
        final int idUsuario = 1;
        ClienteEntity cliente = getClienteEntity();

        when(usuarioService.getIdLoggedUser()).thenReturn(idUsuario);
        when(clienteRepository.findByIdUsuario(idUsuario)).thenReturn(Optional.empty());
        clienteService.listarClientePorUsuario();
    }

    @Test
    public void DeveRetornarClienteDTOCorretamenteQuandoIdClienteCadastradoNoBancoComSucesso() throws RegraDeNegocioException {
        final int idCliente = 1;
        ClienteEntity cliente = getClienteEntity();

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        ClienteDTO clienteDTO = clienteService.listarClienteDTOPeloId(idCliente);

        assertEquals(cliente.getIdCliente(), clienteDTO.getIdCliente());
        assertEquals(cliente.getPrimeiroNome(), clienteDTO.getPrimeiroNome());
        assertEquals(cliente.getUltimoNome(), clienteDTO.getUltimoNome());
        assertEquals(cliente.getCpf(), clienteDTO.getCpf());
        assertEquals(cliente.getDataNascimento(), clienteDTO.getDataNascimento());
        assertEquals(cliente.getUsuario().getEmail(), clienteDTO.getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void DeveRetornarUmaExcecaoQuandoIdClienteNaoEstivercadatradoNoBanco() throws RegraDeNegocioException {
        final int idCliente = 1;
        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());
        clienteService.listarClienteDTOPeloId(idCliente);
    }

    @Test
    public void DeveCadastrarClienteCorretamente() throws RegraDeNegocioException {
        ClienteCreateDTO clienteCreateDTO = getClienteCreateDTO();
        UsuarioEntity usuarioEsperado = getUsuarioEntity();
        usuarioEsperado.setIdUsuario(1);
        ClienteEntity cliente = getClienteEntity();
        cliente.setIdCliente(1);

        when(clienteRepository.findByCpf(clienteCreateDTO.getCpf())).thenReturn(Optional.empty());
        when(usuarioService.findOptionalByEmail(clienteCreateDTO.getEmail())).thenReturn(Optional.empty());
        when(usuarioService.cadastrarUsuario(clienteCreateDTO.getEmail(), clienteCreateDTO.getSenha(), 2))
                .thenReturn(usuarioEsperado);
        when(clienteRepository.save(any())).thenReturn(cliente);
        ClienteDTO clienteResponse = clienteService.cadastrarCliente(clienteCreateDTO);

        verify(logService).salvarLog(any());

        assertEquals(1, clienteResponse.getIdCliente());
        assertEquals(clienteCreateDTO.getPrimeiroNome(), clienteResponse.getPrimeiroNome());
        assertEquals(clienteCreateDTO.getUltimoNome(), clienteResponse.getUltimoNome());
        assertEquals(clienteCreateDTO.getCpf(), clienteResponse.getCpf());
        assertEquals(clienteCreateDTO.getDataNascimento(), clienteResponse.getDataNascimento());
        assertEquals(clienteCreateDTO.getEmail(), clienteResponse.getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void DeveRetornarUmaExcecaoQuandoClienteJaCadastradoComOmesmoCPF() throws RegraDeNegocioException {
        final String cpf = "12345678900";

        ClienteCreateDTO clienteCreateDTO = new ClienteCreateDTO();
        clienteCreateDTO.setCpf(cpf);

        ClienteEntity cliente = getClienteEntity();

        when(clienteRepository.findByCpf(cpf)).thenReturn(Optional.of(cliente));
        clienteService.cadastrarCliente(clienteCreateDTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void DeveRetornarUmaExcecaoQuandoClienteJaCadstradoComOmesmoEmail() throws RegraDeNegocioException {
        final String email = "vinicius@teste.com.br";

        ClienteCreateDTO clienteCreateDTO = new ClienteCreateDTO();
        clienteCreateDTO.setEmail(email);

        UsuarioEntity usuario = getUsuarioEntity();

        when(usuarioService.findOptionalByEmail(email)).thenReturn(Optional.of(usuario));
        clienteService.cadastrarCliente(clienteCreateDTO);
    }

    @Test
    public void DeveAtualizaClienteLogadoMetodoComunicaoComAtualizarClienteEListarClienteUsuarioComSucesso() throws RegraDeNegocioException {
        final int idCliente = 1;
        ClienteCreateDTO clienteCreateDTO = getClienteCreateDTO();
        ClienteEntity cliente = getClienteEntity();

        when(usuarioService.getIdLoggedUser()).thenReturn(idCliente);
        when(clienteRepository.findByIdUsuario(idCliente)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any())).thenReturn(cliente);
        clienteService.atualizarClienteLogado(clienteCreateDTO);

        verify(usuarioService).getIdLoggedUser();
    }

    @Test
    public void DeveAtualizarClienteCorretamente() throws RegraDeNegocioException {
        final int idCliente = 1;
        final String cpf = "98765432100";
        final String email = "vinicius@gmail.com";

        ClienteEntity cliente = getClienteEntity();
        cliente.setIdCliente(idCliente);

        ClienteCreateDTO clienteCreateDTO = getClienteCreateDTO();
        clienteCreateDTO.setEmail(email);
        clienteCreateDTO.setCpf(cpf);

        ClienteEntity clienteAtualizado = getClienteEntity();
        clienteAtualizado.setIdCliente(idCliente);
        clienteAtualizado.getUsuario().setEmail(email);
        clienteAtualizado.setCpf(cpf);

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any())).thenReturn(clienteAtualizado);
        ClienteDTO clienteDTO = clienteService.atualizarCliente(idCliente, clienteCreateDTO);

        assertEquals(idCliente, clienteDTO.getIdCliente());
        assertEquals(cpf, clienteDTO.getCpf());
        assertEquals(email, clienteDTO.getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void DeveRetornarExcecaoQuandoAtualizaClienteQuandoIdNaoEstiverCadastradoNoBanco() throws RegraDeNegocioException {
        final int idCliente = 1;
        ClienteCreateDTO clienteCreateDTO = getClienteCreateDTO();

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());
        clienteService.atualizarCliente(idCliente, clienteCreateDTO);
    }

    @Test
    public void DeveDeletarClienteLogadoCorretamente() throws RegraDeNegocioException {
        final int idCliente = 1;
        ClienteEntity cliente = getClienteEntity();

        when(usuarioService.getIdLoggedUser()).thenReturn(idCliente);
        when(clienteRepository.findByIdUsuario(idCliente)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));

        clienteService.deletarClienteLogado();
        verify(usuarioService).getIdLoggedUser();
    }

    @Test
    public void DeveDeletarUsuarioClienteUtilizaMetodosUsuarioServiceEClienteRepositoryComSucesso() throws RegraDeNegocioException {
        final int idCliente = 1;
        ClienteEntity cliente = getClienteEntity();
        cliente.setIdCliente(idCliente);

        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        clienteService.deletarUsuarioCliente(idCliente);
        verify(clienteRepository).deleteById(idCliente);
        verify(usuarioService).desativarUsuario(any());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void DeveRetornarExcecaoQuandoIdClienteNaoEncontradoNoBanco() throws RegraDeNegocioException {
        final int idCliente = 1;
        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());
        clienteService.deletarUsuarioCliente(idCliente);
    }

    @Test
    public void DeveRetornarListaRelatorioPersonalizadaCorretamente() {
        final int idCliente = 1;
        RelatorioCadastroIngressoClienteDTO relatorioCadastroIngressoClienteDTO = new RelatorioCadastroIngressoClienteDTO();

        when(clienteRepository.listarRelatorioPersonalizado(idCliente)).thenReturn(List.of(relatorioCadastroIngressoClienteDTO));
        List<RelatorioCadastroIngressoClienteDTO> list = clienteService.listarRelatorioPersonalizado(idCliente);

        assertNotNull(list);
        assertEquals(1, list.size());
    }

    private static ClienteCreateDTO getClienteCreateDTO() {
        final String email = "vinicius@teste.com.br";
        final String cpf = "12345678900";
        final String primeiroNome = "Vinicius";
        final String ultimoNome = "Assis";
        final LocalDate dataNascimento = LocalDate.of(2000, 04, 22);
        final String senha = "123";

        ClienteCreateDTO clienteCreateDTO = new ClienteCreateDTO(primeiroNome, ultimoNome, cpf, dataNascimento);
        clienteCreateDTO.setEmail(email);
        clienteCreateDTO.setSenha(senha);

        return clienteCreateDTO;
    }

    private static UsuarioEntity getUsuarioEntity() {
        final String email = "vinicius@teste.com.br";
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(1);
        usuario.setEmail(email);

        return usuario;
    }

    private static ClienteEntity getClienteEntity() {
        UsuarioEntity usuario = getUsuarioEntity();
        return new ClienteEntity(1, 1, "Vinicius", "Assis",
                "12345678900", LocalDate.of(2000, 04, 22), "S", Set.of(), usuario);
    }
}
