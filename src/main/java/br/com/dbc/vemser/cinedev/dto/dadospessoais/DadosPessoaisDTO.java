package br.com.dbc.vemser.cinedev.dto.dadospessoais;

import br.com.dbc.vemser.cinedev.entity.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DadosPessoaisDTO {

        private String nome;
        private String cpf;
        private String rg;
        private String cnh;
        private String nomeMae;
        private String nomePai;
        private String tituloEleitor;
        private Sexo sexo;

}
