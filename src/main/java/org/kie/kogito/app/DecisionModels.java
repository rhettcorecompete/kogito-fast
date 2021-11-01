/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.MalformedInputException;

@org.springframework.stereotype.Component
@org.springframework.web.context.annotation.ApplicationScope
public class DecisionModels extends org.kie.kogito.dmn.AbstractDecisionModels {

    public static String soledmn;
    public static String solens;
    static {
        try
    {
        //InputStream i = new FileInputStream("C:\\Users\\rhett.a.smith\\Downloads\\dmns\\TrafficViolation.dmn");
        //InputStream i = new URL("https://btpx1storage.blob.core.windows.net/open/TrafficViolation.dmn").openStream();
        InputStream i = new URL("https://btpx1storage.blob.core.windows.net/open/50kDecisionTable.dmn").openStream();
        soledmn = "50kDecisionTable";
        solens = "http://www.redhat.com/_c7328033-c355-43cd-b616-0aceef80e52a";
        init(org.kie.kogito.pmml.AbstractPredictionModels.kieRuntimeFactoryFunction, null, null,readResource(i));// readResource(Application.class.getResourceAsStream("/TrafficViolation.dmn")));
     }
    catch (FileNotFoundException ex)  
    {
        // insert code to run when exception occurs
    }
    catch (MalformedInputException ex2)
    {
        //insert code to run when ...
    }
    catch (IOException ex3)
    {

    }
         }

    public DecisionModels(org.kie.kogito.Application app) {
        super(app);
    }
}
