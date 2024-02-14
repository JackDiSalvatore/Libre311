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

package app.model.jurisdictionuser;

import app.model.jurisdiction.Jurisdiction;
import app.model.user.User;
import io.micronaut.core.annotation.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "jurisdiction_user")
public class JurisdictionUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User user;

    @NonNull
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Jurisdiction jurisdiction;

    private boolean isUserAdmin = false;

    public JurisdictionUser() {
    }

    public JurisdictionUser(@NonNull User user, @NonNull Jurisdiction jurisdiction, boolean isUserAdmin) {
        this.user = user;
        this.jurisdiction = jurisdiction;
        this.isUserAdmin = isUserAdmin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public User getUser() {
        return user;
    }

    public void setUser(@NonNull User user) {
        this.user = user;
    }

    @NonNull
    public Jurisdiction getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(@NonNull Jurisdiction jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public boolean isUserAdmin() {
        return isUserAdmin;
    }

    public void setUserAdmin(boolean userAdmin) {
        isUserAdmin = userAdmin;
    }
}





