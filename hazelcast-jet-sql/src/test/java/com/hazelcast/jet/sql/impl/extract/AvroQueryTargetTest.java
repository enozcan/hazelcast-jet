/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.jet.sql.impl.extract;

import com.hazelcast.sql.impl.extract.QueryExtractor;
import com.hazelcast.sql.impl.extract.QueryTarget;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericRecordBuilder;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

import static com.hazelcast.sql.impl.type.QueryDataType.BIGINT;
import static com.hazelcast.sql.impl.type.QueryDataType.BOOLEAN;
import static com.hazelcast.sql.impl.type.QueryDataType.DATE;
import static com.hazelcast.sql.impl.type.QueryDataType.DECIMAL;
import static com.hazelcast.sql.impl.type.QueryDataType.DECIMAL_BIG_INTEGER;
import static com.hazelcast.sql.impl.type.QueryDataType.DOUBLE;
import static com.hazelcast.sql.impl.type.QueryDataType.INT;
import static com.hazelcast.sql.impl.type.QueryDataType.OBJECT;
import static com.hazelcast.sql.impl.type.QueryDataType.REAL;
import static com.hazelcast.sql.impl.type.QueryDataType.SMALLINT;
import static com.hazelcast.sql.impl.type.QueryDataType.TIME;
import static com.hazelcast.sql.impl.type.QueryDataType.TIMESTAMP;
import static com.hazelcast.sql.impl.type.QueryDataType.TIMESTAMP_WITH_TZ_CALENDAR;
import static com.hazelcast.sql.impl.type.QueryDataType.TIMESTAMP_WITH_TZ_DATE;
import static com.hazelcast.sql.impl.type.QueryDataType.TIMESTAMP_WITH_TZ_INSTANT;
import static com.hazelcast.sql.impl.type.QueryDataType.TIMESTAMP_WITH_TZ_OFFSET_DATE_TIME;
import static com.hazelcast.sql.impl.type.QueryDataType.TIMESTAMP_WITH_TZ_ZONED_DATE_TIME;
import static com.hazelcast.sql.impl.type.QueryDataType.TINYINT;
import static com.hazelcast.sql.impl.type.QueryDataType.VARCHAR;
import static com.hazelcast.sql.impl.type.QueryDataType.VARCHAR_CHARACTER;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

public class AvroQueryTargetTest {

