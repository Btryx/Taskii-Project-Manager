import {Component, OnInit} from '@angular/core';
import {TaskService} from "./task.service";
import {Router} from "@angular/router";
import {AuthService} from "./login/auth.service";
import { MatSelectChange } from '@angular/material/select';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'To-do Notes';

  constructor(public router: Router, public loginService: AuthService) { }

  ngOnInit(): void {
  }

  openAddTask(){
    this.router.navigate(['create-task']);
  }

  onFilterChange(event: MatSelectChange) {
    this.router.navigate(['/tasks', event.value]);
  }
}
