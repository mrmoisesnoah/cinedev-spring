package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.dto.UsuarioDTO;
import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteCreateDTO;
import br.com.dbc.vemser.cinedev.dto.clientedto.ClienteDTO;
import br.com.dbc.vemser.cinedev.dto.log.LogCreateDTO;
import br.com.dbc.vemser.cinedev.dto.relatorios.RelatorioCadastroIngressoClienteDTO;
import br.com.dbc.vemser.cinedev.entity.ClienteEntity;
import br.com.dbc.vemser.cinedev.entity.UsuarioEntity;
import br.com.dbc.vemser.cinedev.entity.enums.TipoLog;
import br.com.dbc.vemser.cinedev.enums.TipoEmails;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ClienteService {
    public static final int ROLE_CLIENTE_ID = 2;
    private final ClienteRepository clienteRepository;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    private final UsuarioService usuarioService;
    private final LogService logService;

    public ClienteEntity findById(Integer id) throws RegraDeNegocioException {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Cliente não encontrado"));
    }

    public ClienteDTO converterParaClienteDTO(ClienteEntity cliente) {
        ClienteDTO clienteDTO = objectMapper.convertValue(cliente, ClienteDTO.class);
        clienteDTO.setEmail(cliente.getUsuario().getEmail());
        return clienteDTO;
    }

    public List<ClienteDTO> listarTodosClientes() {
        List<ClienteEntity> clienteEntityList = clienteRepository.findAll();
        return clienteEntityList.stream()
                .map(this::converterParaClienteDTO)
                .toList();
    }

    public ClienteEntity listarClientePorUsuario() throws RegraDeNegocioException {
        return clienteRepository.findByIdUsuario(usuarioService.getIdLoggedUser())
                .orElseThrow(() -> new RegraDeNegocioException("Usuario não encontrado"));
    }

    public ClienteDTO listarClienteDTOPeloId(Integer idCliente) throws RegraDeNegocioException {
        ClienteEntity cliente = findById(idCliente);
        return converterParaClienteDTO(cliente);
    }

    public ClienteDTO cadastrarCliente(ClienteCreateDTO clienteCreateDTO) throws RegraDeNegocioException {
        String clienteCadastroCPF = clienteCreateDTO.getCpf();
        Optional<ClienteEntity> clientePorCPF = clienteRepository.findByCpf(clienteCadastroCPF);

        String clienteCadastroEmail = clienteCreateDTO.getEmail();
        Optional<UsuarioEntity> clientePorEmail = usuarioService.findOptionalByEmail(clienteCadastroEmail);

        if (clientePorCPF.isPresent() || clientePorEmail.isPresent()) {
            throw new RegraDeNegocioException("Cliente já cadastrado com os mesmos dados");
        }

        String email = clienteCreateDTO.getEmail();
        String senha = clienteCreateDTO.getSenha();
        UsuarioEntity usuario = usuarioService.cadastrarUsuario(email, senha, ROLE_CLIENTE_ID);

        ClienteEntity clienteEntity = objectMapper.convertValue(clienteCreateDTO, ClienteEntity.class);
        clienteEntity.setUsuario(usuario);
        ClienteEntity clienteEntityCadastrado = clienteRepository.save(clienteEntity);

        ClienteDTO clienteDTO = objectMapper.convertValue(clienteEntityCadastrado, ClienteDTO.class);
        clienteDTO.setEmail(usuario.getEmail());
        UsuarioDTO usuarioDTO = objectMapper.convertValue(clienteEntityCadastrado.getUsuario(), UsuarioDTO.class);
        emailService.sendEmail(usuarioDTO, TipoEmails.CREATE, null);

        LogCreateDTO logCreateDTO = new LogCreateDTO(clienteDTO.getPrimeiroNome(), TipoLog.CLIENTE,  LocalDate.now());
//        LogDTO logDTO = objectMapper.convertValue(logCreateDTO, LogDTO.class);
        logService.salvarLog(logCreateDTO);

        return clienteDTO;
    }

    public ClienteDTO atualizarClienteLogado(ClienteCreateDTO clienteCreateDTO) throws RegraDeNegocioException {
        ClienteEntity clienteRecuperado = listarClientePorUsuario();
        return atualizarCliente(clienteRecuperado.getIdCliente(), clienteCreateDTO);
    }

    public ClienteDTO atualizarCliente(Integer idCliente, ClienteCreateDTO clienteCreateDTO) throws RegraDeNegocioException {
        ClienteEntity cliente = findById(idCliente);

        cliente.setPrimeiroNome(clienteCreateDTO.getPrimeiroNome());
        cliente.setUltimoNome(clienteCreateDTO.getUltimoNome());
        cliente.setDataNascimento(clienteCreateDTO.getDataNascimento());
        cliente.setCpf(clienteCreateDTO.getCpf());
        cliente.getUsuario().setEmail(clienteCreateDTO.getEmail());

        ClienteEntity clienteSalvo = clienteRepository.save(cliente);

        ClienteDTO clienteDTO = objectMapper.convertValue(clienteSalvo, ClienteDTO.class);
        clienteDTO.setEmail(clienteSalvo.getUsuario().getEmail());
        UsuarioDTO usuarioDTO = objectMapper.convertValue(clienteSalvo.getUsuario(),UsuarioDTO.class);
        emailService.sendEmail(usuarioDTO, TipoEmails.UPDATE, null);

        return clienteDTO;
    }

    public void deletarClienteLogado() throws RegraDeNegocioException {
        ClienteEntity clienteRecuperado = listarClientePorUsuario();
        deletarUsuarioCliente(clienteRecuperado.getIdCliente());
    }

    public void deletarUsuarioCliente(Integer idCliente) throws RegraDeNegocioException {
        ClienteEntity clienteEntity = findById(idCliente);
        UsuarioEntity usuarioEntity = clienteEntity.getUsuario();
        UsuarioDTO usuarioDTO = objectMapper.convertValue(clienteEntity.getUsuario(), UsuarioDTO.class);

        emailService.sendEmail(usuarioDTO, TipoEmails.DELETE, null);

        clienteRepository.deleteById(clienteEntity.getIdCliente());
        usuarioService.desativarUsuario(usuarioEntity);
    }

    public List<RelatorioCadastroIngressoClienteDTO> listarRelatorioPersonalizado(Integer idCliente) {
        return clienteRepository.listarRelatorioPersonalizado(idCliente);
    }

}
