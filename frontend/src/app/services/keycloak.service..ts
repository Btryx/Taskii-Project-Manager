import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import Keycloak, { KeycloakProfile } from 'keycloak-js';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {

  private keycloak: Keycloak | undefined;
  private profile: KeycloakProfile | undefined;

  private http = inject(HttpClient);
  usersUrl = 'http://localhost:8082/api/users';

  async init() {
    if(!this.keycloak) {
    this.keycloak = new Keycloak({
      url: "http://localhost:8080",
      realm: "taskii",
      clientId: "taskii"
    })
    }

    const authenticated = await this.keycloak?.init({
      onLoad: 'login-required'
    });

    if(authenticated) {
      let profile : KeycloakProfile = await this.keycloak?.loadUserProfile();
      this.profile = profile;
      console.log(profile)
      return profile.username;
    }
    return null;
  }

  login() {
    return this.keycloak?.login();
  }

  register() {
    return this.keycloak?.register();
  }

  logout() {
    return this.keycloak?.logout();
  }

  getKeycloak() : Keycloak | undefined {
    return this.keycloak;
  }

  getToken() {
    return this.keycloak?.token;
  }

  getProfile() {
    return this.profile;
  }

  getUsers() : Observable<any> {
    return this.http.get<any>(`${this.usersUrl}`);
  }

  getUserById(id: string) : Observable<User> {
    return this.http.get<User>(`${this.usersUrl}/${id}`);
  }

  getUserByName(name: string) : Observable<User> {
    return this.http.get<User>(`${this.usersUrl}/username/${name}`);
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
