import {Component, OnInit} from '@angular/core';
import {TaskService} from "./task.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'To-do Notes';

  constructor(public taskService : TaskService, public router: Router) { }

  ngOnInit(): void {
  }

  openAddTask(){
    this.router.navigate(['create-task']);
  }
}
