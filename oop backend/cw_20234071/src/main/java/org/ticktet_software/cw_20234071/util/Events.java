package org.ticktet_software.cw_20234071.util;

public enum Events {
    Movie,
    Drama,
    Opera,
    Standup_comedy,
    Musical;

    public static Events fromNumber(int num) {
        return switch (num) {
            case 1 -> Movie;
            case 2 -> Drama;
            case 3 -> Opera;
            case 4 -> Standup_comedy;
            case 5 -> Musical;
            default -> throw new IllegalArgumentException("Invalid event number: " + num);
        };
    }
}
