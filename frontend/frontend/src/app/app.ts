import { Component, inject, OnInit, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Auth } from './service/auth.service';
import {MatButtonModule} from '@angular/material/button';
import { MatMenu, MatMenuTrigger, MatMenuItem } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { User } from './model/user';

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
  user = signal(new User());
  isLoading = signal(true);

  ngOnInit() {
    this.getUser();
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

  getUser() {
    let username = this.authService.getUsername();
    this.authService.getUserByName(username!).subscribe({
      next: (data) => {
        this.user.set(data);
      },
      complete: () => this.isLoading.set(false)
    })
  }

  getUserName() : string {
    return this.user().username;
  }

  getEmail() : string {
    return this.user().email;
  }

}
