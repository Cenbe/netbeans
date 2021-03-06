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
package org.netbeans.modules.javaee.project.api.ant.ui.customizer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.netbeans.api.annotations.common.NonNull;
import org.netbeans.api.annotations.common.NullAllowed;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.netbeans.spi.project.support.ant.EditableProperties;
import org.netbeans.spi.project.support.ant.PropertyEvaluator;
import org.netbeans.spi.project.support.ant.ui.CustomizerUtilities;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

// this class was copied into org.netbeans.modules.web.clientproject.ui.customizer.LicensePanelSupport
public final class LicensePanelSupport implements CustomizerUtilities.LicensePanelContentHandler {

    public static final String LICENSE_NAME = "project.license";
    public static final String LICENSE_PATH = "project.licensePath";

    private String licenseName;
    private String licensePath;
    private String licenseContent;

    private PropertyEvaluator evaluator;
    private AntProjectHelper antHelper;

    public LicensePanelSupport(PropertyEvaluator evaluator, AntProjectHelper antHelper,
            String licensePath, String licenseName) {
        this.evaluator = evaluator;
        this.antHelper = antHelper;
        this.licensePath = licensePath;
        this.licenseName = licenseName;
        this.licenseContent = null;
    }

    @Override
    public String getProjectLicenseLocation() {
        return licensePath;
    }

    @Override
    public String getGlobalLicenseName() {
        return licenseName;
    }

    @Override
    public FileObject resolveProjectLocation(@NonNull String path) {
        String evaluated = evaluator.evaluate(path);
        if (evaluated != null) {
            return antHelper.resolveFileObject(evaluated);
        } else {
            return null;
        }
    }

    @Override
    public void setProjectLicenseLocation(@NullAllowed String newLocation) {
        licensePath = newLocation;
    }

    @Override
    public void setGlobalLicenseName(@NullAllowed String newName) {
        licenseName = newName;
    }

    @Override
    public String getDefaultProjectLicenseLocation() {
        return "./nbproject/licenseheader.txt";
    }

    @Override
    public void setProjectLicenseContent(@NullAllowed String text) {
        licenseContent = text;
    }

    public void saveLicenseFile() throws IOException {
        if (licenseContent != null) {
            assert licensePath != null; //path needs to exist once we have content?
            String evaluated = evaluator.evaluate(licensePath);
            assert evaluated != null;
            File file = antHelper.resolveFile(evaluated);
            FileObject fo;
            if (!file.exists()) {
                fo = FileUtil.createData(file);
            } else {
                fo = FileUtil.toFileObject(file);
            }
            OutputStream out = fo.getOutputStream();
            try {
                FileUtil.copy(new ByteArrayInputStream(licenseContent.getBytes()), out);
            } finally {
                out.close();
            }
        }
    }

    public void updateProperties(EditableProperties projectProperties) {
        if (licensePath != null) {
            projectProperties.setProperty(LICENSE_PATH, licensePath);
        } else {
            projectProperties.remove(LICENSE_PATH);
        }
        if (licenseName != null) {
            projectProperties.setProperty(LICENSE_NAME, licenseName);
        } else {
            projectProperties.remove(LICENSE_NAME);
        }
    }
}
