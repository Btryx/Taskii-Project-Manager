import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Auth {

  private http = inject(HttpClient);
  loginUrl = 'http://localhost:8082/api/login';
  registerUrl = "http://localhost:8082/api/register"

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(this.loginUrl, {username, password});
  }

  register(username: string, password: string): Observable<any> {
    return this.http.post<any>(this.registerUrl, {username, password, enabled: true});
  }

  setToken(token: string) {
    sessionStorage.setItem('jwt', token)
  }

  getToken(): string | null {
    return sessionStorage.getItem('jwt');
  }

  isUserLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  logOut() {
    sessionStorage.removeItem('jwt');
  }
}
