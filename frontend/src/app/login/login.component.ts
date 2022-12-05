import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "./auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username = ''
  password = ''
  invalidLogin = false
  errorMessage = 'Invalid Credentials';

  constructor(private router: Router,
              private authService: AuthService) { }

  ngOnInit() {
  }

  checkLogin() {
    (this.authService.authenticate(this.username, this.password).subscribe(
        data => {
          this.router.navigate(['tasks'])
          this.invalidLogin = false
        },
        error => {
          this.invalidLogin = true

        }
      )
    );

  }

}
