package br.com.dbc.vemser.cinedev.entity.enums;

public enum NotasFiscais {

    // vamo perguntar pro rafa e o maicon amanha

    CINEDEV("BOTAR O VALOR", 0);

    private String topico;
    private Integer particao;

    NotasFiscais(String topico, Integer particao) {
        this.topico = topico;
        this.particao = particao;
    }

    public String getTopico() {
        return topico;
    }

    public Integer getParticao() {
        return particao;
    }
}
