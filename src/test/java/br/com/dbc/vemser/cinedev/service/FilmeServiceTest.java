package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.dto.filmedto.FilmeCreateDTO;
import br.com.dbc.vemser.cinedev.dto.filmedto.FilmeDTO;
import br.com.dbc.vemser.cinedev.entity.FilmeEntity;
import br.com.dbc.vemser.cinedev.entity.enums.Idioma;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.FilmeRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FilmeServiceTest {
    @InjectMocks
    private FilmeService filmeService;
    @Mock
    private FilmeRepository filmeRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(filmeService, "objectMapper", objectMapper);
    }

    @Test
    public void deveEditarFilmeCorretamente() throws RegraDeNegocioException {
        final int idFilme = 1;
        final int novaDuracao = 200;
        final Idioma novoIdioma = Idioma.LEGENDADO;

        FilmeEntity filme = getFilmeEntity();

        FilmeCreateDTO filmeCreateDTO = getFilmeCreateDTO();
        filmeCreateDTO.setDuracao(novaDuracao);
        filmeCreateDTO.setIdioma(novoIdioma);

        FilmeEntity filmeAtualizado = getFilmeEntity();
        filmeAtualizado.setDuracao(novaDuracao);
        filmeAtualizado.setIdioma(novoIdioma);

        when(filmeRepository.findById(idFilme)).thenReturn(Optional.of(filme));
        when(filmeRepository.save(any())).thenReturn(filmeAtualizado);
        FilmeDTO filmeResponse = filmeService.editarFilme(idFilme, filmeCreateDTO);

        assertEquals(idFilme, filmeResponse.getIdFilme());
        assertEquals(filmeCreateDTO.getNome(), filmeResponse.getNome());
        assertEquals(novaDuracao, filmeResponse.getDuracao());
        assertEquals(novoIdioma, filmeResponse.getIdioma());
        assertEquals(filmeCreateDTO.getClassificacaoEtaria(), filmeResponse.getClassificacaoEtaria());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoAtualizarFilmeIdFilmeNaoCadastradoNoBanco() throws RegraDeNegocioException {
        final int idFilme = 1;
        FilmeCreateDTO filme = getFilmeCreateDTO();

        when(filmeRepository.findById(idFilme)).thenReturn(Optional.empty());
        filmeService.editarFilme(idFilme, filme);
    }

    @Test
    public void deveAdicionarFilmeCorretamenteQuandoNomeAindaNaoAdicionadoNoBanco() throws RegraDeNegocioException {
        FilmeCreateDTO filmeCreateDTO = getFilmeCreateDTO();
        FilmeEntity filmeAdicionado = getFilmeEntity();

        when(filmeRepository.findByNome(filmeCreateDTO.getNome())).thenReturn(Optional.empty());
        when(filmeRepository.save(any())).thenReturn(filmeAdicionado);
        FilmeDTO filmeDTO = filmeService.adicionarFilme(filmeCreateDTO);

        assertEquals(filmeAdicionado.getIdFilme(), filmeDTO.getIdFilme());
        assertEquals(filmeCreateDTO.getNome(), filmeDTO.getNome());
        assertEquals(filmeCreateDTO.getIdioma(), filmeDTO.getIdioma());
        assertEquals(filmeCreateDTO.getClassificacaoEtaria(), filmeDTO.getClassificacaoEtaria());
        assertEquals(filmeCreateDTO.getDuracao(), filmeDTO.getDuracao());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveRetornarUmaExcecaoQuandoAdicionaFilmeComNomeJaCadastradoNoBanco() throws RegraDeNegocioException {
        FilmeCreateDTO filmeCreateDTO = getFilmeCreateDTO();
        FilmeEntity filme = getFilmeEntity();

        when(filmeRepository.findByNome(filmeCreateDTO.getNome())).thenReturn(Optional.of(filme));
        filmeService.adicionarFilme(filmeCreateDTO);
    }

    @Test
    public void deveRemoverFilmeCorretamente() throws RegraDeNegocioException {
        final int idFilme = 1;
        FilmeEntity filme = getFilmeEntity();

        when(filmeRepository.findById(idFilme)).thenReturn(Optional.of(filme));
        filmeService.removerFilme(idFilme);

        verify(filmeRepository).deleteById(idFilme);
    }

    @Test
    public void deveRetornarListaComTodosFilmesCorretamente() {
        final int tamanhoListaEsperado = 1;
        FilmeEntity filme = getFilmeEntity();

        when(filmeRepository.findAll()).thenReturn(List.of(filme));
        List<FilmeDTO> list = filmeService.listarTodosFilmes();

        assertNotNull(list);
        assertEquals(tamanhoListaEsperado, list.size());
    }

    @Test
    public void deveConverterCorretamenteClienteEntityParaClienteDTO() throws RegraDeNegocioException {
        final int idFilme = 1;
        FilmeEntity filme = getFilmeEntity();

        when(filmeRepository.findById(idFilme)).thenReturn(Optional.of(filme));
        FilmeDTO filmeDTO = filmeService.listarDTOPeloId(idFilme);

        assertEquals(filme.getIdFilme(), filmeDTO.getIdFilme());
        assertEquals(filme.getNome(), filmeDTO.getNome());
        assertEquals(filme.getIdioma(), filmeDTO.getIdioma());
        assertEquals(filme.getClassificacaoEtaria(), filmeDTO.getClassificacaoEtaria());
        assertEquals(filme.getDuracao(), filmeDTO.getDuracao());
    }

    private static FilmeEntity getFilmeEntity() {
        return new FilmeEntity(1, "Pantera Negra: Wakanda Para Sempre", Idioma.DUBLADO,
                12, 161, "S", Set.of());
    }

    private static FilmeCreateDTO getFilmeCreateDTO() {
        return new FilmeCreateDTO("Pantera Negra: Wakanda Para Sempre", Idioma.DUBLADO,
                12, 161);
    }
}
