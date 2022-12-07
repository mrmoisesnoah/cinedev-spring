package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.dto.avaliacoes.AvaliacoesCreateDTO;
import br.com.dbc.vemser.cinedev.dto.avaliacoes.AvaliacoesDTO;
import br.com.dbc.vemser.cinedev.dto.avaliacoes.AvaliacoesDTOContador;
import br.com.dbc.vemser.cinedev.entity.AvaliacoesEntity;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.AvaliacoesRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AvaliacoesServiceTest {

    @InjectMocks
    private AvaliacoesService avaliacoesService;

    @Mock
    private AvaliacoesRepository avaliacoesRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(avaliacoesService, "objectMapper", objectMapper);
    }
    @Test
    public void teste() {

    }

    @Test
    public void deveAdicionarAvaliacaoComSucesso() throws RegraDeNegocioException {
        final String ID_USUARIO = "1";
        AvaliacoesCreateDTO avaliacoesCreateDTO = getAvaliacoesCreateDTO();
        String avaliacaoNome = avaliacoesCreateDTO.getNome();
        Double nota = avaliacoesCreateDTO.getNota();
        String comentario = avaliacoesCreateDTO.getComentario();
        AvaliacoesEntity avaliacoesEntity = new AvaliacoesEntity();
        avaliacoesEntity.setNome(avaliacaoNome);
        avaliacoesEntity.setNota(nota);
        avaliacoesEntity.setComentario(comentario);
        avaliacoesEntity.setId(ID_USUARIO);

        when(avaliacoesRepository.findByNome(avaliacaoNome)).thenReturn(Optional.empty());
        when(avaliacoesRepository.save(any())).thenReturn(avaliacoesEntity);
        avaliacoesService.adicionarAvaliacao(avaliacoesCreateDTO);

        assertEquals("1", avaliacoesEntity.getId());
        assertEquals(avaliacoesCreateDTO.getNome(), avaliacoesEntity.getNome());

    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveAdicionarAvaliacaoComException() throws RegraDeNegocioException {
        final String ID_USUARIO = "1";
        AvaliacoesCreateDTO avaliacoesCreateDTO = getAvaliacoesCreateDTO();
        String avaliacaoNome = avaliacoesCreateDTO.getNome();
        Double nota = avaliacoesCreateDTO.getNota();
        String comentario = avaliacoesCreateDTO.getComentario();
        AvaliacoesEntity avaliacoesEntity = new AvaliacoesEntity();
        avaliacoesEntity.setNome(avaliacaoNome);
        avaliacoesEntity.setNota(nota);
        avaliacoesEntity.setComentario(comentario);
        avaliacoesEntity.setId(ID_USUARIO);

       when(avaliacoesRepository.findByNome(avaliacoesCreateDTO.getNome())).thenReturn(Optional.of(avaliacoesEntity));
       avaliacoesService.adicionarAvaliacao(avaliacoesCreateDTO);
    }

    @Test
    public void deveRetornarGrupoPorNotaEContarQuantasTemComSucesso() throws RegraDeNegocioException {
        AvaliacoesDTOContador avaliacoesDTOContador = new AvaliacoesDTOContador();
        final int quantidade = 1;
        final Double nota = 10.0;
        avaliacoesDTOContador.setQuantidade(quantidade);
        avaliacoesDTOContador.setNota(nota);
        when(avaliacoesRepository.groupByNota()).thenReturn((List<AvaliacoesDTOContador>) List.of(avaliacoesDTOContador));
        List<AvaliacoesDTOContador> avaliacoesDTOContadores = avaliacoesService.groupByNotaAndCount();
        assertEquals(1, avaliacoesDTOContadores.size());
    }

    @Test
    public void deveListarNotasContendoValorComSucesso() throws RegraDeNegocioException {
        final int tamanhoListaEsperada = 1;
        final Double nota = 10.0;
        AvaliacoesEntity avaliacoesEntity = getAvaliacoesEntity();
        when(avaliacoesRepository.findAllByNota(nota)).thenReturn(List.of(avaliacoesEntity));
        List<AvaliacoesDTO> avaliacoesDTOS = avaliacoesService.listByNotaContains(nota);

        assertNotNull(avaliacoesDTOS);
        assertEquals(tamanhoListaEsperada, avaliacoesDTOS.size());
    }
    public static AvaliacoesCreateDTO getAvaliacoesCreateDTO(){
        final String nome = "Carros 3";
        final Double nota = 10.0;
        final String comentario = "Muito bom";
        AvaliacoesCreateDTO avaliacoesCreateDTO = new AvaliacoesCreateDTO();
        avaliacoesCreateDTO.setComentario(comentario);
        avaliacoesCreateDTO.setNome(nome);
        avaliacoesCreateDTO.setNota(nota);
        return avaliacoesCreateDTO;
    }



    public static AvaliacoesDTO getAvaliacoesDTO(){
        AvaliacoesDTO avaliacoesDTO = new AvaliacoesDTO();
        avaliacoesDTO.setNome(getAvaliacoesDTO().getNome());
        avaliacoesDTO.setNota(getAvaliacoesDTO().getNota());
        avaliacoesDTO.setComentario(getAvaliacoesDTO().getComentario());
        return avaliacoesDTO;
    }

    private static AvaliacoesEntity getAvaliacoesEntity(){
        return new AvaliacoesEntity("1", "Carros 3", 10.0, "Muito bom", 5);
    }
}
