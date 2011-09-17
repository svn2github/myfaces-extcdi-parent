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
package org.apache.myfaces.extensions.cdi.jpa.impl.transaction.context;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

/**
 * Holds the information we need store to manage
 * the beans in the {@link TransactionContext}.
 */
public class TransactionBeanBag<T>
{
    private Bean<T> bean;
    private T contextualInstance;
    private CreationalContext<T> creationalContext;

    public TransactionBeanBag(Bean<T> bean, T contextualInstance, CreationalContext<T> creationalContext)
    {
        this.bean = bean;
        this.contextualInstance = contextualInstance;
        this.creationalContext = creationalContext;
    }

    public Bean<T> getBean()
    {
        return bean;
    }

    public T getContextualInstance()
    {
        return contextualInstance;
    }

    public CreationalContext<T> getCreationalContext()
    {
        return creationalContext;
    }
}
