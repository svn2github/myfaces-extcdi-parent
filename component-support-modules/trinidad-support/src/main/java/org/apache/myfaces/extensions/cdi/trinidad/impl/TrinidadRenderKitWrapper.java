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
package org.apache.myfaces.extensions.cdi.trinidad.impl;

import org.apache.myfaces.trinidad.util.Service;
import org.apache.myfaces.trinidad.render.DialogRenderKitService;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.extensions.cdi.jsf.impl.scope.conversation.InterceptedResponseWriter;

import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;
import java.util.Map;
import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;

/**
 * @author Gerhard Petracek
 */
class TrinidadRenderKitWrapper extends RenderKit
                               implements Service.Provider, DialogRenderKitService, ExtendedRenderKitService
{
    private RenderKit wrapped;

    public TrinidadRenderKitWrapper(RenderKit wrapped)
    {
        this.wrapped = wrapped;
    }

    public ResponseWriter createResponseWriter(Writer writer, String s, String s1)
    {
        ResponseWriter responseWriter = wrapped.createResponseWriter(writer, s, s1);

        if (responseWriter == null)
        {
            return null;
        }

        return new InterceptedResponseWriter(responseWriter);
    }
    
    public <T> T getService(Class<T> tClass)
    {
        if(this.wrapped instanceof Service.Provider)
        {
            return ((Service.Provider)this.wrapped).getService(tClass);
        }
        return null;
    }

    public boolean launchDialog(FacesContext facesContext,
                                UIViewRoot uiViewRoot,
                                UIComponent uiComponent,
                                Map<String, Object> processParameters,
                                boolean useWindow,
                                Map<String, Object> windowProperties)
    {
        if(this.wrapped instanceof DialogRenderKitService)
        {
            return ((DialogRenderKitService)this.wrapped).launchDialog(facesContext,
                                                                       uiViewRoot,
                                                                       uiComponent,
                                                                       processParameters,
                                                                       useWindow,
                                                                       windowProperties);
        }

        //TODO logging
        return false;
    }

    public boolean returnFromDialog(FacesContext facesContext, Object returnValue)
    {
        if(this.wrapped instanceof DialogRenderKitService)
        {
            return ((DialogRenderKitService)this.wrapped).returnFromDialog(facesContext, returnValue);
        }

        //TODO logging
        return false;
    }

    public boolean isReturning(FacesContext facesContext, UIComponent source)
    {
        if(this.wrapped instanceof DialogRenderKitService)
        {
            return ((DialogRenderKitService)this.wrapped).returnFromDialog(facesContext, source);
        }

        //TODO logging
        return false;
    }

    public void addScript(FacesContext facesContext, String s)
    {
        if(this.wrapped instanceof ExtendedRenderKitService)
        {
            ((ExtendedRenderKitService)this.wrapped).addScript(facesContext, s);
        }
    }

    public void encodeScripts(FacesContext facesContext) throws IOException
    {
        if(this.wrapped instanceof ExtendedRenderKitService)
        {
            ((ExtendedRenderKitService)this.wrapped).encodeScripts(facesContext);
        }
    }

    public boolean shortCircuitRenderView(FacesContext facesContext) throws IOException
    {
        if(this.wrapped instanceof ExtendedRenderKitService)
        {
            ((ExtendedRenderKitService)this.wrapped).shortCircuitRenderView(facesContext);
        }

        //TODO logging
        return false;
    }

    public boolean isStateless(FacesContext facesContext)
    {
        if(this.wrapped instanceof ExtendedRenderKitService)
        {
            ((ExtendedRenderKitService)this.wrapped).isStateless(facesContext);
        }

        //TODO logging
        return false;
    }

    public void encodeBegin(FacesContext facesContext) throws IOException
    {
        if(this.wrapped instanceof ExtendedRenderKitService)
        {
            ((ExtendedRenderKitService)this.wrapped).encodeBegin(facesContext);
        }
    }

    public void encodeEnd(FacesContext facesContext) throws IOException
    {
        if(this.wrapped instanceof ExtendedRenderKitService)
        {
            ((ExtendedRenderKitService)this.wrapped).encodeEnd(facesContext);
        }
    }

    public void encodeFinally(FacesContext facesContext)
    {
        if(this.wrapped instanceof ExtendedRenderKitService)
        {
            ((ExtendedRenderKitService)this.wrapped).encodeFinally(facesContext);
        }
    }

    public void addRenderer(String s, String s1, Renderer renderer)
    {
        this.wrapped.addRenderer(s, s1, renderer);
    }

    public Renderer getRenderer(String s, String s1)
    {
        return this.wrapped.getRenderer(s, s1);
    }

    public ResponseStateManager getResponseStateManager()
    {
        return this.wrapped.getResponseStateManager();
    }

    public ResponseStream createResponseStream(OutputStream outputStream)
    {
        return this.wrapped.createResponseStream(outputStream);
    }
}