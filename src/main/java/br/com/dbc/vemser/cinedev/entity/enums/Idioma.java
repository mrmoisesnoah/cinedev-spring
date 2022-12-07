package br.com.dbc.vemser.cinedev.entity.enums;

public enum Idioma {

    DUBLADO("DUBLADO"), LEGENDADO("LEGENDADO");

    private final String idioma;

    Idioma(String idioma) {
        this.idioma = idioma;
    }

    public String getIdioma() {
        return idioma;
    }

}
