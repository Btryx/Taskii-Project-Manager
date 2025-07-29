import { HttpHandlerFn, HttpInterceptorFn, HttpRequest } from "@angular/common/http";
import { inject } from "@angular/core";
import { catchError } from "rxjs";
import { Router } from "@angular/router";
import { KeycloakService } from "./services/keycloak.service.";


export const AuthInterceptor : HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {

  let keycloakService = inject(KeycloakService);
  let router = inject(Router);

  const token = keycloakService.getToken();
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(req).pipe(
    catchError((error) => {
      //TODO: info popup about other errors
       if (error.status==403 || error.status==404) {
        router.navigate(["/projects"]);
      }
      else if(!token || keycloakService.getKeycloak()?.isTokenExpired || error.status==401){
        keycloakService.logout();
      }
      throw error;
    })
  )
}
