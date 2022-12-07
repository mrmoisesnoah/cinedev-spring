package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.dto.cinemadto.CinemaCreateDTO;
import br.com.dbc.vemser.cinedev.dto.cinemadto.CinemaDTO;
import br.com.dbc.vemser.cinedev.dto.paginacaodto.PageDTO;
import br.com.dbc.vemser.cinedev.dto.relatorios.RelatorioCadastroCinemaFilmeDTO;
import br.com.dbc.vemser.cinedev.entity.CinemaEntity;
import br.com.dbc.vemser.cinedev.entity.UsuarioEntity;
import br.com.dbc.vemser.cinedev.entity.enums.Disponibilidade;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.CinemaRepository;
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
public class CinemaServiceTest {

    @InjectMocks
    private CinemaService cinemaService;
    @Mock
    private CinemaRepository cinemaRepository;
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
        ReflectionTestUtils.setField(cinemaService, "objectMapper", objectMapper);
    }

    @Test
    public void deveRetornaCinemaQuandoIdEstiverCadastradoNoBancoComSucesso() throws RegraDeNegocioException {
        final int ID_CINEMA = 1;
        CinemaEntity cinemaEsperado = new CinemaEntity();
        cinemaEsperado.setIdCinema(ID_CINEMA);

        when(cinemaRepository.findById(ID_CINEMA)).thenReturn(Optional.of(cinemaEsperado));
        CinemaEntity cinemaResponse = cinemaService.findById(ID_CINEMA);

        assertEquals(ID_CINEMA, cinemaResponse.getIdCinema());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornaUmaExceptionQuandoIdNaoEstiverCadastradoNoBancoComSucesso() throws RegraDeNegocioException {
        final int ID_CINEMA = 1;
        when(cinemaRepository.findById(ID_CINEMA)).thenReturn(Optional.empty());
        cinemaService.findById(ID_CINEMA);
    }

    @Test
    public void deveRetornaCinemaPorUsuarioQuandoUsuarioEstiverCadastradoNoBancoComSucesso() throws RegraDeNegocioException {
        final int ID_USUARIO = 1;
        CinemaEntity cinemaEsperado = new CinemaEntity();
        cinemaEsperado.setIdCinema(ID_USUARIO);

        when(cinemaRepository.findByIdUsuario(ID_USUARIO)).thenReturn(Optional.of(cinemaEsperado));
        CinemaEntity cinemaResponse = cinemaService.listarCinemaIdUsuario(ID_USUARIO);

        assertEquals(ID_USUARIO, cinemaResponse.getIdCinema());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExceptionQuandoIdUsuarioNaoEstiverCadastradoNoBancoComSucesso() throws RegraDeNegocioException {
        final int ID_USUARIO = 1;
        when(cinemaRepository.findByIdUsuario(ID_USUARIO)).thenReturn(Optional.empty());
        cinemaService.listarCinemaIdUsuario(ID_USUARIO);
    }

    @Test
    public void deveConverterCorretamenteCinemaEntityParaCinemaDTOComSucesso() {
        CinemaEntity cinema = getCinemaEntity();
        CinemaDTO cinemaDTOResponse = cinemaService.converterParaCinemaDTO(cinema);

        assertEquals(cinema.getIdCinema(), cinemaDTOResponse.getIdCinema());
        assertEquals(cinema.getNome(), cinemaDTOResponse.getNome());
        assertEquals(cinema.getEstado(), cinemaDTOResponse.getEstado());
        assertEquals(cinema.getCidade(), cinemaDTOResponse.getCidade());
        assertEquals(cinema.getUsuario().getEmail(), cinemaDTOResponse.getEmail());
    }

    @Test
    public void retornaListaDeCinemaDTOCorretamenteComSucesso() {
        CinemaEntity cinema = getCinemaEntity();

        when(cinemaRepository.findAll()).thenReturn(List.of(cinema));
        List<CinemaDTO> cinemaDTOList = cinemaService.listarCinema();

        assertEquals(1, cinemaDTOList.size());
    }

    @Test
    public void DeveRetornarCinemaDTOCorretamenteQuandoIdCinemaCadastradoNoBancoComSucesso() throws RegraDeNegocioException {
        final int idCinema = 1;
        CinemaEntity cinema = getCinemaEntity();

        when(cinemaRepository.findById(idCinema)).thenReturn(Optional.of(cinema));
        CinemaDTO cinemaDTO = cinemaService.listarCinemaPorId(idCinema);

        assertEquals(cinema.getIdCinema(), cinemaDTO.getIdCinema());
        assertEquals(cinema.getNome(), cinemaDTO.getNome());
        assertEquals(cinema.getEstado(), cinemaDTO.getEstado());
        assertEquals(cinema.getCidade(), cinemaDTO.getCidade());
        assertEquals(cinema.getUsuario().getEmail(), cinemaDTO.getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void DeveRetornarUmaExcecaoQuandoIdCinemaNaoEstivercadatradoNoBanco() throws RegraDeNegocioException {
        final int idCinema = 1;
        when(cinemaRepository.findById(idCinema)).thenReturn(Optional.empty());
        cinemaService.listarCinemaPorId(idCinema);
    }

    @Test
    public void DeveCadastrarUmCinemaCorretamenteComSucesso() throws RegraDeNegocioException {
        CinemaCreateDTO cinemaCreateDTO = getCinemaCreateDTO();
        UsuarioEntity usuarioEsperado = getUsuarioEntity();
        usuarioEsperado.setIdUsuario(1);
        CinemaEntity cinema = getCinemaEntity();
        cinema.setIdCinema(1);

        when(cinemaRepository.findByNome(cinemaCreateDTO.getNome())).thenReturn(Optional.empty());
        when(usuarioService.cadastrarUsuario(cinemaCreateDTO.getEmail(), cinemaCreateDTO.getSenha(), 3))
                .thenReturn(usuarioEsperado);
        when(cinemaRepository.save(any())).thenReturn(cinema);
        CinemaDTO cinemaResponse = cinemaService.adicionarCinema(cinemaCreateDTO);

        verify(logService).salvarLog(any());

        assertEquals(1, cinemaResponse.getIdCinema());
        assertEquals(cinemaCreateDTO.getNome(), cinemaResponse.getNome());
        assertEquals(cinemaCreateDTO.getEstado(), cinemaResponse.getEstado());
        assertEquals(cinemaCreateDTO.getCidade(), cinemaResponse.getCidade());
        assertEquals(cinemaCreateDTO.getEmail(), cinemaResponse.getEmail());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void DeveRetornarUmaExcecaoQuandoCinemaJaCadastradoComOmesmoNomeComSucesso() throws RegraDeNegocioException {
        final String nome = "cinemark";

        CinemaCreateDTO cinemaCreateDTO = new CinemaCreateDTO();
        cinemaCreateDTO.setNome(nome);

        CinemaEntity cinema = getCinemaEntity();

        when(cinemaRepository.findByNome(nome)).thenReturn(Optional.of(cinema));
        cinemaService.adicionarCinema(cinemaCreateDTO);
    }

    @Test
    public void DeveAtualizarDadosDoCinemaUsandoMetodosAtualizarCinemaEAtualizarCinemaLogadoComSucesso() throws RegraDeNegocioException {
        final int idCinema = 1;
        CinemaCreateDTO cinemaCreateDTO = getCinemaCreateDTO();
        CinemaEntity cinema = getCinemaEntity();

        when(usuarioService.getIdLoggedUser()).thenReturn(idCinema);
        when(cinemaRepository.findByIdUsuario(idCinema)).thenReturn(Optional.of(cinema));
        when(cinemaRepository.save(any())).thenReturn(cinema);
        cinemaService.atualizarCinemaLogado(cinemaCreateDTO);

        verify(usuarioService).getIdLoggedUser();
    }

    @Test
    public void DeveAtualizarCinemaComDadosCorretosComSucesso() throws RegraDeNegocioException {
        final int idCinema = 1;
        final String nome = "cinemark";
        final String cidade = "Ceilandia";

        CinemaEntity cinema = getCinemaEntity();
        cinema.setIdCinema(idCinema);

        CinemaCreateDTO cinemaCreateDTO = getCinemaCreateDTO();
        cinemaCreateDTO.setCidade(cidade);
        cinemaCreateDTO.setNome(nome);

        CinemaEntity cinemaAtualizado = getCinemaEntity();
        cinemaAtualizado.setIdCinema(idCinema);
        cinemaAtualizado.setCidade(cidade);
        cinemaAtualizado.setNome(nome);

        when(cinemaRepository.findByIdUsuario(idCinema)).thenReturn(Optional.of(cinema));
        when(cinemaRepository.save(any())).thenReturn(cinemaAtualizado);
        CinemaDTO cinemaDTO = cinemaService.atualizarCinema(idCinema, cinemaCreateDTO);

        assertEquals(idCinema, cinemaDTO.getIdCinema());
        assertEquals(nome, cinemaDTO.getNome());
        assertEquals(cidade, cinemaDTO.getCidade());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void DeveRetornarUmaExcecaoQuandoAtualizarCinemaQuandoIdNaoCadastradoNoBanco() throws RegraDeNegocioException {
        final int idCinema = 1;
        CinemaCreateDTO cinemaCreateDTO = getCinemaCreateDTO();

        when(cinemaRepository.findByIdUsuario(idCinema)).thenReturn(Optional.empty());
        cinemaService.atualizarCinema(idCinema, cinemaCreateDTO);
    }

    @Test
    public void DeveDeletarCinemaLogadoDeFormaCorretaComSucesso() throws RegraDeNegocioException {
        final int idCinema = 1;
        CinemaEntity cinema = getCinemaEntity();

        when(usuarioService.getIdLoggedUser()).thenReturn(idCinema);
        when(cinemaRepository.findByIdUsuario(idCinema)).thenReturn(Optional.of(cinema));
        when(cinemaRepository.findById(idCinema)).thenReturn(Optional.of(cinema));

        cinemaService.deletarCinemaLogado();
        verify(usuarioService).getIdLoggedUser();
    }

    @Test
    public void DeveDeletarUsuarioCinemaUtilizaMetodosUsuarioServiceECinemaRepositoryComSucesso() throws RegraDeNegocioException {
        final int idCinema = 1;
        CinemaEntity cinema = getCinemaEntity();
        cinema.setIdCinema(idCinema);

        when(cinemaRepository.findById(idCinema)).thenReturn(Optional.of(cinema));
        cinemaService.deletarCinemaPorUsuario(idCinema);
        verify(cinemaRepository).delete(cinema);
        verify(usuarioService).desativarUsuario(any());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void DeveRetornaUmaExcecaoQuandoIdClienteNaoEncontradoNoBanco() throws RegraDeNegocioException {
        final int idCinema = 1;
        when(cinemaRepository.findById(idCinema)).thenReturn(Optional.empty());
        cinemaService.deletarCinemaPorUsuario(idCinema);
    }

    @Test
    public void DeveRetornaListaRelatorioPersonalizadaCorretamente() throws RegraDeNegocioException {
        final int idCinema = 1;
        RelatorioCadastroCinemaFilmeDTO relatorioCadastroCinemaFilmeDTO = getRelatorioCadastroCinemaFilmeDTO();
        CinemaEntity cinema = getCinemaEntity();

        when(usuarioService.getIdLoggedUser()).thenReturn(idCinema);
        when(cinemaRepository.findByIdUsuario(idCinema)).thenReturn(Optional.of(cinema));
        when(cinemaRepository.listarRelatorioPersonalizado(idCinema)).thenReturn(List.of(relatorioCadastroCinemaFilmeDTO));
        List<RelatorioCadastroCinemaFilmeDTO> list = cinemaService.listarRelatorioPersonalizadoCinemaLogado();

        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    public void DeveRetornaRelatorioPersonalizadoComSucesso() {
        RelatorioCadastroCinemaFilmeDTO relatorioCadastroCinemaFilmeDTO = getRelatorioCadastroCinemaFilmeDTO();
        final int idCinema = 1;

        when(cinemaRepository.listarRelatorioPersonalizado(idCinema)).thenReturn(List.of(relatorioCadastroCinemaFilmeDTO));
        List<RelatorioCadastroCinemaFilmeDTO> list = cinemaService.listarRelatorioPersonalizado(idCinema);

        assertNotNull(list);
        assertEquals(1, list.size());

    }

    @Test
    public void DeveListarCinemaPaginadoCorretamente() {
        final int numeroPagina = 0;
        final int tamanho = 3;

        CinemaEntity cinema = getCinemaEntity();
        PageImpl<CinemaEntity> listaPaginada = new PageImpl<>(List.of(cinema), PageRequest.of(numeroPagina, tamanho), 0);

        when(cinemaRepository.findAll(any(Pageable.class))).thenReturn(listaPaginada);
        PageDTO<CinemaDTO> cinemaDTOPageDTO = cinemaService.listCinemaPaginado(numeroPagina, tamanho);

        assertNotNull(cinemaDTOPageDTO);
        assertEquals(1, cinemaDTOPageDTO.getTotalElementos());
        assertEquals(1, cinemaDTOPageDTO.getQuantidadePaginas());
        assertEquals(listaPaginada.getPageable().getPageNumber(), cinemaDTOPageDTO.getPagina());
    }

    private static RelatorioCadastroCinemaFilmeDTO getRelatorioCadastroCinemaFilmeDTO() {
        return new RelatorioCadastroCinemaFilmeDTO(1, "cinemark", "Taguatinga", 1,
                "Shrek 2", 13, 120, 1, 30.0,
                LocalDateTime.now().plusDays(3), Disponibilidade.S, "S");
    }

    private static CinemaCreateDTO getCinemaCreateDTO() {
        final String nome = "Cinemark";
        final String estado = "DF";
        final String cidade = "Taguatinga";

        final String email = "cinema@teste.com.br";
        final String senha = "123";

        CinemaCreateDTO cinemaCreateDTO = new CinemaCreateDTO(nome, estado, cidade);
        cinemaCreateDTO.setEmail(email);
        cinemaCreateDTO.setSenha(senha);

        return cinemaCreateDTO;
    }

    private static UsuarioEntity getUsuarioEntity() {
        final String email = "cinema@teste.com.br";
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setIdUsuario(1);
        usuario.setEmail(email);

        return usuario;
    }

    private static CinemaEntity getCinemaEntity() {
        UsuarioEntity usuario = getUsuarioEntity();
        return new CinemaEntity(1, 1,
                "Cinemark", "DF", "Taguatinga", "S", Set.of(), usuario);
    }

}
