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

package org.netbeans.modules.websvc.core.jaxws;

import org.netbeans.modules.websvc.jaxws.api.JAXWSView;
import org.netbeans.modules.websvc.jaxws.spi.JAXWSViewFactory;
import org.netbeans.modules.websvc.jaxws.spi.JAXWSViewProvider;

/**
 *
 * @author mkuchtiak
 */
@org.openide.util.lookup.ServiceProvider(service=org.netbeans.modules.websvc.jaxws.spi.JAXWSViewProvider.class)
public class ProjectJAXWSViewProvider implements JAXWSViewProvider {
    
    private ProjectJAXWSView wsView = new ProjectJAXWSView();
    private JAXWSView apiJaxWsView;
    /** Creates a new instance of ProjectJAXWSViewProvider */
    public ProjectJAXWSViewProvider() {
    }

    public synchronized JAXWSView findJAXWSView() {
        if (apiJaxWsView==null) {
            apiJaxWsView = JAXWSViewFactory.createJAXWSView(wsView);
        }
        return apiJaxWsView;
    }
    
}
