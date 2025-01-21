import { TaskStatus } from './../task-status.enum';
import { Component, OnInit } from '@angular/core';
import {Task} from "../task";
import {TaskService} from "../task.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-task',
  templateUrl: './create-task.component.html',
  styleUrls: ['./create-task.component.css']
})
export class CreateTaskComponent implements OnInit {

  newTask: Task = new Task();
  TaskStatus = TaskStatus;
  constructor(private taskService: TaskService, private router : Router) { }

  ngOnInit(): void {
  }

  addTask(){
    this.taskService.createTask(this.newTask).subscribe( data => {
      this.newTask = new Task();
      this.router.navigate(['/tasks', 'ALL']);
    })
  }

  goBack() {
    this.router.navigate(['/tasks', 'ALL']);
  }
}
