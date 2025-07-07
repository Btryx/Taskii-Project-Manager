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
  userUrl = "http://localhost:8082/api/user"

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(this.loginUrl, {username, password});
  }

  register(email: string, username: string, password: string): Observable<any> {
    return this.http.post<any>(this.registerUrl, {email, username, password, enabled: true});
  }

  setToken(token: string) {
    sessionStorage.setItem('jwt', token)
  }

  setUsername(username: string) {
    sessionStorage.setItem('username', username)
  }

  getToken(): string | null {
    return sessionStorage.getItem('jwt');
  }

  getUsername(): string | null {
    return sessionStorage.getItem('username');
  }

  isUserLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  logOut() {
    sessionStorage.removeItem('jwt');
    sessionStorage.removeItem('username');
  }

  getUser(id: string) : Observable<any> {
    return this.http.get<any>(`${this.userUrl}/${id}`);
  }

  getColorForUser(id: string): string {
    const COLORS: string[] = [
      '#FF6B6B', // soft red
      '#6BCB77', // mint green
      '#4D96FF', // cornflower blue
      '#FFD93D', // warm yellow
      '#FF6F91', // pink rose
      '#845EC2', // lavender purple
      '#2C73D2', // sapphire blue
      '#00C9A7', // teal green
      '#F9F871', // pastel yellow
      '#F98404', // orange gold
      '#B15DFF', // vibrant violet
      '#00A8E8', // sky blue
      '#FF4C60', // coral
      '#36F1CD', // aqua
      '#FF9F1C', // amber
      '#A9DEF9', // baby blue
      '#E4C1F9', // lilac
      '#F67280', // watermelon
      '#C06C84', // mauve
      '#6C5B7B', // eggplant
    ];

    let hash = 0;
    for (let i = 0; i < id.length; i++) {
      hash = hash + id.charCodeAt(i) + ((hash << 5) - hash);
    }

    const index = Math.abs(hash) % COLORS.length;
    return COLORS[index];
  }

}
