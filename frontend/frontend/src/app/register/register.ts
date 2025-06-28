import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import { Auth } from '../auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-register',
  imports: [MatCardModule, MatFormFieldModule, ReactiveFormsModule, MatInputModule, MatIconModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
  private authService = inject(Auth);
  private router = inject(Router);

  registerForm = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', [Validators.required, Validators.minLength(10), Validators.pattern(/\d/) ])
  })

  errorMessage = signal('');
  hidePassword = signal(true);

  get anyFieldEmpty() {
    const username = this.registerForm.controls.username;
    const password = this.registerForm.controls.password;
    return username.hasError('required') || password.hasError('required') ;
  }

  get passwordLengthError() {
    const control = this.registerForm.controls.password;
    return control.hasError('minlength') && control.touched;
  }

  get passwordPatternError() {
    const control = this.registerForm.controls.password;
    return control.hasError('pattern') && control.touched;
  }

  submitRegister() {
    if(this.passwordLengthError || this.passwordPatternError) {
      return;
    }
    this.authService.register(this.registerForm.value.username!, this.registerForm.value.password!).subscribe({
      next: data => {
        if(data.success) {
          console.log(data.message);
          this.router.navigate(["/login"]);
        } else {
          this.errorMessage.set(data.message);
        }
      },
      error: (error: HttpErrorResponse) => {
        console.error(error);
        this.errorMessage.set(error.error.message);
      }
    })
  }

  togglePasswordVisibility(event: MouseEvent) {
    this.hidePassword.set(!this.hidePassword());
    event.stopPropagation;
  }
}
