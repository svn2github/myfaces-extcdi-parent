<%--
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>Hello World</title>
    </head>
    <body>
        <f:view>
            <h1>Scripting-Demo</h1>
            <h:panelGrid>
                <h:outputText value="result via injected ScriptExecutor: #{serverSideScriptingBean.result1}"/>
                <h:outputText value="result via injected ScriptExecutor (parameterized script): #{serverSideScriptingBean.result2}"/>
                <h:outputText value="result via injected ScriptBuilder (parameterized  script): #{serverSideScriptingBean.result3}"/>
                <h:outputText value="result via injected ScriptEngine: #{serverSideScriptingBean.manualResult}"/>
                <h:outputText value="inline: #{sExec.js['2 * 7']}"/>
            </h:panelGrid>
        </f:view>
    </body>
</html>
