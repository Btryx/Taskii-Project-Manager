import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Auth {

  private http = inject(HttpClient);
  url = 'http://localhost:8082/api/login';

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(this.url, {username, password});
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
