import { Component } from '@angular/core';
import { Router } from "@angular/router";
import { AuthService } from "./auth.service";
import { HttpErrorResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  isLoading = false;
  errorMessage = '';
  hidePassword = true;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  checkLogin() {
    if (!this.username || !this.password) {
      this.errorMessage = 'Please enter both username and password';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.authenticate(this.username, this.password)
      .pipe(
        finalize(() => this.isLoading = false)
      )
      .subscribe({
        next: () => {
          this.router.navigate(['/tasks', 'ALL']);
        },
        error: (error: HttpErrorResponse) => {
          if (error.status === 401) {
            this.errorMessage = 'Invalid username or password';
          } else if (error.status === 0) {
            this.errorMessage = 'Invalid username or password';
          } else {
            this.errorMessage = `Login failed: ${error.error?.message || 'Please try again later'}`;
          }
        }
      });
  }
}
