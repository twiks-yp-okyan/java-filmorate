package ru.yandex.practicum.filmorate.model;

public enum RatingMpa {
    G("G"),
    PG("PG"),
    PG13("PG13"),
    R("R"),
    NC17("NC17");

    public final String label;

    private RatingMpa(String label) {
        this.label = label;
    }
}
