// Copyright 2023 Libre311 Authors
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package app.service.jurisdiction;

import app.model.jurisdiction.JurisdictionInfoResponse;
import app.model.jurisdiction.JurisdictionRepository;
import io.micronaut.context.annotation.Property;
import jakarta.inject.Singleton;

@Singleton
public class JurisdictionService {

    @Property(name = "micronaut.http.services.auth.url")
    protected String authUrl;

    private final JurisdictionRepository jurisdictionRepository;

    public JurisdictionService(JurisdictionRepository jurisdictionRepository) {
        this.jurisdictionRepository = jurisdictionRepository;
    }

    public JurisdictionInfoResponse findJurisdictionByHostName(String hostName) {
        return jurisdictionRepository.findByRemoteHostsNameEquals(hostName)
            .map(jurisdiction -> new JurisdictionInfoResponse(jurisdiction.getId(), jurisdiction.getName(), authUrl))
            .orElse(null);
    }
}
