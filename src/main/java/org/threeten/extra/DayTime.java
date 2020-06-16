/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.threeten.extra;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.*;
import java.util.Objects;

/**
 * A day-of-week and time without a time-zone in the ISO-8601 calendar system
 * such as {@code Monday at 13:45}.
 * <p>
 * {@code DayTime} is an immutable date-time object that represents the combination
 * of a day-of-week and time-of-day.
 * Any field that can be derived from those two fields can be obtained.
 * <p>
 * This class does not store or represent a date or time-zone.
 * <p>
 * The ISO-8601 calendar system is the modern civil calendar system used today
 * in most of the world. It is equivalent to the proleptic Gregorian calendar
 * system, in which today's rules for leap years are applied for all time.
 * For most applications written today, the ISO-8601 rules are entirely suitable.
 * However, any application that makes use of historical dates, and requires them
 * to be accurate will find the ISO-8601 approach unsuitable.
 * <p>
 * ISO-8601 defines the week as always starting with Monday.
 *
 * <h3>Implementation Requirements:</h3>
 * This class is immutable and thread-safe.
 * <p>
 * This class must be treated as a value type. Do not synchronize, rely on the
 * identity hash code or use the distinction between equals() and ==.
 */
public final class DayTime
        implements Temporal, TemporalAccessor, TemporalAdjuster, Comparable<DayTime>, Serializable {

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = -7762817588559947796L;

    /**
     * The day-of-week part.
     */
    private final DayOfWeek dayOfWeek;

    /**
     * The time part.
     */
    private final LocalTime localTime;

    private static final long NANOS_PER_MICROSECOND = 1_000;
    private static final long NANOS_PER_MILLISECOND = 1_000_000;
    private static final long NANOS_PER_SECOND = 1_000_000_000;
    private static final long NANOS_PER_MINUTE = NANOS_PER_SECOND * 60;
    private static final long NANOS_PER_HOUR = NANOS_PER_MINUTE * 60;
    private static final long NANOS_PER_DAY = NANOS_PER_HOUR * 24;
    private static final long NANOS_PER_WEEK = NANOS_PER_DAY * 7;

    private static final long MICROS_PER_DAY = NANOS_PER_DAY / 1000;
    private static final long MILLIS_PER_DAY = MICROS_PER_DAY / 1000;
    private static final long SECONDS_PER_DAY = MILLIS_PER_DAY / 1000;
    private static final long MINUTES_PER_DAY = SECONDS_PER_DAY / 60;

    /**
     * Constructs a new {@code DayTime} instance with the given day-of-week and time-of-day.
     *
     * @param dayOfWeek  the day-of-week part, not null
     * @param localTime  the time-of-day part, not null
     */
    private DayTime(final DayOfWeek dayOfWeek, final LocalTime localTime) {
        this.dayOfWeek = Objects.requireNonNull(dayOfWeek, "Day of week must not be null.");
        this.localTime = Objects.requireNonNull(localTime, "Time of day must not be null.");
    }

    /**
     * Obtains a {@code DayTime} with the given day of the week and local time.
     *
     * @param dayOfWeek the day of the week to represent; must not be {@code null}
     * @param localTime the local time of day to represent; must not be {@code null}
     *
     * @return a day-time with the given day of the week and local time
     */
    public static DayTime of(final DayOfWeek dayOfWeek, final LocalTime localTime) {
        return new DayTime(dayOfWeek, localTime);
    }

    /**
     * Obtains an instance of {@code DayTime} from a temporal object.
     * <p>
     * This obtains a day-time based on the specified temporal. A {@code TemporalAccessor} represents an arbitrary set
     * of date and time information, which this factory converts to an instance of {@code DayTime}.
     * <p>
     * The conversion extracts and combines the {@code DayOfWeek} and the {@code LocalTime} from the temporal object.
     * Implementations are permitted to perform optimizations such as accessing those fields that are equivalent to the
     * relevant objects.
     * <p>
     * This method matches the signature of the functional interface {@link TemporalQuery} allowing it to be used as a
     * query via method reference, {@code LocalDateTime::from}.
     *
     * @param temporalAccessor  the temporal object to convert, not null
     * @return the day-time, not null
     * @throws DateTimeException if unable to convert to a {@code DayTime}
     */
    public static DayTime from(final TemporalAccessor temporalAccessor) {
        Objects.requireNonNull(temporalAccessor, "Temporal accessor must not be null.");

        try {
            final DayOfWeek dayOfWeek = DayOfWeek.from(temporalAccessor);
            final LocalTime localTime = LocalTime.from(temporalAccessor);
            return new DayTime(dayOfWeek, localTime);
        }
        catch (DateTimeException exc) {
            throw new DateTimeException("Unable to obtain DayTime from TemporalAccessor: " + temporalAccessor +
                " of type " + temporalAccessor.getClass().getName(), exc);
        }
    }

    /**
     * Gets the day-of-week field using the {@link DayOfWeek} enum.
     *
     * @return the day of the week
     */
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Gets the hour-of-day field.
     *
     * @return the hour of the day, from 0 to 23
     *
     * @see LocalTime#getHour()
     */
    public int getHour() {
        return localTime.getHour();
    }

    /**
     * Gets the minute-of-hour field.
     *
     * @return the minute of the hour, from 0 to 59
     *
     * @see LocalTime#getMinute()
     */
    public int getMinute() {
        return localTime.getMinute();
    }

    /**
     * Returns the second-of-minute field.
     *
     * @return the second of the minute, from 0 to 59
     *
     * @see LocalTime#getSecond()
     */
    public int getSecond() {
        return localTime.getSecond();
    }

    /**
     * Returns the nano-of-second field.
     *
     * @return the nano-of-second, from 0 to 999,999,999
     *
     * @see LocalTime#getNano()
     */
    public int getNano() {
        return localTime.getNano();
    }

    /**
     * Gets the {@code LocalTime} part of this day-time.
     * <p>
     * This returns a {@code LocalTime} with the same hour, minute, second and nanosecond as this day-time.
     *
     * @return the time part of this day-time, not null
     */
    public LocalTime toLocalTime() {
        return localTime;
    }

    /**
     * Checks if the specified field is supported. Any {@link ChronoField} supported by a {@link DayOfWeek} or
     * {@link LocalTime} is supported.
     *
     * @param field the field to check
     *
     * @return {@code true} if the field is supported or {@code false} otherwise
     */
    @Override
    public boolean isSupported(final TemporalField field) {
        if (field instanceof ChronoField) {
            final ChronoField chronoField = (ChronoField) field;
            return chronoField == ChronoField.DAY_OF_WEEK || chronoField.isTimeBased();
        }

        return field != null && field.isSupportedBy(this);
    }

    @Override
    public boolean isSupported(final TemporalUnit unit) {
        if (unit instanceof ChronoUnit) {
            return unit.isTimeBased();
        }

        return unit != null && unit.isSupportedBy(this);
    }

    @Override
    public long getLong(final TemporalField field) {
        if (field instanceof ChronoField) {
            final ChronoField chronoField = (ChronoField) field;

            if (chronoField == ChronoField.DAY_OF_WEEK) {
                return dayOfWeek.getValue();
            } else {
                return localTime.getLong(field);
            }
        } else {
            return field.getFrom(this);
        }
    }

    @Override
    public Temporal adjustInto(final Temporal temporal) {
        return localTime.adjustInto(dayOfWeek.adjustInto(temporal));
    }

    @Override
    public DayTime with(final TemporalField field, final long newValue) {
        if (field instanceof ChronoField) {
            final ChronoField chronoField = (ChronoField) field;

            final DayOfWeek day;
            final LocalTime time;

            if (field == ChronoField.DAY_OF_WEEK) {
                day = DayOfWeek.of((int) newValue);
                time = this.localTime;
            } else {
                day = this.dayOfWeek;
                time = this.localTime.with(chronoField, newValue);
            }

            return new DayTime(day, time);
        }

        return field.adjustInto(this, newValue);
    }

    /**
     * Returns a copy of this day-time with the specified amount added.
     * <p>
     * This returns a {@code DayTime}, based on this one, with the amount in terms of the unit added. If it is not
     * possible to add the amount, because the unit is not supported or for some other reason, an exception is thrown.
     * <p>
     * If the field is a {@link ChronoUnit} then the addition is implemented here. Otherwise, if the field is not a
     * {@code ChronoUnit}, then the result of this method is obtained by invoking {@code TemporalUnit.addTo(Temporal,
     * long)} passing {@code this} as the argument. In this case, the unit determines whether and how to perform the
     * addition.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param amountToAdd  the amount of the unit to add to the result, may be negative
     * @param unit  the unit of the amount to add, not null
     * @return a {@code DayTime} based on this day-time with the specified amount added, not null
     * @throws DateTimeException if the addition cannot be made
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public DayTime plus(final long amountToAdd, final TemporalUnit unit) {
        final DayTime sum;

        if (unit instanceof ChronoUnit) {
            final long daysToAdd;
            final long nanosToAdd;

            switch ((ChronoUnit) unit) {

                case NANOS: {
                    daysToAdd = amountToAdd / NANOS_PER_DAY;
                    nanosToAdd = amountToAdd % NANOS_PER_DAY;
                    break;
                }

                case MICROS: {
                    daysToAdd = amountToAdd / MICROS_PER_DAY;
                    nanosToAdd = (amountToAdd % MICROS_PER_DAY) * NANOS_PER_MICROSECOND;
                    break;
                }

                case MILLIS: {
                    daysToAdd = amountToAdd / MILLIS_PER_DAY;
                    nanosToAdd = (amountToAdd % MILLIS_PER_DAY) * NANOS_PER_MILLISECOND;
                    break;
                }

                case SECONDS: {
                    daysToAdd = amountToAdd / SECONDS_PER_DAY;
                    nanosToAdd = (amountToAdd % SECONDS_PER_DAY) * NANOS_PER_SECOND;
                    break;
                }

                case MINUTES: {
                    daysToAdd = amountToAdd / MINUTES_PER_DAY;
                    nanosToAdd = (amountToAdd % MINUTES_PER_DAY) * NANOS_PER_MINUTE;
                    break;
                }

                case HOURS: {
                    daysToAdd = amountToAdd / 24;
                    nanosToAdd = (amountToAdd % 24) * NANOS_PER_HOUR;
                    break;
                }

                case HALF_DAYS: {
                    daysToAdd = amountToAdd / 2;
                    nanosToAdd = (amountToAdd % 2) * NANOS_PER_DAY / 2;
                    break;
                }

                case DAYS: {
                    daysToAdd = amountToAdd;
                    nanosToAdd = 0;
                    break;
                }

                default: {
                    throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);
                }
            }

            final LocalTime adjustedTime = localTime.plusNanos(nanosToAdd);
            final long carryoverDay;

            // We may have wrapped around the day boundary and should "carry the one" over to the days to add
            if (amountToAdd > 0 && adjustedTime.isBefore(localTime)) {
                carryoverDay = 1;
            } else if (amountToAdd < 0 && adjustedTime.isAfter(localTime)) {
                carryoverDay = -1;
            } else {
                carryoverDay = 0;
            }

            sum = new DayTime(dayOfWeek.plus(daysToAdd + carryoverDay), adjustedTime);
        } else {
            sum = unit.addTo(this, amountToAdd);
        }

        return sum;
    }

    @Override
    public DayTime minus(final long amountToSubtract, final TemporalUnit unit) {
        // Copied from Temporal to provide more convenient typing
        return (amountToSubtract == Long.MIN_VALUE ? plus(Long.MAX_VALUE, unit).plus(1, unit) : plus(-amountToSubtract, unit));
    }

    @Override
    public long until(final Temporal endExclusive, final TemporalUnit unit) {
        final DayTime endDayTime = DayTime.from(endExclusive);
        final long timeUntil;

        if (unit instanceof ChronoUnit) {
            final long nanosUntil = ((((endDayTime.dayOfWeek.getValue() - dayOfWeek.getValue()) * NANOS_PER_DAY) +
                    endDayTime.localTime.toNanoOfDay() - localTime.toNanoOfDay()) + NANOS_PER_WEEK) % NANOS_PER_WEEK;

            switch ((ChronoUnit) unit) {
                case NANOS: {
                    timeUntil = nanosUntil;
                    break;
                }

                case MICROS: {
                    timeUntil = nanosUntil / 1000;
                    break;
                }

                case MILLIS: {
                    timeUntil = nanosUntil / 1_000_000;
                    break;
                }

                case SECONDS: {
                    timeUntil = nanosUntil / NANOS_PER_SECOND;
                    break;
                }

                case MINUTES: {
                    timeUntil = nanosUntil / NANOS_PER_MINUTE;
                    break;
                }

                case HOURS: {
                    timeUntil = nanosUntil / NANOS_PER_HOUR;
                    break;
                }

                case HALF_DAYS: {
                    timeUntil = nanosUntil / (12 * NANOS_PER_HOUR);
                    break;
                }

                case DAYS: {
                    timeUntil = nanosUntil / NANOS_PER_DAY;
                    break;
                }

                default: {
                    throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);
                }
            }
        } else {
            timeUntil = unit.between(this, endDayTime);
        }

        return timeUntil;
    }

    @Override
    public int compareTo(final DayTime other) {
        final int dayOfWeekComparison = dayOfWeek.compareTo(other.dayOfWeek);
        return dayOfWeekComparison == 0 ? localTime.compareTo(other.localTime) : dayOfWeekComparison;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final DayTime that = (DayTime) other;
        return dayOfWeek == that.dayOfWeek && Objects.equals(localTime, that.localTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfWeek, localTime);
    }

    @Override
    public String toString() {
        return dayOfWeek + "@" + localTime;
    }
}
