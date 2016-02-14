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

package net.morimekta.providence.reflect.util;

import net.morimekta.providence.descriptor.PDeclaredDescriptor;
import net.morimekta.providence.descriptor.PDescriptor;
import net.morimekta.providence.descriptor.PDescriptorProvider;
import net.morimekta.providence.descriptor.PList;
import net.morimekta.providence.descriptor.PMap;
import net.morimekta.providence.descriptor.PPrimitive;
import net.morimekta.providence.descriptor.PSet;
import net.morimekta.providence.reflect.contained.CDocument;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Stein Eldar Johnsen
 * @since 07.09.15
 */
public class TypeRegistry {
    private final Map<String, PDeclaredDescriptor<?>> mDeclaredTypes;
    private final Map<String, String>                 mTypedefs;
    private final Map<String, CDocument>              mDocuments;

    public TypeRegistry() {
        mDeclaredTypes = new LinkedHashMap<>();
        mTypedefs = new LinkedHashMap<>();
        mDocuments = new LinkedHashMap<>();
    }

    public boolean putDocument(String path, CDocument doc) {
        if (!mDocuments.containsKey(path)) {
            mDocuments.put(path, doc);
            return true;
        }
        return false;
    }

    public CDocument getDocument(String path) {
        return mDocuments.get(path);
    }

    public CDocument getDocumentForPackage(String packageContext) {
        for (CDocument document : mDocuments.values()) {
            if (document.getPackageName()
                        .equals(packageContext)) {
                return document;
            }
        }
        return null;
    }

    /**
     * Get the declared type with the name and package context.
     *
     * @param name           Name of type, without any spaces.
     * @param packageContext The package context of the type.
     * @return The type provider.
     */
    @SuppressWarnings("unchecked")
    public <T extends PDeclaredDescriptor<T>> T getDescriptor(String name, String packageContext) {
        String declaredTypeName = name;
        if (!name.contains(".") && packageContext != null) {
            declaredTypeName = packageContext + "." + name;
        }
        if (mDeclaredTypes.containsKey(declaredTypeName)) {
            return (T) mDeclaredTypes.get(declaredTypeName);
        }

        throw new IllegalArgumentException("No such type \"" + name + "\" for package \"" + packageContext + "\"");
    }

    /**
     * Put a declared type into the registry.
     *
     * @param declaredType The type to register.
     */
    public <T> void putDeclaredType(PDeclaredDescriptor<T> declaredType) {
        String declaredTypeName = declaredType.getQualifiedName(null);
        if (mDeclaredTypes.containsKey(declaredTypeName)) {
            throw new IllegalStateException("Type " + declaredTypeName + " already exists");
        }
        mDeclaredTypes.put(declaredTypeName, declaredType);
    }

    /**
     * @param typeName
     * @param identifier
     */
    public void putTypedef(String typeName, String identifier) {
        if (identifier == null || typeName == null) {
            throw new IllegalArgumentException("NOOO!");
        }
        mTypedefs.put(identifier, typeName);
    }

    /**
     * Given a type name and a package context, fetches the type provider for the given type.
     *
     * @param typeName       Name of type, without any spaces.
     * @param packageContext The package context of the type.
     * @return The type provider.
     */
    @SuppressWarnings("unchecked")
    public <T> PDescriptorProvider<T> getProvider(String typeName, final String packageContext) {
        while (mTypedefs.containsKey(typeName)) {
            typeName = mTypedefs.get(typeName);
        }

        // Prepend package context to name
        PPrimitive primitive = PPrimitive.findByName(typeName);
        if (primitive != null) {
            return (PDescriptorProvider<T>) primitive.provider();
        }

        // Collection types are a bit complex, so handle it here.
        if (typeName.startsWith("map<") && typeName.endsWith(">")) {
            String[] parts = typeName.substring(4, typeName.length() - 1)
                                     .split(",", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException(typeName + " is not a valid map descriptor, wrong number of types.");
            }
            String keyType = parts[0];
            String valueType = parts[1];
            return (PDescriptorProvider<T>) PMap.provider(getProvider(keyType, packageContext),
                                                          getProvider(valueType, packageContext));
        }
        if (typeName.startsWith("set<") && typeName.endsWith(">")) {
            String itemType = typeName.substring(4, typeName.length() - 1);
            return (PDescriptorProvider<T>) PSet.provider(getProvider(itemType, packageContext));
        }
        if (typeName.startsWith("list<") && typeName.endsWith(">")) {
            String itemType = typeName.substring(5, typeName.length() - 1);
            return (PDescriptorProvider<T>) PList.provider(getProvider(itemType, packageContext));
        }

        final String name = typeName;

        // Otherwise it's a declared type.
        return new PDescriptorProvider<T>() {
            @Override
            public PDescriptor<T> descriptor() {
                return (PDescriptor<T>) getDescriptor(name, packageContext);
            }
        };
    }
}