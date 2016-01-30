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

package net.morimekta.providence.reflect.contained;

import net.morimekta.providence.PMessageBuilder;
import net.morimekta.providence.PMessageBuilderFactory;
import net.morimekta.providence.PMessageVariant;
import net.morimekta.providence.descriptor.PField;
import net.morimekta.providence.descriptor.PStructDescriptor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stein Eldar Johnsen
 * @since 07.09.15
 */
public class CExceptionDescriptor extends PStructDescriptor<CException, CField> {
    private final CField[]             mFields;
    private final Map<Integer, CField> mFieldIdMap;
    private final Map<String, CField>  mFieldNameMap;

    public CExceptionDescriptor(String comment, String packageName, String name, List<CField> fields) {
        super(comment, packageName, name, new _Factory(),
              // overrides isSimple instead to avoid having to check fields
              // types before it's converted.
              false, false);
        ((_Factory) factory()).setType(this);

        mFields = fields.toArray(new CField[fields.size()]);

        Map<Integer, CField> fieldIdMap = new LinkedHashMap<>();
        Map<String, CField> fieldNameMap = new LinkedHashMap<>();
        for (CField field : fields) {
            fieldIdMap.put(field.getKey(), field);
            fieldNameMap.put(field.getName(), field);
        }
        mFieldIdMap = fieldIdMap;
        mFieldNameMap = fieldNameMap;
    }

    @Override
    public CField[] getFields() {
        return mFields;
    }

    @Override
    public CField getField(String name) {
        return mFieldNameMap.get(name);
    }

    @Override
    public CField getField(int key) {
        return mFieldIdMap.get(key);
    }

    @Override
    public boolean isSimple() {
        for (PField<?> field : getFields()) {
            switch (field.getType()) {
                case MAP:
                case SET:
                case LIST:
                case MESSAGE:
                    return false;
                default:
                    break;
            }
        }
        return true;
    }

    @Override
    public PMessageVariant getVariant() {
        return PMessageVariant.EXCEPTION;
    }

    private static class _Factory extends PMessageBuilderFactory<CException> {
        private CExceptionDescriptor mType;

        public void setType(CExceptionDescriptor type) {
            mType = type;
        }

        @Override
        public PMessageBuilder<CException> builder() {
            // TODO Auto-generated method stub
            return new CException.Builder(mType);
        }
    }
}
