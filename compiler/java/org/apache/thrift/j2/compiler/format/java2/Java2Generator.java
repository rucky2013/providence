/*
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

package org.apache.thrift.j2.compiler.format.java2;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.thrift.j2.compiler.generator.GeneratorException;
import org.apache.thrift.j2.compiler.util.FileManager;
import org.apache.thrift.j2.descriptor.TDeclaredDescriptor;
import org.apache.thrift.j2.descriptor.TStructDescriptor;
import org.apache.thrift.j2.reflect.contained.TCDocument;
import org.apache.thrift.j2.reflect.util.TTypeRegistry;
import org.apache.thrift.j2.compiler.generator.Generator;
import org.apache.thrift.j2.util.io.IndentedPrintWriter;
import org.apache.thrift.j2.descriptor.TEnumDescriptor;

/**
 * @author Stein Eldar Johnsen
 * @since 05.09.15
 */
public class Java2Generator
        extends Generator {
    TTypeRegistry   mRegistry;
    Java2TypeHelper mTypeHelper;
    boolean         mAndroid;

    public Java2Generator(FileManager manager,
                          TTypeRegistry registry,
                          boolean android) {
        super(manager);
        mRegistry = registry;
        mAndroid = android;

        mTypeHelper = new Java2TypeHelper(mRegistry);
    }

    @Override
    @SuppressWarnings("resource")
    public void generate(TCDocument document) throws IOException, GeneratorException {
        String javaPackage = Java2Utils.getJavaPackage(document);
        Java2MessageFormatter messageFormatter =
                new Java2MessageFormatter(mTypeHelper, mAndroid);
        Java2EnumFormatter enumFormatter
                = new Java2EnumFormatter(mTypeHelper);

        String path = Java2Utils.getPackageClassPath(javaPackage);

        for (TDeclaredDescriptor<?> type : document.getDeclaredTypes()) {
            String file = mTypeHelper.getInstanceClassName(type) + ".java";
            OutputStream out = getFileManager().create(path, file);
            try {
                IndentedPrintWriter writer = new IndentedPrintWriter(out);
                switch (type.getType()) {
                    case MESSAGE:
                        messageFormatter.format(writer, (TStructDescriptor<?,?>) type);
                        break;
                    case ENUM:
                        enumFormatter.format(writer, (TEnumDescriptor<?>) type);
                        break;
                    default:
                        throw new GeneratorException("Unhandled declaration type.");

                }
                writer.flush();
            } finally {
                System.out.println(path + File.separatorChar + file + " OK");
                getFileManager().finalize(out);
            }
        }
    }
}