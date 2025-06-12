import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map} from "rxjs";

export class User{
  constructor(
    public status:string,
  ) {}

}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  public username: string;

  constructor(private httpClient: HttpClient) { }

  authenticate(username: string, password: string) {
    return this.httpClient.post<{ message: string }>('http://localhost:8082/api/login', {
      username,
      password
    }).pipe(
      map(response => {
      sessionStorage.setItem('username', username);
      sessionStorage.setItem('jwt', response.message);
      console.log(response.message)
      this.username = username;
      })
    );
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem('username')
    return !(user === null)
  }

  logOut() {
    sessionStorage.removeItem('username')
    sessionStorage.removeItem('jwt')
    this.username = null;
  }

  getLoggedInUserName() {
    let user = sessionStorage.getItem('username')
    if (user === null) return ''
    return user
  }
}
