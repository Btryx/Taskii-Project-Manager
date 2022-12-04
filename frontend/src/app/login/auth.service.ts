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

  authenticate(username, password) {
    const headers = new HttpHeaders({ Authorization: 'Basic ' + btoa(username + ':' + password) });
    return this.httpClient.get<User>('http://localhost:8082/api/validateLogin',{headers}).pipe(
      map(
        userData => {
          sessionStorage.setItem('username',username);
          return userData;
        }
      )

    );
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem('username')
    console.log(!(user === null))
    return !(user === null)
  }

  logOut() {
    sessionStorage.removeItem('username')
  }
}
