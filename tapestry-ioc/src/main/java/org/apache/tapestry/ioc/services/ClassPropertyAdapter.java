// Copyright 2006 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.ioc.services;

import java.util.List;

/**
 * Organizes all {@link org.apache.tapestry.ioc.services.PropertyAdapter}s for a particular class. *
 * <p>
 * Only provides access to <em>simple</em> properties. Indexed properties are ignored.
 */
public interface ClassPropertyAdapter
{
    /** Returns the names of all properties, sorted into alphabetic order. */
    List<String> getPropertyNames();

    /**
     * Returns the property adapter with the given name, or null if no such adapter exists.
     */
    PropertyAdapter getPropertyAdapter(String name);

    /**
     * Reads the value of a property.
     * 
     * @throws UnsupportedOperationException
     *             if the property is write only
     * @throws IllegalArgumentException
     *             if property does not exist
     */
    Object get(Object instance, String propertyName);

    /**
     * Updates the value of a property. *
     * 
     * @throws UnsupportedOperationException
     *             if the property is read only
     * @throws IllegalArgumentException
     *             if property does not exist
     */
    void set(Object instance, String propertyName, Object value);
}
