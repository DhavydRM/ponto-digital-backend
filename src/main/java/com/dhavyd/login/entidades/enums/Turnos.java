package com.dhavyd.login.entidades.enums;


import java.time.LocalDateTime;
import java.time.LocalTime;

public enum Turnos {

    MANHA,
    TARDE,
    NOITE;

    public static Turnos retornarTurno(LocalDateTime entrada) {

        LocalDateTime meioDia = LocalDateTime.of(entrada.toLocalDate(), LocalTime.NOON);
        LocalDateTime noite = LocalDateTime.of(entrada.toLocalDate(), LocalTime.of(18, 0, 0));

        if (entrada.isBefore(meioDia)) {
            return Turnos.MANHA;

        } else if (entrada.isAfter(meioDia) && entrada.isBefore(noite)) {
            return Turnos.TARDE;

        } else {
            return Turnos.NOITE;
        }

    }
}
