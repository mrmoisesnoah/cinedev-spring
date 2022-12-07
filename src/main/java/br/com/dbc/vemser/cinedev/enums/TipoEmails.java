package br.com.dbc.vemser.cinedev.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoEmails {
    CREATE("Cadastro realizado com sucesso!"),
    UPDATE("Alteração de Dados Cadastrais!"),
    DELETE("Acesso da conta encerrado!"),
    ING_COMPRADO("Compra do IngressoEntity Realizada!"),
    REC_SENHA("Token para recuperação do e-mail realizada!");

    private final String descricao;


}
