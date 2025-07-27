import { Component, inject, OnInit, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Auth } from './services/auth.service';
import {MatButtonModule} from '@angular/material/button';
import { MatMenu, MatMenuTrigger, MatMenuItem } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { User } from './models/user';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, MatToolbarModule, MatButtonModule, MatMenu, MatMenuTrigger, MatMenuItem, MatIconModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App  implements OnInit {

  protected title = 'Taskii';
  private authService = inject(Auth);
  private router = inject(Router);

  ngOnInit() {
  }

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


  getId() : string | null {
    return this.authService.getId();
  }

  getUserName() : string | null {
    return this.authService.getUsername();
  }

  getEmail() : string | null {
    return this.authService.getEmail();
  }

}
