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

import static org.testng.Assert.assertEquals;

import java.time.Duration;
import java.time.Period;
import java.util.Locale;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test AmPm.
 */
@Test
public class TestAmountFormats {

    //-----------------------------------------------------------------------
    public void test_iso8601() {
        Period period = Period.of(0, 12, 6);
        Duration duration = Duration.ofMinutes(8 * 60 + 30);
        assertEquals(AmountFormats.iso8601(period, duration ), "P12M6DT8H30M");
    }

    //-----------------------------------------------------------------------
    @DataProvider(name = "wordBased")
    Object[][] data_wordBased() {
        return new Object[][] {
            {Period.ofYears(0), Locale.ROOT, "0 days"},
            {Period.ofYears(1), Locale.ROOT, "1 year"},
            {Period.ofYears(2), Locale.ROOT, "2 years"},
            {Period.ofYears(12), Locale.ROOT, "12 years"},
            {Period.ofYears(-1), Locale.ROOT, "-1 years"},
            
            {Period.ofYears(0), Locale.ENGLISH, "0 days"},
            {Period.ofYears(1), Locale.ENGLISH, "1 year"},
            {Period.ofYears(2), Locale.ENGLISH, "2 years"},
            {Period.ofYears(12), Locale.ENGLISH, "12 years"},
            {Period.ofYears(-1), Locale.ENGLISH, "-1 years"},
        };
    }
    
    @Test(dataProvider = "wordBased")
    public void test_wordBased(Period period, Locale locale, String expected) {
        assertEquals(AmountFormats.wordBased(period, locale), expected);
    }

}
