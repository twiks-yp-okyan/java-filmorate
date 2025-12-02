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

    public static RatingMpa from (String value) {
        return switch (value.toLowerCase()) {
            case "g" -> G;
            case "pg" -> PG;
            case "pg13" -> PG13;
            case "r" -> R;
            case "nc17" -> NC17;
            default -> null;
        };
    }
}
