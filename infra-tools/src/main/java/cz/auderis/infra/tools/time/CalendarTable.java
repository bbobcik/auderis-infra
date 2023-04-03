package cz.auderis.infra.tools.time;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalQuery;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Supports operations on a monthly calendar table, which is represented as
 * a rectangular grid of days. The grid is defined by a reference month and
 * a day of week that represents the start of the week.
 * <p>
 * The start (top-left) day of the grid doesn't have to be the first day of
 * the reference month; the same applies to the end (bottom-right) day of the
 * grid. In other words, the grid may contain days from the previous or next month
 * in order to fill the entire grid.
 * <p>
 * The class provides only static methods, many of them in the form of
 * {@linkplain TemporalAdjuster temporal adjusters} and {@linkplain TemporalQuery temporal queries}.
 *
 * @author Boleslav Bobcik
 */
public final class CalendarTable {

    /**
     * Generate a stream of dates that represent the days of the calendar table.
     *
     * @param monthSpec specification of the table month; can be e.g. a {@link YearMonth} or a {@link LocalDate} instance
     * @param firstDayOfWeek day of week that represents the start of the week
     * @return stream of dates that represent the days of the calendar table
     */
    public static Stream<LocalDate> dateStream(@NotNull TemporalAccessor monthSpec, @NotNull DayOfWeek firstDayOfWeek) {
        Objects.requireNonNull(monthSpec);
        Objects.requireNonNull(firstDayOfWeek);
        final var referenceMonth = YearMonth.from(monthSpec);
        final var firstDay = referenceMonth.atDay(1);
        final var firstTableDay = firstDay.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        final var lastDay = referenceMonth.atEndOfMonth();
        final var lastTableDayPlusOne = lastDay.with(TemporalAdjusters.next(firstDayOfWeek));
        return firstTableDay.datesUntil(lastTableDayPlusOne);
    }

    /**
     * {@code TemporalAdjuster} that returns the first day of the calendar grid.
     *
     * @param firstDayOfWeek day of week that represents the start of the week
     * @return temporal adjuster that returns the first day of the calendar grid
     */
    public static TemporalAdjuster firstDay(@NotNull DayOfWeek firstDayOfWeek) {
        Objects.requireNonNull(firstDayOfWeek);
        return (Temporal t) -> {
            final var monthStart = t.with(TemporalAdjusters.firstDayOfMonth());
            return monthStart.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
        };
    }

    /**
     * {@code TemporalAdjuster} that returns the last day of the calendar grid.
     *
     * @param lastDayOfWeek day of week that represents the end of the week
     * @return temporal adjuster that returns the last day of the calendar grid
     */
    public static TemporalAdjuster lastDay(@NotNull DayOfWeek lastDayOfWeek) {
        Objects.requireNonNull(lastDayOfWeek);
        return (Temporal t) -> {
            final var monthEnd = t.with(TemporalAdjusters.lastDayOfMonth());
            return monthEnd.with(TemporalAdjusters.nextOrSame(lastDayOfWeek));
        };
    }

    /**
     * {@code TemporalAdjuster} that returns the last day of the calendar grid,
     * but at least {@code minNumberOfWeeks} weeks long.
     *
     * @param lastDayOfWeek day of week that represents the end of the week
     * @param minNumberOfWeeks minimum number of weeks that the grid must span
     * @return temporal adjuster that returns the last day of the calendar grid
     */
    public static TemporalAdjuster lastDay(@NotNull DayOfWeek lastDayOfWeek, int minNumberOfWeeks) {
        Objects.requireNonNull(lastDayOfWeek);
        return (Temporal t) -> {
            final var monthEnd = t.with(TemporalAdjusters.lastDayOfMonth());
            var result = monthEnd.with(TemporalAdjusters.nextOrSame(lastDayOfWeek));
            if (minNumberOfWeeks > 4) {
                final var monthStart = t.with(TemporalAdjusters.firstDayOfMonth());
                final var beforeFirstTableDay = monthStart.with(TemporalAdjusters.previous(lastDayOfWeek));
                final var minEnd = beforeFirstTableDay.plus(minNumberOfWeeks, ChronoUnit.WEEKS);
                if (minEnd.get(ChronoField.EPOCH_DAY) > result.get(ChronoField.EPOCH_DAY)) {
                    result = minEnd.with(TemporalAdjusters.nextOrSame(lastDayOfWeek));
                }
            }
            return result;
        };
    }

    /**
     * {@code TemporalQuery} that decides whether a given date is one of the listed days of week.
     *
     * @param candidates days of week that are considered to be a match
     * @return temporal query that decides whether a given date is one of the listed days of week
     */
    public static TemporalQuery<Boolean> isAnyDayOfWeek(@NotNull Set<DayOfWeek> candidates) {
        Objects.requireNonNull(candidates);
        return (TemporalAccessor t) -> {
            final var dayOfWeek = t.get(ChronoField.DAY_OF_WEEK);
            return candidates.contains(dayOfWeek);
        };
    }

