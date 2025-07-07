import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Auth } from './auth.service';
import {MatButtonModule} from '@angular/material/button';
import { MatMenu, MatMenuTrigger, MatMenuItem } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MatToolbarModule, MatButtonModule, MatMenu, MatMenuTrigger, MatMenuItem, MatIconModule],
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

  navigateToProjects() {
    this.router.navigate(["/projects"]);
  }

  logOut() {
    this.authService.logOut()
    this.router.navigate(["/login"]);
  }

  getColorForUser(id: string): string {
    return this.authService.getColorForUser(id);
  }
}
