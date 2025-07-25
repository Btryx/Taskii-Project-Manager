import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { Auth } from '../service/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-login',
  imports: [MatCardModule, MatFormFieldModule, ReactiveFormsModule, MatInputModule, MatIconModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Login  implements OnInit {
  private authService = inject(Auth);
  private router = inject(Router);
  loginForm = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  })

  ngOnInit(): void {
    if (this.authService.isUserLoggedIn()) {
      this.router.navigate(["/projects"])
    }
  }

  errorMessage = signal('');
  hidePassword = signal(true);

  submitLogin() {
    this.authService.login(this.loginForm.value.username!, this.loginForm.value.password!).subscribe(
      {
        next: data => {
          let token = data.message;
          this.authService.setUserData(this.loginForm.value.username!, token);
        },
        error: (error: HttpErrorResponse) => {
          console.error(error.error.message);
          this.errorMessage.set(error.error.message);
        },
        complete: () => this.router.navigate(["/projects"])
      }
    );
  };

  togglePasswordVisibility(event: MouseEvent) {
    this.hidePassword.set(!this.hidePassword());
    event.stopPropagation;
  }

  navigateToRegister() {
    this.router.navigate(["/register"]);
  }
}
