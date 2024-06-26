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

package app.security;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import static io.micronaut.context.env.Environment.TEST;

@Client(id = "auth", path = "api/")
@Requires(notEnv = TEST)
public interface UnityAuthClient {
    @Post( "/hasPermission")
    HttpResponse<HasPermissionResponse> hasPermission(@Body HasPermissionRequest requestDTO,
                                                      @Header("Authorization") String authorizationHeader);

    @Post("/principal/permissions")
    HttpResponse<UserPermissionsResponse> getUserPermissions(
        @Body UnityAuthUserPermissionsRequest requestDTO,
        @Header("Authorization") String authorizationHeader);
}