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
package org.apache.myfaces.extensions.cdi.jsf.test.impl.config.view;

import org.apache.myfaces.extensions.cdi.core.api.config.view.ViewConfig;
import org.apache.myfaces.extensions.cdi.jsf.api.config.view.Page;

/**
 * view-config
 */
@Page(basePath = "/manual/")
abstract class NestedViewConfigWithManualNamesAndSharedBasePath2 implements ViewConfig
{
    @Page(name = "/page3")
    public final class Page1 extends NestedViewConfigWithManualNamesAndSharedBasePath2
    {
    }

    @Page(name = "/page4")
    public final class Page2 extends NestedViewConfigWithManualNamesAndSharedBasePath2
    {
    }
}