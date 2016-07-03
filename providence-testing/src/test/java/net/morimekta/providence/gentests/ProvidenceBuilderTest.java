/*
 * Copyright (c) 2016, Stein Eldar Johnsen
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package net.morimekta.providence.gentests;

import net.morimekta.test.providence.CompactFields;
import net.morimekta.test.providence.OptionalFields;
import net.morimekta.test.providence.calculator.Operand;
import net.morimekta.test.providence.number.Imaginary;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for providence-built sources - The message builder.
 */
public class ProvidenceBuilderTest {
    @Test
    public void testMerge() {
        OptionalFields a = OptionalFields.builder()
                                         .setIntegerValue(1)
                                         .setDoubleValue(1.1)
                                         .setCompactValue(new CompactFields("a", 1, "al"))
                                         .build();
        OptionalFields b = OptionalFields.builder()
                                         .setIntegerValue(2)
                                         .setCompactValue(new CompactFields("b", 2, null))
                                         .build();

        OptionalFields exp = OptionalFields.builder()
                                           .setIntegerValue(2)
                                           .setDoubleValue(1.1)
                                           .setCompactValue(new CompactFields("b", 2, "al"))
                                           .build();

        OptionalFields c = a.mergeWith(b);

        assertEquals(exp, c);
    }
}
