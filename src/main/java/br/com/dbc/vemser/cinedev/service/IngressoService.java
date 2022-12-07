package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.dto.UsuarioDTO;
import br.com.dbc.vemser.cinedev.dto.ingressodto.IngressoCompradoDTO;
import br.com.dbc.vemser.cinedev.dto.ingressodto.IngressoCreateDTO;
import br.com.dbc.vemser.cinedev.dto.ingressodto.IngressoDTO;
import br.com.dbc.vemser.cinedev.dto.log.LogCreateDTO;
import br.com.dbc.vemser.cinedev.dto.paginacaodto.PageDTO;
import br.com.dbc.vemser.cinedev.dto.relatorios.RelatorioCadastroIngressoClienteDTO;
import br.com.dbc.vemser.cinedev.entity.CinemaEntity;
import br.com.dbc.vemser.cinedev.entity.ClienteEntity;
import br.com.dbc.vemser.cinedev.entity.FilmeEntity;
import br.com.dbc.vemser.cinedev.entity.IngressoEntity;
import br.com.dbc.vemser.cinedev.entity.enums.Disponibilidade;
import br.com.dbc.vemser.cinedev.entity.enums.TipoLog;
import br.com.dbc.vemser.cinedev.enums.TipoEmails;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.IngressoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngressoService {
    private final IngressoRepository ingressoRepository;
    private final CinemaService cinemaService;
    private final FilmeService filmeService;
    private final ClienteService clienteService;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    private final LogService logService;

    private final ProdutorService produtorService;


    public IngressoEntity findById(Integer id) throws RegraDeNegocioException {
        return ingressoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Cinema não encontrado!"));
    }

    public IngressoDTO ingressoDTOporId(Integer id) throws RegraDeNegocioException {
        return objectMapper.convertValue(findById(id), IngressoDTO.class);
    }

    public List<IngressoDTO> listarIngressos() {
        List<IngressoEntity> ingressoslist = ingressoRepository.findIngressoDisponiveis();
        return ingressoslist.stream()
                .map(ingressoEntity -> objectMapper.convertValue(ingressoEntity, IngressoDTO.class))
                .toList();
    }

    public List<IngressoDTO> listarIngressosComprados() {
        List<IngressoEntity> ingressoslist = ingressoRepository.findIngressoComprados();
        return ingressoslist.stream()
                .map(ingressoEntity -> objectMapper.convertValue(ingressoEntity, IngressoDTO.class))
                .toList();
    }

    public List<RelatorioCadastroIngressoClienteDTO> listarIngressosCompradosPorCliente(Integer id) {
        return clienteService.listarRelatorioPersonalizado(id);
    }

    public List<RelatorioCadastroIngressoClienteDTO> listarIngressosDoClienteLogado() throws RegraDeNegocioException {
        ClienteEntity clienteRecuperado = clienteService.listarClientePorUsuario();
        return clienteService.listarRelatorioPersonalizado(clienteRecuperado.getIdCliente());
    }

    public IngressoDTO createIngresso(IngressoCreateDTO ingressoCreateDTO) throws RegraDeNegocioException {
        FilmeEntity filme = filmeService.listarPeloId(ingressoCreateDTO.getIdFilme());
        CinemaEntity cinema = cinemaService.findById(ingressoCreateDTO.getIdCinema());
        IngressoEntity ingressoEntity = objectMapper.convertValue(ingressoCreateDTO, IngressoEntity.class);
        ingressoEntity.setFilme(filme);
        ingressoEntity.setCinema(cinema);
        IngressoEntity ingressoEntitySalvo = ingressoRepository.save(ingressoEntity);
        return objectMapper.convertValue(ingressoEntitySalvo, IngressoDTO.class);
    }

    public IngressoCompradoDTO comprarIngresso(Integer idCliente, Integer idIngresso) throws RegraDeNegocioException, JsonProcessingException {
        IngressoEntity ingressoRecuperado = findById(idIngresso);

        ClienteEntity clienteRecuperado = clienteService.findById(idCliente);
        ingressoRecuperado.setCliente(clienteRecuperado);
        ingressoRecuperado.setIdCliente(clienteRecuperado.getIdCliente());
        ingressoRecuperado.setDisponibilidade(Disponibilidade.N);
        ingressoRecuperado.setPreco(30.00);
        ingressoRecuperado = ingressoRepository.save(ingressoRecuperado);

        IngressoCompradoDTO ingressoDTO = objectMapper.convertValue(ingressoRecuperado, IngressoCompradoDTO.class);
        UsuarioDTO usuarioDTO = objectMapper.convertValue(clienteRecuperado, UsuarioDTO.class);
        usuarioDTO.setEmail(clienteRecuperado.getUsuario().getEmail());
        ingressoDTO.setNomeCinema(ingressoRecuperado.getCinema().getNome());
        ingressoDTO.setNomeCliente((clienteRecuperado.getPrimeiroNome() + " " + clienteRecuperado.getUltimoNome()));
        ingressoDTO.setCpf(clienteRecuperado.getCpf());
        ingressoDTO.setIdCliente(ingressoRecuperado.getIdCliente());
        ingressoDTO.setNomeFilme(ingressoRecuperado.getFilme().getNome());
        ingressoDTO.setDataHora(LocalDateTime.now());
        emailService.sendEmail(usuarioDTO, TipoEmails.ING_COMPRADO, null);
        ingressoDTO.setPreco(ingressoRecuperado.getPreco());
        produtorService.enviarMensagem(ingressoDTO);
        LogCreateDTO logCreateDTO = new LogCreateDTO(ingressoDTO.getNomeCliente(), TipoLog.INGRESSOS,  LocalDate.now());
//        LogDTO logDTO = objectMapper.convertValue(logCreateDTO, LogDTO.class);

        return ingressoDTO;
    }

    public void removeIngresso(Integer id) {
        ingressoRepository.deleteById(id);
    }

    public PageDTO<IngressoDTO> listIngressoPaginas(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<IngressoEntity> paginaDoRepositorio = ingressoRepository.findAll(pageRequest);
        List<IngressoDTO> ingressosPaginas = paginaDoRepositorio.getContent().stream()
                .map(ingressoEntity -> objectMapper.convertValue(ingressoEntity, IngressoDTO.class))
                .toList();

        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                ingressosPaginas
        );
    }
}
