package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.dto.UsuarioDTO;
import br.com.dbc.vemser.cinedev.dto.cinemadto.CinemaCreateDTO;
import br.com.dbc.vemser.cinedev.dto.cinemadto.CinemaDTO;
import br.com.dbc.vemser.cinedev.dto.log.LogCreateDTO;
import br.com.dbc.vemser.cinedev.dto.paginacaodto.PageDTO;
import br.com.dbc.vemser.cinedev.dto.relatorios.RelatorioCadastroCinemaFilmeDTO;
import br.com.dbc.vemser.cinedev.entity.CinemaEntity;
import br.com.dbc.vemser.cinedev.entity.UsuarioEntity;
import br.com.dbc.vemser.cinedev.entity.enums.TipoLog;
import br.com.dbc.vemser.cinedev.enums.TipoEmails;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.CinemaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CinemaService {
    public static final int ROLE_CINEMA_ID = 3;
    private final CinemaRepository cinemaRepository;
    private final ObjectMapper objectMapper;
    private final UsuarioService usuarioService;
    private final EmailService emailService;

    private final LogService logService;

    public CinemaEntity findById(Integer id) throws RegraDeNegocioException {
        CinemaEntity cinemaEntityRecuperado = null;
        cinemaEntityRecuperado = cinemaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Cinema não encontrado!"));
        return cinemaEntityRecuperado;
    }

    public CinemaDTO converterParaCinemaDTO(CinemaEntity cinema) {
        CinemaDTO cinemaDTO = objectMapper.convertValue(cinema, CinemaDTO.class);
        cinemaDTO.setEmail(cinema.getUsuario().getEmail());
        return cinemaDTO;
    }

    public List<CinemaDTO> listarCinema() {
        List<CinemaEntity> cinemaEntityList = cinemaRepository.findAll();
        return cinemaEntityList.stream()
                .map(this::converterParaCinemaDTO)
                .toList();
    }

    public CinemaEntity listarCinemaID(Integer idCinema) throws RegraDeNegocioException {
        Optional<CinemaEntity> cinemaOptional = cinemaRepository.findById(idCinema);

        if (cinemaOptional.isEmpty()) {
            throw new RegraDeNegocioException("cinema não cadastrado");
        }

        return cinemaOptional.get();
    }

    public CinemaDTO listarCinemaPorId(Integer idCinema) throws RegraDeNegocioException {
        CinemaEntity cinema = listarCinemaID(idCinema);
        return converterParaCinemaDTO(cinema);
    }

    public CinemaEntity listarCinemaIdUsuario(Integer idUsuario) throws RegraDeNegocioException {
        Optional<CinemaEntity> cinemaOptional = cinemaRepository.findByIdUsuario(idUsuario);

        if (cinemaOptional.isEmpty()) {
            throw new RegraDeNegocioException("cinema não cadastrado");
        }

        return cinemaOptional.get();
    }

    public CinemaDTO adicionarCinema(CinemaCreateDTO cinemaCapturado) throws RegraDeNegocioException {
        String cinemaNome = cinemaCapturado.getNome();
        Optional<CinemaEntity> cinemaPorNome = cinemaRepository.findByNome(cinemaNome);

        if (cinemaPorNome.isPresent()) {
            throw new RegraDeNegocioException("Erro! Nome do Cinema já consta em nossa lista de cadastros!");
        }

        String email = cinemaCapturado.getEmail();
        String senha = cinemaCapturado.getSenha();
        UsuarioEntity usuario = usuarioService.cadastrarUsuario(email, senha, ROLE_CINEMA_ID);

        CinemaEntity cinema = new CinemaEntity();
        cinema.setUsuario(usuario);
        cinema.setNome(cinemaCapturado.getNome());
        cinema.setEstado(cinemaCapturado.getEstado());
        cinema.setCidade(cinemaCapturado.getCidade());
        CinemaEntity cinemaEntitySalvo = cinemaRepository.save(cinema);

        CinemaDTO cinemaDTO = objectMapper.convertValue(cinemaEntitySalvo, CinemaDTO.class);
        cinemaDTO.setEmail(usuario.getEmail());

        UsuarioDTO cinemaEmail = objectMapper.convertValue(cinemaEntitySalvo.getUsuario(), UsuarioDTO.class);
        emailService.sendEmail(cinemaEmail, TipoEmails.CREATE, null);

        LogCreateDTO logCreateDTO = new LogCreateDTO(cinemaDTO.getNome(), TipoLog.CINEMA,  LocalDate.now());
//        LogDTO logDTO = objectMapper.convertValue(logCreateDTO, LogDTO.class);
        logService.salvarLog(logCreateDTO);

        return cinemaDTO;
    }

    public CinemaDTO atualizarCinema(Integer idCinema, CinemaCreateDTO cinemaCreateDTO) throws RegraDeNegocioException {
        CinemaEntity cinemaPego = listarCinemaIdUsuario(idCinema);

        cinemaPego.setNome(cinemaCreateDTO.getNome());
        cinemaPego.setCidade(cinemaCreateDTO.getCidade());
        cinemaPego.setEstado(cinemaCreateDTO.getEstado());
        cinemaPego.getUsuario().setEmail(cinemaCreateDTO.getEmail());

        CinemaEntity cinemaSalvo = cinemaRepository.save(cinemaPego);
        CinemaDTO cinemaDTO = objectMapper.convertValue(cinemaSalvo, CinemaDTO.class);
        cinemaDTO.setEmail(cinemaPego.getUsuario().getEmail());

        UsuarioDTO cinemaEmail = objectMapper.convertValue(cinemaSalvo.getUsuario(), UsuarioDTO.class);
        emailService.sendEmail(cinemaEmail, TipoEmails.UPDATE, null);

        return cinemaDTO;
    }

    public CinemaDTO atualizarCinemaLogado(CinemaCreateDTO cinemaCreateDTO) throws RegraDeNegocioException {
        CinemaEntity cinemaPego = listarCinemaIdUsuario(usuarioService.getIdLoggedUser());
        return atualizarCinema(cinemaPego.getIdCinema(), cinemaCreateDTO);
    }

    public void deletarCinemaLogado() throws RegraDeNegocioException {
        CinemaEntity cinemaPego = listarCinemaIdUsuario(usuarioService.getIdLoggedUser());
        deletarCinemaPorUsuario(cinemaPego.getIdCinema());
    }

    public void deletarCinemaPorUsuario(Integer idCinema) throws RegraDeNegocioException {
        CinemaEntity cinema = findById(idCinema);
        UsuarioEntity usuarioEntity = cinema.getUsuario();
        cinemaRepository.delete(cinema);
        usuarioService.desativarUsuario(usuarioEntity);
        UsuarioDTO cinemaEmail = objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
        emailService.sendEmail(cinemaEmail, TipoEmails.DELETE, null);
    }

    public List<RelatorioCadastroCinemaFilmeDTO> listarRelatorioPersonalizadoCinemaLogado() throws RegraDeNegocioException {
        CinemaEntity cinemaLogado = listarCinemaIdUsuario(usuarioService.getIdLoggedUser());
        return listarRelatorioPersonalizado(cinemaLogado.getIdCinema());
    }

    public List<RelatorioCadastroCinemaFilmeDTO> listarRelatorioPersonalizado(Integer idCinema) {
        return cinemaRepository.listarRelatorioPersonalizado(idCinema);
    }

    public PageDTO<CinemaDTO> listCinemaPaginado(Integer pagina, Integer tamanho) {
        PageRequest pageRequest = PageRequest.of(pagina, tamanho);
        Page<CinemaEntity> paginaDoRepositorio = cinemaRepository.findAll(pageRequest);
        List<CinemaDTO> cinemasPaginas = paginaDoRepositorio.getContent().stream()
                .map(cinemaEntity -> objectMapper.convertValue(cinemaEntity, CinemaDTO.class))
                .toList();

        return new PageDTO<>(paginaDoRepositorio.getTotalElements(),
                paginaDoRepositorio.getTotalPages(),
                pagina,
                tamanho,
                cinemasPaginas
        );
    }
}