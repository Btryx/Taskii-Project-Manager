import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import { Auth } from '../auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { InfoPopup } from '../info-popup/info-popup';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-register',
  imports: [MatCardModule, MatFormFieldModule, ReactiveFormsModule, MatInputModule, MatIconModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
  private authService = inject(Auth);
  private router = inject(Router);
  private dialog: MatDialog = inject(MatDialog);


  registerForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    username: new FormControl('', Validators.required),
    password: new FormControl('', [Validators.required, Validators.minLength(10), Validators.pattern(/\d/) ])
  })

  errorMessage = signal('');
  hidePassword = signal(true);

  get anyFieldEmpty() {
    const username = this.registerForm.controls.username;
    const password = this.registerForm.controls.password;
    const email = this.registerForm.controls.email;
    return username.hasError('required') || password.hasError('required') || email.hasError('required');
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
    if(!this.registerForm.valid) {
      this.errorMessage.set("Form is invalid!")
      return;
    }
    this.authService.register(this.registerForm.value.email!, this.registerForm.value.username!, this.registerForm.value.password!).subscribe({
      next: data => {
        if(data.success) {

          const dialogRef = this.dialog.open(InfoPopup, {
            disableClose: false
          });
          dialogRef.componentInstance.title = "Registration successful!"

          dialogRef.afterClosed().subscribe(() => this.router.navigate(["/login"]));

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
