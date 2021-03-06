package net.morimekta.providence.compiler.options;/*
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

import net.morimekta.console.args.ArgumentException;
import net.morimekta.console.util.Parser;
import net.morimekta.providence.generator.Language;

import java.util.Collections;
import java.util.LinkedList;

public class GeneratorSpecParser implements Parser<GeneratorSpec> {
    @Override
    public GeneratorSpec parse(String spec) {
        Language generator = null;
        LinkedList<String> options = new LinkedList<>();

        String[] gen = spec.split("[:]", 2);
        if (gen.length > 2) {
            throw new ArgumentException("Invalid generator spec, only one ':' allowed: " + spec);
        }

        for (Language lang : Language.values()) {
            if (gen[0].equalsIgnoreCase(lang.name())) {
                generator = lang;
                break;
            }
        }
        if (generator == null) {
            throw new ArgumentException("Unknown output language " + gen[0]);
        }

        if (gen.length == 2) {
            Collections.addAll(options, gen[1].split("[,]"));
        }

        return new GeneratorSpec(generator, options);
    }
}
