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

  USER_NAME_SESSION_ATTRIBUTE_NAME = 'authenticatedUser'

  public username: string;
  public password: string;

  constructor(private httpClient: HttpClient) { }

  authenticate(username: string, password: string) {
    return this.httpClient.post('http://localhost:8082/api/login', {
      username,
      password
    }).pipe(
      map(() => {
        sessionStorage.setItem('username', username);
        sessionStorage.setItem('password', password);
        this.registerSuccessfulLogin(username, password);
      })
    );
  }

  // TODO:  use token
  registerSuccessfulLogin(username, password) {
    sessionStorage.setItem(this.USER_NAME_SESSION_ATTRIBUTE_NAME, username)
    sessionStorage.setItem(this.password, password)
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem('username')
    return !(user === null)
  }

  logOut() {
    sessionStorage.removeItem('username')
    sessionStorage.removeItem('password')
    this.username = null;
    this.password = null;
  }

  getLoggedInUserName() {
    let user = sessionStorage.getItem('username')
    if (user === null) return ''
    return user
  }

    getLoggedInPassword() {
      let pw = sessionStorage.getItem('password')
      if (pw === null) return ''
      return pw
    }

}
