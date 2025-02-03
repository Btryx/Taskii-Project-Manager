import { TaskStatus } from './../task-status.enum';
import { Component, OnInit } from '@angular/core';
import {Task} from "../task";
import {TaskService} from "../task.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-create-task',
  templateUrl: './create-task.component.html',
  styleUrls: ['./create-task.component.css']
})
export class CreateTaskComponent implements OnInit {

  newTask: Task = new Task();
  TaskStatus = TaskStatus;
  projectId: string;
  constructor(private taskService: TaskService, private router : Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.projectId = params['projectId'];
    });
  }

  addTask(){
    this.newTask.projectId = this.projectId;
    this.taskService.createTask(this.newTask).subscribe( data => {
      this.router.navigate(["tasks/filter"], {
        queryParams: {
          projectId: this.projectId,
          status: null,
          priority:  null
        },
        queryParamsHandling: 'merge'
      });
    })
  }

  goBack() {
    this.router.navigate(["tasks/filter"], {
      queryParams: {
        projectId: this.projectId,
        status: null,
        priority:  null
      },
      queryParamsHandling: 'merge'
    });
  }
}
