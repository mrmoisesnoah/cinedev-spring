package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.dto.ingressodto.IngressoCompradoDTO;
import br.com.dbc.vemser.cinedev.dto.ingressodto.IngressoCreateDTO;
import br.com.dbc.vemser.cinedev.dto.ingressodto.IngressoDTO;
import br.com.dbc.vemser.cinedev.dto.paginacaodto.PageDTO;
import br.com.dbc.vemser.cinedev.dto.relatorios.RelatorioCadastroIngressoClienteDTO;
import br.com.dbc.vemser.cinedev.entity.*;
import br.com.dbc.vemser.cinedev.entity.enums.Disponibilidade;
import br.com.dbc.vemser.cinedev.entity.enums.Idioma;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.IngressoRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IngressoServiceTest {
    @InjectMocks
    private IngressoService ingressoService;
    @Mock
    private IngressoRepository ingressoRepository;
    @Mock
    private CinemaService cinemaService;
    @Mock
    private FilmeService filmeService;
    @Mock
    private ClienteService clienteService;
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
        ReflectionTestUtils.setField(ingressoService, "objectMapper", objectMapper);
    }

    @Test
    public void deveRetornaIngressoCorretamenteQuandoIdIngressoCadastradoNoBanco() throws RegraDeNegocioException {
        final int idIngresso = 1;
        IngressoEntity ingressoEntity = getIngressoEntity();

        when(ingressoRepository.findById(idIngresso)).thenReturn(Optional.of(ingressoEntity));
        IngressoEntity ingressoResponse = ingressoService.findById(idIngresso);

        assertEquals(idIngresso, ingressoResponse.getIdIngresso());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornaUmaExcecaoQuandoBuscarIdIngressoNaoCadastradoNoBanco() throws RegraDeNegocioException {
        final int idIngresso = 1;

        when(ingressoRepository.findById(idIngresso)).thenReturn(Optional.empty());
        ingressoService.findById(idIngresso);
    }

    @Test
    public void deveConverteCorretamenteIngressoEntityParaIngressoDTO() throws RegraDeNegocioException {
        final int idIngresso = 1;
        IngressoEntity ingressoEntity = getIngressoEntity();

        when(ingressoRepository.findById(idIngresso)).thenReturn(Optional.of(ingressoEntity));
        IngressoDTO ingressoDTO = ingressoService.ingressoDTOporId(idIngresso);

        assertEquals(ingressoEntity.getIdIngresso(), ingressoDTO.getIdIngresso());
        assertEquals(ingressoEntity.getIdFilme(), ingressoDTO.getIdFilme());
        assertEquals(ingressoEntity.getIdCinema(), ingressoDTO.getIdCinema());
        assertEquals(ingressoEntity.getPreco(), ingressoDTO.getPreco());
        assertEquals(ingressoEntity.getDataHora(), ingressoDTO.getDataHora());
        assertEquals(ingressoEntity.getDisponibilidade(), ingressoDTO.getDisponibilidade());
        assertEquals(ingressoEntity.getAtivo(), ingressoDTO.getAtivo());
    }

    @Test
    public void deveCriarIngressoCorretamente() throws RegraDeNegocioException {
        final int idFilme = 1;
        final int idCinema = 1;

        FilmeEntity filme = getFilmeEntity();
        CinemaEntity cinema = getCinemaEntity();

        IngressoCreateDTO ingressoCreateDTO = getIngressoCreateDTO();
        IngressoEntity ingressoEntity = getIngressoEntity();

        when(filmeService.listarPeloId(idFilme)).thenReturn(filme);
        when(cinemaService.findById(idCinema)).thenReturn(cinema);
        when(ingressoRepository.save(any())).thenReturn(ingressoEntity);
        IngressoDTO ingressoDTO = ingressoService.createIngresso(ingressoCreateDTO);

        assertEquals(ingressoEntity.getIdIngresso(), ingressoDTO.getIdIngresso());
        assertEquals(ingressoEntity.getIdFilme(), ingressoDTO.getIdFilme());
        assertEquals(ingressoEntity.getIdCinema(), ingressoDTO.getIdCinema());
        assertEquals(ingressoEntity.getPreco(), ingressoDTO.getPreco());
        assertEquals(ingressoEntity.getDataHora(), ingressoDTO.getDataHora());
        assertEquals(ingressoEntity.getDisponibilidade(), ingressoDTO.getDisponibilidade());
        assertEquals(ingressoEntity.getAtivo(), ingressoDTO.getAtivo());
    }

//    @Test
//    public void deveComprarIngressoCorretamenteQuandoClienteEIngressoExistem() throws RegraDeNegocioException {
//        final int idIngresso = 1;
//        final int idCliente = 1;
//
//        IngressoEntity ingressoEntity = getIngressoEntity();
//        ClienteEntity clienteEntity = getClienteEntity();
//
//        IngressoEntity ingressoSalvo = getIngressoEntity();
//        ingressoSalvo.setCliente(clienteEntity);
//        ingressoSalvo.setIdCliente(clienteEntity.getIdCliente());
//        ingressoSalvo.setDisponibilidade(Disponibilidade.N);
//        ingressoSalvo.setPreco(30.00);
//
//        when(clienteService.findById(idCliente)).thenReturn(clienteEntity);
//        when(ingressoRepository.findById(idIngresso)).thenReturn(Optional.of(ingressoEntity));
//        when(ingressoRepository.save(any())).thenReturn(ingressoSalvo);
//        IngressoCompradoDTO ingressoCompradoDTO = ingressoService.comprarIngresso(idCliente, idIngresso);
//
//        verify(logService).salvarLog(any());
//
//        assertEquals(ingressoSalvo.getIdCliente(), ingressoCompradoDTO.getIdCliente());
//        assertEquals(ingressoSalvo.getCliente().getPrimeiroNome(), ingressoCompradoDTO.getNomeCliente());
//        assertEquals(Disponibilidade.N, ingressoCompradoDTO.getDisponibilidade());
//    }

    @Test
    public void deveUtilizarIngressoRepositoryDeleteByIdquandoDeletarIngresso() {
        final int idIngresso = 1;

        ingressoService.removeIngresso(idIngresso);
        verify(ingressoRepository).deleteById(idIngresso);
    }

    @Test
    public void deveListarIngressosPaginadosCorretamente() {
        final int numeroPagina = 0;
        final int tamanho = 3;

        IngressoEntity ingresso = getIngressoEntity();
        PageImpl<IngressoEntity> listaPaginada = new PageImpl<>(List.of(ingresso), PageRequest.of(numeroPagina, tamanho), 0);

        when(ingressoRepository.findAll(any(Pageable.class))).thenReturn(listaPaginada);
        PageDTO<IngressoDTO> ingressoDTOPageDTO = ingressoService.listIngressoPaginas(numeroPagina, tamanho);

        assertNotNull(ingressoDTOPageDTO);
        assertEquals(1, ingressoDTOPageDTO.getTotalElementos());
        assertEquals(1, ingressoDTOPageDTO.getQuantidadePaginas());
        assertEquals(listaPaginada.getPageable().getPageNumber(), ingressoDTOPageDTO.getPagina());
    }

    @Test
    public void deveListarIngressosDisponiveisCorretamente() {
        IngressoEntity ingresso = getIngressoEntity();

        when(ingressoRepository.findIngressoDisponiveis()).thenReturn(List.of(ingresso));
        List<IngressoDTO> ingressoDTOList = ingressoService.listarIngressos();

        assertEquals(1, ingressoDTOList.size());
    }

    @Test
    public void deveListarIngressosCompradosCorretamente() {
        ClienteEntity cliente = getClienteEntity();
        IngressoEntity ingresso = getIngressoEntity();
        ingresso.setCliente(cliente);
        ingresso.setDisponibilidade(Disponibilidade.N);

        when(ingressoRepository.findIngressoComprados()).thenReturn(List.of(ingresso));
        List<IngressoDTO> ingressoDTOList = ingressoService.listarIngressosComprados();

        assertEquals(1, ingressoDTOList.size());
    }

    @Test
    public void deveListarIngressosPorClienteCorretamente() {
       final int idPessoa = 1;
       RelatorioCadastroIngressoClienteDTO relatorioCadastroIngressoClienteDTO = getRelatorioCadastroIngressoClienteDTO();

       when(clienteService.listarRelatorioPersonalizado(idPessoa)).
               thenReturn(List.of(relatorioCadastroIngressoClienteDTO));

       List<RelatorioCadastroIngressoClienteDTO> relatorio = ingressoService
               .listarIngressosCompradosPorCliente(idPessoa);

       assertEquals(1, relatorio.size());
    }

    @Test
    public void deveListarIngressosDoClienteLogadoCorretamente() throws RegraDeNegocioException {
       final int idPessoa = 1;
       RelatorioCadastroIngressoClienteDTO relatorioCadastroIngressoClienteDTO = getRelatorioCadastroIngressoClienteDTO();
       ClienteEntity cliente = getClienteEntity();

       when(clienteService.listarClientePorUsuario()).thenReturn(cliente);
       when(clienteService.listarRelatorioPersonalizado(idPessoa)).
               thenReturn(List.of(relatorioCadastroIngressoClienteDTO));

       List<RelatorioCadastroIngressoClienteDTO> relatorio = ingressoService
               .listarIngressosDoClienteLogado();

       assertEquals(1, relatorio.size());
    }

    private static IngressoEntity getIngressoEntity() {
        CinemaEntity cinema = getCinemaEntity();
        FilmeEntity filme = getFilmeEntity();

        IngressoEntity ingressoEntity = new IngressoEntity();
        ingressoEntity.setIdIngresso(1);
        ingressoEntity.setIdFilme(1);
        ingressoEntity.setIdCinema(1);
        ingressoEntity.setPreco(30.0);
        ingressoEntity.setDataHora(LocalDateTime.now().plusDays(5));
        ingressoEntity.setCinema(cinema);
        ingressoEntity.setFilme(filme);

        return ingressoEntity;
    }

    private static IngressoCreateDTO getIngressoCreateDTO() {
        IngressoCreateDTO ingressoCreateDTO = new IngressoCreateDTO();
        ingressoCreateDTO.setIdCinema(1);
        ingressoCreateDTO.setIdFilme(1);
        ingressoCreateDTO.setPreco(30.0);
        ingressoCreateDTO.setDataHora(LocalDateTime.now().plusDays(5));
        ingressoCreateDTO.setDisponibilidade(Disponibilidade.S);
        ingressoCreateDTO.setAtivo("S");

        return ingressoCreateDTO;
    }

    private static FilmeEntity getFilmeEntity() {
        return new FilmeEntity(1, "Pantera Negra: Wakanda Para Sempre", Idioma.DUBLADO,
                12, 161, "S", Set.of());
    }

    private static UsuarioEntity getUsuario() {
        final String email = "cinemark_canoas@teste.com.br";
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(1);
        usuario.setEmail(email);

        return usuario;
    }

    private static CinemaEntity getCinemaEntity() {
        UsuarioEntity usuario = getUsuario();
        return new CinemaEntity(1, 1, "Cinemark Canoas", "RS",
                "Canoas", "S", Set.of(), usuario);
    }

    private static ClienteEntity getClienteEntity() {
        UsuarioEntity usuario = getUsuario();
        usuario.setIdUsuario(2);
        return new ClienteEntity(1, 1, "Vinicius", "Assis",
                "12345678900", LocalDate.of(2000, 04, 22), "S", Set.of(), usuario);
    }

    private static RelatorioCadastroIngressoClienteDTO getRelatorioCadastroIngressoClienteDTO() {
        return new RelatorioCadastroIngressoClienteDTO(1, "Vinicius", "Assis",
                "12345678900", LocalDate.of(2000, 04, 22), "vinicius@test.com.br",
                1, 1, "Pantera Negra: Wakanda Para Sempre", 1, LocalDateTime.now().plusDays(3),
                "Cinemark Canoas", "RS", "Canoas", "S");
    }
}
