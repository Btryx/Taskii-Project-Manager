import { KeycloakService } from './services/keycloak.service.';
import { Component, inject, OnInit, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
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
  private keycloakService = inject(KeycloakService);
  private router = inject(Router);

  ngOnInit() {
  }

  isLoggedIn() : boolean {
      return this.keycloakService.getToken() ? true : false;
  }

  navigateToProjects() {
    this.router.navigate(["/projects"]);
  }

  async logOut() {
    this.keycloakService.logout();
  }

  getColorForUser(id: string): string {
    return this.keycloakService.getColorForUser(id);
  }


  getId() : string | null {
    return this.keycloakService.getProfile().id;
  }

  getUserName() : string | null {
     return this.keycloakService.getProfile().username;
  }

  getEmail() : string | null {
    return this.keycloakService.getProfile().email;
  }

}
