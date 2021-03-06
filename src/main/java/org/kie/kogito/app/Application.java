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

import java.util.Map;

import org.kie.kogito.KogitoEngine;
import org.kie.kogito.process.ProcessConfig;
import org.kie.kogito.uow.UnitOfWorkManager;


@org.springframework.stereotype.Component
@org.springframework.web.context.annotation.ApplicationScope
public class Application extends org.kie.kogito.StaticApplication {

    @org.springframework.beans.factory.annotation.Autowired()
    public Application(org.kie.kogito.Config config, java.util.Collection<org.kie.kogito.KogitoEngine> engines) {
        super(config, engines);
    }

    /*
    @SuppressWarnings("unchecked")
    public <T extends KogitoEngine> T gets(Class<T> clazz) {
        return (T) engineMap.entrySet().stream()
                .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
    */
}
