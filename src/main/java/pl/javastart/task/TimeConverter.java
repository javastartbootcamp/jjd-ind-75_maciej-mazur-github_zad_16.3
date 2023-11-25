package pl.javastart.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        String operator = operation.substring(0, 1);
        String unit = String.valueOf(operation.charAt(operation.length() - 1));
        int amount = Integer.parseInt(operation.substring(1, operation.length() - 1));
        if (operator.equals(OPERATOR_PLUS)) {
            return increaseOrDecreaseDate(localDateTime, amount, unit, true);
        } else {
            return increaseOrDecreaseDate(localDateTime, amount, unit, false);
        }
    }

    private LocalDateTime increaseOrDecreaseDate(LocalDateTime localDateTime, int amount, String unit, boolean increaseDate) {
        if (increaseDate) {
            return increaseDate(localDateTime, amount, unit);
        } else {
            return decreaseDate(localDateTime, amount, unit);
        }
    }
    
    private LocalDateTime decreaseDate(LocalDateTime localDateTime, int amount, String unit) {
        return switch (unit) {
            case YEARS -> localDateTime.minusYears(amount);
            case MONTHS -> localDateTime.minusMonths(amount);
            case DAYS -> localDateTime.minusDays(amount);
            case HOURS -> localDateTime.minusHours(amount);
            case MINUTES -> localDateTime.minusMinutes(amount);
            case SECONDS -> localDateTime.minusSeconds(amount);
            default -> null;
        };
    }
    
    private LocalDateTime increaseDate(LocalDateTime localDateTime, int amount, String unit) {
        return switch (unit) {
            case YEARS -> localDateTime.plusYears(amount);
            case MONTHS -> localDateTime.plusMonths(amount);
            case DAYS -> localDateTime.plusDays(amount);
            case HOURS -> localDateTime.plusHours(amount);
            case MINUTES -> localDateTime.plusMinutes(amount);
            case SECONDS -> localDateTime.plusSeconds(amount);
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
