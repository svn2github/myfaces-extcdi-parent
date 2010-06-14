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
package org.apache.myfaces.extensions.cdi.javaee.jsf.impl.config;

import org.apache.myfaces.extensions.cdi.javaee.jsf.api.config.CodiWebConfig12;
import static org.apache.myfaces.extensions.cdi.javaee.jsf.api.WebXmlParameter.TRANSACTION_TOKEN_ENABLED;
import org.apache.myfaces.extensions.cdi.core.api.config.Config;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.faces.context.FacesContext;

/**
 * @author Gerhard Petracek
 */
@Singleton
public class DefaultCodiWebConfig extends CodiWebConfig12
{
    protected DefaultCodiWebConfig()
    {
    }

    @Inject
    public DefaultCodiWebConfig(FacesContext facesContext)
    {
        initTransactionTokenEnabled(facesContext);
    }

    private void initTransactionTokenEnabled(FacesContext facesContext)
    {
        boolean transactionTokenEnabled = false;

        if("true".equalsIgnoreCase(facesContext.getExternalContext().getInitParameter(TRANSACTION_TOKEN_ENABLED)))
        {
            transactionTokenEnabled = true;
        }

        setAttribute(TRANSACTION_TOKEN_ENABLED, transactionTokenEnabled);
    }

    @Produces
    @Named
    @Dependent
    @Config(CodiWebConfig12.class)
    public Boolean transactionTokenEnabled()
    {
        return isTransactionTokenEnabled();
    }

    public boolean isTransactionTokenEnabled()
    {
        return getAttribute(TRANSACTION_TOKEN_ENABLED, Boolean.class);
    }
}
