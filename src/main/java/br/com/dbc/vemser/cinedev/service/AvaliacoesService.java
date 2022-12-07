package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.dto.avaliacoes.AvaliacoesCreateDTO;
import br.com.dbc.vemser.cinedev.dto.avaliacoes.AvaliacoesDTO;
import br.com.dbc.vemser.cinedev.dto.avaliacoes.AvaliacoesDTOContador;
import br.com.dbc.vemser.cinedev.entity.AvaliacoesEntity;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.AvaliacoesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvaliacoesService {

    private final AvaliacoesRepository avaliacoesRepository;

    private final ObjectMapper objectMapper;

    public AvaliacoesDTO adicionarAvaliacao(AvaliacoesCreateDTO avaliacoesCreateDTO) throws RegraDeNegocioException {
        String avaliacaoNome = avaliacoesCreateDTO.getNome();
        Optional<AvaliacoesEntity> avaliacoes = avaliacoesRepository.findByNome(avaliacaoNome);

        if (avaliacoes.isPresent()) {
            throw new RegraDeNegocioException("Erro! Avaliação do filme já consta em nosso cadastro!");
        }

        Double nota = avaliacoesCreateDTO.getNota();
        String comentario = avaliacoesCreateDTO.getComentario();
        AvaliacoesEntity avaliacoesEntity = new AvaliacoesEntity();
        avaliacoesEntity.setNome(avaliacaoNome);
        avaliacoesEntity.setNota(nota);
        avaliacoesEntity.setComentario(comentario);
        avaliacoesRepository.save(avaliacoesEntity);

        AvaliacoesDTO avaliacoesDTO = objectMapper.convertValue(avaliacoesEntity, AvaliacoesDTO.class);
        return avaliacoesDTO;
    }

    public List<AvaliacoesDTOContador> groupByNotaAndCount() {
        return avaliacoesRepository.groupByNota();
    }

    public List<AvaliacoesDTO> listByNotaContains(Double nota) {
        return avaliacoesRepository.findAllByNota(nota).stream()
                .map(avaliacoesEntity -> objectMapper.convertValue(avaliacoesEntity, AvaliacoesDTO.class))
                .toList();
    }

}
