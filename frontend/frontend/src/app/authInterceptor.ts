import { HttpHandlerFn, HttpInterceptorFn, HttpRequest } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { catchError, Observable } from "rxjs";
import { Auth } from "./auth";
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
      if(error.status==401 && token){
        //logout and redirect to login
        authService.logOut();
        router.navigate(["/login"]);
      }
      if(error.status==403 && token){
        //redirect to a "You don't have access to this project" page
      }
      throw error;
    })
  )
}
