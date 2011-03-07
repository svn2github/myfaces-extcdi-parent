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
package org.apache.myfaces.extensions.cdi.core.api.security;

import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.util.Set;

/**
 * Interface for implementing concrete voters.
 * A voter has to add an instance of
 * {@link org.apache.myfaces.extensions.cdi.core.api.security.SecurityViolation} to the given result-set,
 * if a restriction is detected.
 * 
 * @author Gerhard Petracek
 */
public interface AccessDecisionVoter extends Serializable
{
    /**
     * Checks the permission for the given {@link javax.interceptor.InvocationContext}.
     * If a violation is detected, it should be added to a set which gets returned by the method.
     *
     * @param invocationContext current invocationContext
     * @return a set which contains violations which have been detected
     */
    Set<SecurityViolation> checkPermission(InvocationContext invocationContext);
}