    /**
     * {@code TemporalQuery} that decides whether a given date is a weekday.
     * @return temporal query that decides whether a given date is a weekday
     */
    public static TemporalQuery<Boolean> isWeekday() {
        return isAnyDayOfWeek(EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
    }

    /**
     * {@code TemporalQuery} that decides whether a given date is a weekend day.
     * @return temporal query that decides whether a given date is a weekend day
     */
    public static TemporalQuery<Boolean> isWeekend() {
        return isAnyDayOfWeek(EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
    }

    /**
     * {@code TemporalQuery} that decides whether a given date belongs to the given month.
     *
     * @param monthSpec specification of the month; can be e.g. a {@link YearMonth} or a {@link LocalDate} instance
     * @return temporal query that decides whether a given date belongs to the given month
     */
    public static TemporalQuery<Boolean> belongsToMonth(@NotNull TemporalAccessor monthSpec) {
        Objects.requireNonNull(monthSpec);
        final var referenceMonth = YearMonth.from(monthSpec);
        final var refYear = referenceMonth.getYear();
        final var refMonth = referenceMonth.getMonthValue();
        return (TemporalAccessor t) -> {
            final var year = t.get(ChronoField.YEAR);
            final var month = t.get(ChronoField.MONTH_OF_YEAR);
            return (year == refYear) && (month == refMonth);
        };
    }

    /**
     * {@code TemporalQuery} that determines the style of a given date, as used
     * in a calendar table for the given month. The style is a constant from
     * {@link CalendarTableStyle}.
     *
     * @param monthSpec specification of the month; can be e.g. a {@link YearMonth} or a {@link LocalDate} instance
     * @return temporal query that determines the style of a given date
     */
    public static TemporalQuery<CalendarTableStyle> dayStyle(@NotNull TemporalAccessor monthSpec) {
        Objects.requireNonNull(monthSpec);
        final var referenceMonth = YearMonth.from(monthSpec);
        final var refYear = referenceMonth.getYear();
        final var refMonth = referenceMonth.getMonthValue();
        return (TemporalAccessor t) -> {
            final var year = t.get(ChronoField.YEAR);
            final var month = t.get(ChronoField.MONTH_OF_YEAR);
            if ((year != refYear) || (month != refMonth)) {
                return CalendarTableStyle.NOT_IN_MONTH;
            }
            final var dayOfWeek = t.get(ChronoField.DAY_OF_WEEK);
            final var bitMask = 1 << dayOfWeek;
            if (bitMask == (WEEKEND_BITMASK & bitMask)) {
                return CalendarTableStyle.WEEKEND;
            }
            return CalendarTableStyle.WEEKDAY;
        };
    }

    /**
     * {@code TemporalQuery} that determines the row number (1-based) of a given date
     * within calendar grid for the given month.
     *
     * @param monthSpec specification of the month; can be e.g. a {@link YearMonth} or a {@link LocalDate} instance
     * @param firstDayOfWeek day of week that represents the start of the week
     * @return temporal query that determines the row number (1-based) of a given date
     */
    public static TemporalQuery<Integer> weekNumber(@NotNull Temporal monthSpec, @NotNull DayOfWeek firstDayOfWeek) {
        Objects.requireNonNull(monthSpec);
        Objects.requireNonNull(firstDayOfWeek);
        final var tableStart = monthSpec.with(firstDay(firstDayOfWeek));
        final var tableStartDay = tableStart.get(ChronoField.EPOCH_DAY);
        return (TemporalAccessor t) -> {
            final var day = t.get(ChronoField.EPOCH_DAY);
            if (day < tableStartDay) {
                throw new IllegalArgumentException("Date is before table start: " + t);
            }
            return (day - tableStartDay) / 7 + 1;
        };
    }

    /**
     * {@code TemporalQuery} that determines whether a given date falls on a date
     * when there is a change of time zone offset.
     *
     * @param targetZone time zone that specifies offset change rules
     * @return temporal query that determines whether a given date is a special day
     */
    public static TemporalQuery<Boolean> isSpecialDay(@NotNull ZoneId targetZone) {
        Objects.requireNonNull(targetZone);
        final var rules = targetZone.getRules();
        if (rules.isFixedOffset()) {
            return (TemporalAccessor t) -> false;
        }
        return (TemporalAccessor t) -> {
            final var referenceDay = LocalDate.from(t);
            final var startOfDay= referenceDay.from(t).atStartOfDay().atZone(targetZone).toInstant()
            final var beforeStartOfDay = startOfDay.minusMillis(1L);
            final var nextTransition = rules.nextTransition(beforeStartOfDay);
            if (null == nextTransition) {
                return false;
            }
            final var nextTransitionDay = nextTransition.getDateTimeBefore().toLocalDate();
            return nextTransitionDay.isEqual(referenceDay);
        };
    }


    public enum CalendarTableStyle {
        WEEKDAY,
        WEEKEND,
        NOT_IN_MONTH
    }

    private static final int WEEKEND_BITMASK;
    static {
        final var weekendDays = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        int weekendMask = 0;
        for (var d : weekendDays) {
            weekendMask |= (1 << d.getValue());
        }
        WEEKEND_BITMASK = weekendMask;
    }

    private CalendarTable() {
        throw new AssertionError();
    }

}
