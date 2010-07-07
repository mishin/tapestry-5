// Copyright 2007, 2008, 2010 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.internal.services;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Id;
import org.apache.tapestry5.internal.transform.ReadOnlyFieldValueConduit;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.FieldValueConduit;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentValueProvider;
import org.apache.tapestry5.services.InjectionProvider;
import org.apache.tapestry5.services.TransformConstants;
import org.apache.tapestry5.services.TransformField;
import org.apache.tapestry5.services.TransformMethod;

/**
 * Identifies fields of type {@link Block} that have the {@link Inject} annotation and converts them
 * into read-only
 * fields containing the injected Block from the template. The annotation's value is the id of the
 * block to inject; if
 * omitted, the block id is deduced from the field id.
 * <p/>
 * Must be scheduled before {@link DefaultInjectionProvider} because it uses the same annotation, Inject, with a
 * different interpretation.
 */
public class BlockInjectionProvider implements InjectionProvider
{

    public boolean provideInjection(final String fieldName, Class fieldType, ObjectLocator locator,
            ClassTransformation transformation, MutableComponentModel componentModel)
    {
        if (!fieldType.equals(Block.class))
            return false;

        TransformField field = transformation.getField(fieldName);

        Id annotation = field.getAnnotation(Id.class);

        String blockId = getBlockId(fieldName, annotation);

        ComponentValueProvider<FieldValueConduit> provider = cxreateProvider(fieldName, blockId);

        field.replaceAccess(provider);

        return true; // claim the field
    }

    private ComponentValueProvider<FieldValueConduit> cxreateProvider(final String fieldName, final String blockId)
    {
        return new ComponentValueProvider<FieldValueConduit>()
        {
            public FieldValueConduit get(final ComponentResources resources)
            {
                return new ReadOnlyFieldValueConduit(resources, fieldName)
                {
                    public Object get()
                    {
                        return resources.getBlock(blockId);
                    }
                };
            }
        };
    }

    private String getBlockId(String fieldName, Id annotation)
    {
        if (annotation != null)
            return annotation.value();

        return InternalUtils.stripMemberName(fieldName);
    }
}
