package pl.javastart.task;

import java.time.ZoneId;
import java.util.TimeZone;

public enum TimeZoneItem {
    LOCAL_TIME("Czas lokalny", TimeZone.getDefault().toZoneId()),
    UTC("UTC", ZoneId.of("UTC")),
    LONDON("Londyn", ZoneId.of("Europe/London")),
    LOS_ANGELES("Los Angeles", ZoneId.of("America/Los_Angeles")),
    SYDNEY("Sydney", ZoneId.of("Australia/Sydney"));

    private final String translation;
    private final ZoneId zoneId;

    TimeZoneItem(String translation, ZoneId zoneId) {
        this.translation = translation;
        this.zoneId = zoneId;
    }

    public String getTranslation() {
        return translation;
    }

    public ZoneId getZoneId() {
        return zoneId;
    }
}
