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
package org.apache.myfaces.extensions.cdi.jsf.impl.listener.request;

import org.apache.myfaces.extensions.cdi.core.api.config.CodiCoreConfig;
import org.apache.myfaces.extensions.cdi.core.api.scope.conversation.WindowContext;
import org.apache.myfaces.extensions.cdi.core.impl.util.ClassDeactivation;
import org.apache.myfaces.extensions.cdi.core.impl.util.CodiUtils;
import org.apache.myfaces.extensions.cdi.jsf.impl.scope.conversation.RedirectedConversationAwareExternalContext;
import org.apache.myfaces.extensions.cdi.jsf.impl.util.ConversationUtils;

import javax.el.ELContext;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Gerhard Petracek
 */
class CodiFacesContextWrapper extends FacesContext
{
    private FacesContext wrappedFacesContext;

    private ExternalContext wrappedExternalContext;

    private Boolean advancedQualifierRequiredForDependencyInjection;

    private BeforeAfterFacesRequestBroadcaster beforeAfterFacesRequestBroadcaster;

    private WindowContext windowContext;

    CodiFacesContextWrapper(FacesContext wrappedFacesContext)
    {
        this.wrappedFacesContext = wrappedFacesContext;

        this.wrappedExternalContext =
                new RedirectedConversationAwareExternalContext(wrappedFacesContext.getExternalContext());

        setCurrentInstance(this);
    }

    public Application getApplication()
    {
        lazyInit();
        return new InjectionAwareApplicationWrapper(wrappedFacesContext.getApplication(),
                this.advancedQualifierRequiredForDependencyInjection);
    }

    public void release()
    {
        broadcastAfterFacesRequestEvent();
        wrappedFacesContext.release();
    }

    private void broadcastAfterFacesRequestEvent()
    {
        lazyInit();
        if(this.beforeAfterFacesRequestBroadcaster != null)
        {
            this.beforeAfterFacesRequestBroadcaster.broadcastAfterFacesRequestEvent(this);
        }
    }

    private void lazyInit()
    {
        if(this.advancedQualifierRequiredForDependencyInjection == null)
        {
            this.advancedQualifierRequiredForDependencyInjection =
                    CodiUtils.getContextualReferenceByClass(CodiCoreConfig.class)
                            .isAdvancedQualifierRequiredForDependencyInjection();

            if(!ClassDeactivation.isClassActivated(BeforeAfterFacesRequestBroadcaster.class))
            {
                return;
            }

            this.beforeAfterFacesRequestBroadcaster =
                    CodiUtils.getContextualReferenceByClass(BeforeAfterFacesRequestBroadcaster.class);
        }
    }

    public ExternalContext getExternalContext()
    {
        return this.wrappedExternalContext;
    }

    public void addMessage(String componentId, FacesMessage facesMessage)
    {
        this.wrappedFacesContext.addMessage(componentId, facesMessage);

        if(this.windowContext == null)
        {
            this.windowContext = ConversationUtils.getWindowContextManager().getCurrentWindowContext();
        }

        if(this.windowContext == null)
        {
            return;
        }

        @SuppressWarnings({"unchecked"})
        List<FacesMessageEntry> facesMessageEntryList =
                this.windowContext.getAttribute(FacesMessage.class.getName(), List.class);

        if(facesMessageEntryList == null)
        {
            facesMessageEntryList = new CopyOnWriteArrayList<FacesMessageEntry>();
            this.windowContext.setAttribute(FacesMessage.class.getName(), facesMessageEntryList);
        }

        facesMessageEntryList.add(new FacesMessageEntry(componentId, facesMessage));
    }

    @Override
    public ELContext getELContext()
    {
        return wrappedFacesContext.getELContext();
    }

    public Iterator<String> getClientIdsWithMessages()
    {
        return wrappedFacesContext.getClientIdsWithMessages();
    }

    public FacesMessage.Severity getMaximumSeverity()
    {
        return wrappedFacesContext.getMaximumSeverity();
    }

    public Iterator<FacesMessage> getMessages()
    {
        return wrappedFacesContext.getMessages();
    }

    public Iterator<FacesMessage> getMessages(String s)
    {
        return wrappedFacesContext.getMessages(s);
    }

    public RenderKit getRenderKit()
    {
        return wrappedFacesContext.getRenderKit();
    }

    public boolean getRenderResponse()
    {
        return wrappedFacesContext.getRenderResponse();
    }

    public boolean getResponseComplete()
    {
        return wrappedFacesContext.getResponseComplete();
    }

    public ResponseStream getResponseStream()
    {
        return wrappedFacesContext.getResponseStream();
    }

    public void setResponseStream(ResponseStream responseStream)
    {
        wrappedFacesContext.setResponseStream(responseStream);
    }

    public ResponseWriter getResponseWriter()
    {
        return wrappedFacesContext.getResponseWriter();
    }

    public void setResponseWriter(ResponseWriter responseWriter)
    {
        wrappedFacesContext.setResponseWriter(responseWriter);
    }

    public UIViewRoot getViewRoot()
    {
        return wrappedFacesContext.getViewRoot();
    }

    public void setViewRoot(UIViewRoot uiViewRoot)
    {
        wrappedFacesContext.setViewRoot(uiViewRoot);
    }

    public void renderResponse()
    {
        wrappedFacesContext.renderResponse();
    }

    public void responseComplete()
    {
        wrappedFacesContext.responseComplete();
    }
}
