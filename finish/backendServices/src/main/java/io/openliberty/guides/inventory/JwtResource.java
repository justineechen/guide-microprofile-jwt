// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
 // end::copyright[]
package io.openliberty.guides.inventory;

import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;

//JWT Imports
import org.eclipse.microprofile.jwt.JsonWebToken;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;


@RequestScoped
@Path("jwt")
public class JwtResource {

    /**
     * The JWT of the current caller. Since this is a request scoped resource, the JWT will be
     * injected for each JAX-RS request. The injection is performed by the mpJwt-1.0 feature.
     */
    @Inject private JsonWebToken jwtPrincipal;


     @GET
     @RolesAllowed({"admin", "user"})
     @Path("/username")
     public Response getJwtUserName() {
        return  Response.ok(this.jwtPrincipal.getName()).build();
     }


     @GET
     @RolesAllowed({"admin", "user"})
     @Path("/groups")
     public Response getGroups(@Context SecurityContext securityContext) {
       Set<String> groups = null;
       Principal user = securityContext.getUserPrincipal();
       if (user instanceof JsonWebToken) {
                JsonWebToken jwt = (JsonWebToken) user;
                groups = jwt.getGroups();
       }
       return  Response.ok(groups).build();
      }

     @GET
     @RolesAllowed({"admin", "user"})
     @Path("/customClaim")
     public Response getStatus(@Context SecurityContext securityContext) {
         String customClaim;
         if(securityContext.isUserInRole("admin")) {
             customClaim = jwtPrincipal.getClaim("customClaim");
         } else {
             return Response.status(Response.Status.FORBIDDEN).build();
         }
         return Response.ok(customClaim).build();
     }
}