    @Test
    public void test_get() {
        QueryTarget target = new AvroQueryTarget();
        QueryExtractor nullExtractor = target.createExtractor("null", OBJECT);
        QueryExtractor stringExtractor = target.createExtractor("string", VARCHAR);
        QueryExtractor characterExtractor = target.createExtractor("character", VARCHAR_CHARACTER);
        QueryExtractor booleanExtractor = target.createExtractor("boolean", BOOLEAN);
        QueryExtractor byteExtractor = target.createExtractor("byte", TINYINT);
        QueryExtractor shortExtractor = target.createExtractor("short", SMALLINT);
        QueryExtractor intExtractor = target.createExtractor("int", INT);
        QueryExtractor longExtractor = target.createExtractor("long", BIGINT);
        QueryExtractor floatExtractor = target.createExtractor("float", REAL);
        QueryExtractor doubleExtractor = target.createExtractor("double", DOUBLE);
        QueryExtractor bigDecimalExtractor = target.createExtractor("bigDecimal", DECIMAL);
        QueryExtractor bigIntegerExtractor = target.createExtractor("bigInteger", DECIMAL_BIG_INTEGER);
        QueryExtractor localTimeExtractor = target.createExtractor("localTime", TIME);
        QueryExtractor localDateExtractor = target.createExtractor("localDate", DATE);
        QueryExtractor localDateTimeExtractor = target.createExtractor("localDateTime", TIMESTAMP);
        QueryExtractor dateExtractor = target.createExtractor("date", TIMESTAMP_WITH_TZ_DATE);
        QueryExtractor calendarExtractor = target.createExtractor("calendar", TIMESTAMP_WITH_TZ_CALENDAR);
        QueryExtractor instantExtractor = target.createExtractor("instant", TIMESTAMP_WITH_TZ_INSTANT);
        QueryExtractor zonedDateTimeExtractor = target.createExtractor("zonedDateTime", TIMESTAMP_WITH_TZ_ZONED_DATE_TIME);
        QueryExtractor offsetDateTimeExtractor =
                target.createExtractor("offsetDateTime", TIMESTAMP_WITH_TZ_OFFSET_DATE_TIME);

        Schema schema = SchemaBuilder.record("name")
                                     .fields()
                                     .name("null").type().nullable().record("nested").fields().endRecord().noDefault()
                                     .name("string").type().stringType().noDefault()
                                     .name("character").type().stringType().noDefault()
                                     .name("boolean").type().booleanType().noDefault()
                                     .name("byte").type().intType().noDefault()
                                     .name("short").type().intType().noDefault()
                                     .name("int").type().intType().noDefault()
                                     .name("long").type().longType().noDefault()
                                     .name("float").type().floatType().noDefault()
                                     .name("double").type().doubleType().noDefault()
                                     .name("bigDecimal").type().stringType().noDefault()
                                     .name("bigInteger").type().stringType().noDefault()
                                     .name("localTime").type().stringType().noDefault()
                                     .name("localDate").type().stringType().noDefault()
                                     .name("localDateTime").type().stringType().noDefault()
                                     .name("date").type().stringType().noDefault()
                                     .name("calendar").type().stringType().noDefault()
                                     .name("instant").type().stringType().noDefault()
                                     .name("zonedDateTime").type().stringType().noDefault()
                                     .name("offsetDateTime").type().stringType().noDefault()
                                     .endRecord();

        target.setTarget(new GenericRecordBuilder(schema)
                .set("null", null)
                .set("string", "string")
                .set("character", "a")
                .set("boolean", true)
                .set("byte", (byte) 127)
                .set("short", (short) 32767)
                .set("int", 2147483647)
                .set("long", 9223372036854775807L)
                .set("float", 1234567890.1F)
                .set("double", 123451234567890.1D)
                .set("bigDecimal", "9223372036854775.123")
                .set("bigInteger", "9223372036854775222")
                .set("localTime", "12:23:34")
                .set("localDate", "2020-09-09")
                .set("localDateTime", "2020-09-09T12:23:34.1")
                .set("date", "2020-09-09T12:23:34.2Z")
                .set("calendar", "2020-09-09T12:23:34.3Z")
                .set("instant", "2020-09-09T12:23:34.4Z")
                .set("zonedDateTime", "2020-09-09T12:23:34.5Z")
                .set("offsetDateTime", "2020-09-09T12:23:34.6Z")
                .build()
        );

        assertThat(nullExtractor.get()).isNull();
        assertThat(stringExtractor.get()).isEqualTo("string");
        assertThat(characterExtractor.get()).isEqualTo("a");
        assertThat(booleanExtractor.get()).isEqualTo(true);
        assertThat(byteExtractor.get()).isEqualTo((byte) 127);
        assertThat(shortExtractor.get()).isEqualTo((short) 32767);
        assertThat(intExtractor.get()).isEqualTo(2147483647);
        assertThat(longExtractor.get()).isEqualTo(9223372036854775807L);
        assertThat(floatExtractor.get()).isEqualTo(1234567890.1F);
        assertThat(doubleExtractor.get()).isEqualTo(123451234567890.1D);
        assertThat(bigDecimalExtractor.get()).isEqualTo(new BigDecimal("9223372036854775.123"));
        assertThat(bigIntegerExtractor.get()).isEqualTo(new BigDecimal("9223372036854775222"));
        assertThat(localTimeExtractor.get()).isEqualTo(LocalTime.of(12, 23, 34));
        assertThat(localDateExtractor.get()).isEqualTo(LocalDate.of(2020, 9, 9));
        assertThat(localDateTimeExtractor.get()).isEqualTo(LocalDateTime.of(2020, 9, 9, 12, 23, 34, 100_000_000));
        assertThat(dateExtractor.get()).isEqualTo(OffsetDateTime.of(2020, 9, 9, 12, 23, 34, 200_000_000, UTC));
        assertThat(calendarExtractor.get()).isEqualTo(OffsetDateTime.of(2020, 9, 9, 12, 23, 34, 300_000_000, UTC));
        assertThat(instantExtractor.get()).isEqualTo(OffsetDateTime.of(2020, 9, 9, 12, 23, 34, 400_000_000, UTC));
        assertThat(zonedDateTimeExtractor.get()).isEqualTo(OffsetDateTime.of(2020, 9, 9, 12, 23, 34, 500_000_000, UTC));
        assertThat(offsetDateTimeExtractor.get()).isEqualTo(OffsetDateTime.of(2020, 9, 9, 12, 23, 34, 600_000_000, UTC));
        assertThat(offsetDateTimeExtractor.get()).isEqualTo(OffsetDateTime.of(2020, 9, 9, 12, 23, 34, 600_000_000, UTC));
    }
}
