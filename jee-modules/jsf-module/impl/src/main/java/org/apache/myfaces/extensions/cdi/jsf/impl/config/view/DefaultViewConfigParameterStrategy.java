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
package org.apache.myfaces.extensions.cdi.jsf.impl.config.view;

import org.apache.myfaces.extensions.cdi.jsf.impl.config.view.spi.ViewConfigParameterStrategy;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;

/**
 * @author Gerhard Petracek
 */
@Dependent
public class DefaultViewConfigParameterStrategy implements ViewConfigParameterStrategy
{
    private static final long serialVersionUID = 898321901578229292L;

    @Inject
    private ViewConfigParameterContext viewConfigParameterContext;

    /**
     * {@inheritDoc}
     */
    public void addParameter(String key, String value)
    {
        this.viewConfigParameterContext.addViewParameter(key, value);
    }

    /**
     * {@inheritDoc}
     */
    public Object execute(InvocationContext invocationContext) throws Exception
    {
        return invocationContext.proceed();
    }
}
