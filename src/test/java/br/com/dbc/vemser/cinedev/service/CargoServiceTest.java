package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.entity.CargoEntity;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.CargoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CargoServiceTest {
    @InjectMocks
    private CargoService cargoService;
    @Mock
    private CargoRepository cargoRepository;

    @Test
    public void deveRetornaCargoQuandoCargoEstiverCadastradoNoBancoComSucesso() throws RegraDeNegocioException {
        final int ID_CARGO_ESPERADO = 2;
        final String NOME_ROLE = "ROLE_CIENTE_ID";

        CargoEntity cargoEsperado = new CargoEntity();
        cargoEsperado.setIdCargo(ID_CARGO_ESPERADO);
        cargoEsperado.setNome(NOME_ROLE);

        when(cargoRepository.findById(ID_CARGO_ESPERADO)).thenReturn(Optional.of(cargoEsperado));
        CargoEntity cargoResponse = cargoService.findById(ID_CARGO_ESPERADO);

        assertEquals(ID_CARGO_ESPERADO, cargoResponse.getIdCargo());
        assertEquals(NOME_ROLE, cargoEsperado.getNome());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveGerarUmRegraDeNegocioExceptionQuandoIdNaoEstiverNoBanco() throws RegraDeNegocioException {
        final int ID_CARGO_ESPERADO = 2;

        when(cargoRepository.findById(ID_CARGO_ESPERADO)).thenReturn(Optional.empty());
        cargoService.findById(ID_CARGO_ESPERADO);
    }
}
