import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Auth } from './auth.service';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MatToolbarModule, MatButtonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  protected title = 'Taskii';
  private authService = inject(Auth);
  private router = inject(Router);

  isLoggedIn() : boolean {
      return this.authService.isUserLoggedIn()
  }
  logOut() {
    this.authService.logOut()
    this.router.navigate(["/login"]);
  }
}
