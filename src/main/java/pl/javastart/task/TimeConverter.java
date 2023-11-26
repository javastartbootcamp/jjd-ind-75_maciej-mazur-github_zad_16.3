package pl.javastart.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

public class TimeConverter {
    private static final String OPERATOR_PLUS = "+";
    private static final String YEARS = "y";
    private static final String MONTHS = "M";
    private static final String DAYS = "d";
    private static final String HOURS = "h";
    private static final String MINUTES = "m";
    private static final String SECONDS = "s";

    public LocalDateTime createDateFromString(String input) throws DataFormatException {
        if (input.toLowerCase().startsWith("t")) {
            return parseSpecialFormatDate(input);
        }

        List<String> dateTimeFormatterStrings = Arrays.asList("yyyy-MM-dd HH:mm:ss", "dd.MM.yyyy HH:mm:ss");
        List<String> dateFormatterStrings = Arrays.asList("yyyy-MM-dd");

        if (input.split(" ").length == 1) {
            return parseDateTime(input, dateFormatterStrings, true);
        } else {
            return parseDateTime(input, dateTimeFormatterStrings, false);
        }
    }

    private LocalDateTime parseSpecialFormatDate(String input) throws DataFormatException {
        if (input.length() == 1) {
            return LocalDateTime.now();
        }

        List<String> operations = new ArrayList<>(Arrays.asList(input.split("(?=[+-])")));
        operations.remove(0); // początkowa litera "t" jest niepotrzebna dla dalszych operacji

        if (!(operations.get(0).charAt(0) == '+' || operations.get(0).charAt(0) == '-')) {
            throw new DataFormatException("Wpisano dane w nieprawidłowym formacie. Po literze \"t\" musi następować znak" +
                    "\"+\" lub znak \"-\"");
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        for (String operation : operations) {
            localDateTime = calculateDate(localDateTime, operation);
        }
        return localDateTime;
    }

    private LocalDateTime calculateDate(LocalDateTime localDateTime, String operation) {
        Pattern pattern = Pattern.compile("(?<operator>[+-])(?<amount>\\d+)(?<unit>[yMdhms])");
        Matcher matcher = pattern.matcher(operation);
        String operator;
        int amount;
        String unit;

        if (matcher.find()) {
            operator = matcher.group("operator");
            amount = Integer.parseInt(matcher.group("amount"));
            unit = matcher.group("unit");
        } else {
            throw new IllegalArgumentException("Podałeś datę w nieprawidłowym formacie.");
        }

        ChronoUnit chronoUnit = getChronoUnitFromString(unit);
        if (operator.equals(OPERATOR_PLUS)) {
            return localDateTime.plus(amount, chronoUnit);
        } else {
            return localDateTime.minus(amount, chronoUnit);
        }
    }

    private ChronoUnit getChronoUnitFromString(String unit) {
        return switch (unit) {
            case YEARS -> ChronoUnit.YEARS;
            case MONTHS -> ChronoUnit.MONTHS;
            case DAYS -> ChronoUnit.DAYS;
            case HOURS -> ChronoUnit.HOURS;
            case MINUTES -> ChronoUnit.MINUTES;
            case SECONDS -> ChronoUnit.SECONDS;
            default -> null;
        };
    }

    private LocalDateTime parseDateTime(String input, List<String> formatterStrings, boolean isTimeCompletionNeeded) {
        LocalDateTime localDateTime = null;

        for (String formatterString : formatterStrings) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterString);

                if (isTimeCompletionNeeded) {
                    LocalDate localDate = LocalDate.parse(input, formatter);
                    localDateTime = LocalDateTime.of(localDate, LocalTime.parse("00:00"));
                } else {
                    localDateTime = LocalDateTime.parse(input, formatter);
                }
            } catch (DateTimeParseException ignore) {
                //
            }
        }

        return localDateTime;
    }
}
