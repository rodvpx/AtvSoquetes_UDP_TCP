/**
* Utilitario com lista de timezones e selecao aleatoria para os testes.
 * @author Rodrigo Simão Guimarães
 *
 * @since 2026-03-19
 */
package testes;

import java.util.concurrent.ThreadLocalRandom;

public final class Timezones {

    public static final String[] LISTA = {
            "America/Sao_Paulo",
            "America/New_York",
            "America/Los_Angeles",
            "America/Chicago",
            "America/Mexico_City",
            "America/Toronto",
            "America/Buenos_Aires",
            "America/Lima",
            "Europe/London",
            "Europe/Berlin",
            "Europe/Paris",
            "Europe/Madrid",
            "Europe/Rome",
            "Europe/Moscow",
            "Europe/Athens",
            "Europe/Lisbon",
            "Asia/Tokyo",
            "Asia/Dubai",
            "Asia/Shanghai",
            "Asia/Hong_Kong",
            "Asia/Singapore",
            "Asia/Seoul",
            "Asia/Mumbai",
            "Asia/Bangkok",
            "Australia/Sydney",
            "Australia/Melbourne",
            "Australia/Perth",
            "Africa/Johannesburg",
            "Africa/Cairo",
            "Africa/Lagos",
            "Pacific/Auckland",
            "Pacific/Fiji"
    };

    private Timezones() {
    }

    public static String aleatorio() {
        return LISTA[ThreadLocalRandom.current().nextInt(LISTA.length)];
    }
}
