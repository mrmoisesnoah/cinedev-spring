package br.com.dbc.vemser.cinedev.service;

import br.com.dbc.vemser.cinedev.entity.CargoEntity;
import br.com.dbc.vemser.cinedev.exception.RegraDeNegocioException;
import br.com.dbc.vemser.cinedev.repository.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CargoService {
    private final CargoRepository cargoRepository;

    public CargoEntity findById(Integer idCargo) throws RegraDeNegocioException {
        return cargoRepository.findById(idCargo)
                .orElseThrow(() -> new RegraDeNegocioException("Cargo não encontrado!"));
    }
}
