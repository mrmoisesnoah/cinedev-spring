package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.dto.filmedto.FilmeCreateDTO;
import br.com.dbc.vemser.cinedev.dto.filmedto.FilmeDTO;
import br.com.dbc.vemser.cinedev.entity.FilmeEntity;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.FilmeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmeService {
    private final FilmeRepository filmeRepository;
    private final ObjectMapper objectMapper;

    public FilmeDTO editarFilme(Integer id, FilmeCreateDTO filmeCapturado) throws RegraDeNegocioException {
        listarPeloId(id);

        FilmeEntity filmeEntityTransf = objectMapper.convertValue(filmeCapturado, FilmeEntity.class);
        FilmeEntity filmeEntitySalvo = filmeRepository.save(filmeEntityTransf);
        return objectMapper.convertValue(filmeEntitySalvo, FilmeDTO.class);
    }
    public FilmeDTO adicionarFilme(FilmeCreateDTO filmeCapturado) throws RegraDeNegocioException {
        String filmeNome = filmeCapturado.getNome();
        Optional<FilmeEntity> filmePorNome = filmeRepository.findByNome(filmeNome);

        if (filmePorNome.isPresent()) {
            throw new RegraDeNegocioException("Erro! Nome do filme já consta em nossa lista de cadastros!");
        }

        FilmeEntity filmeEntityTransform = objectMapper.convertValue(filmeCapturado, FilmeEntity.class);
        FilmeEntity filmeEntitySalvo = filmeRepository.save(filmeEntityTransform);
        return objectMapper.convertValue(filmeEntitySalvo, FilmeDTO.class);
    }

    public void removerFilme(Integer id) throws RegraDeNegocioException {
        listarPeloId(id);
        filmeRepository.deleteById(id);
    }

    public List<FilmeDTO> listarTodosFilmes() {
        return filmeRepository.findAll().stream()
                .map(filmeEntity -> objectMapper.convertValue(filmeEntity, FilmeDTO.class))
                .toList();
    }

    public FilmeEntity listarPeloId(Integer idFilme) throws RegraDeNegocioException {
        return filmeRepository.findById(idFilme)
                .orElseThrow(() -> new RegraDeNegocioException("Filme não encontrado com Id procurado."));
    }

    public FilmeDTO listarDTOPeloId(Integer id) throws RegraDeNegocioException {
        return objectMapper.convertValue(listarPeloId(id), FilmeDTO.class);
    }
}
