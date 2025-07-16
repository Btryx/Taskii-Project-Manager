import { HttpHandlerFn, HttpInterceptorFn, HttpRequest } from "@angular/common/http";
import { inject } from "@angular/core";
import { catchError } from "rxjs";
import { Auth } from "./service/auth.service";
import { Router } from "@angular/router";


export const AuthInterceptor : HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {

  let authService = inject(Auth);
  let router = inject(Router);

  const token = authService.getToken();
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
      if(!token || error.status==401 || error.status==403){
        authService.logOut();
        router.navigate(["/login"]);
      }
      throw error;
    })
  )
}
