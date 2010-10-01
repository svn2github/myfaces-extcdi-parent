/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.extensions.cdi.jsf2.impl.listener.phase;

import org.apache.myfaces.extensions.cdi.jsf.impl.listener.phase.PhaseListenerExtension;
import org.apache.myfaces.extensions.cdi.core.api.Deactivatable;
import org.apache.myfaces.extensions.cdi.core.api.util.ClassDeactivation;

import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import java.util.Iterator;

/**
 * @author Gerhard Petracek
 */
public class CodiLifecycleFactoryWrapper extends LifecycleFactory implements Deactivatable
{
    private final LifecycleFactory wrapped;

    private final boolean deactivated;

    public CodiLifecycleFactoryWrapper(LifecycleFactory wrapped)
    {
        this.wrapped = wrapped;
        this.deactivated = !isActivated();
    }

    public void addLifecycle(String s, Lifecycle lifecycle)
    {
        wrapped.addLifecycle(s, lifecycle);
    }

    public Lifecycle getLifecycle(String s)
    {
        Lifecycle result = this.wrapped.getLifecycle(s);

        if(this.deactivated)
        {
            return result;
        }
        return new CodiLifecycleWrapper(result, PhaseListenerExtension.consumePhaseListeners());
    }

    public Iterator<String> getLifecycleIds()
    {
        return wrapped.getLifecycleIds();
    }

    public LifecycleFactory getWrapped()
    {
        return wrapped.getWrapped();
    }

    public boolean isActivated()
    {
        return ClassDeactivation.isClassActivated(getClass());
    }
}
