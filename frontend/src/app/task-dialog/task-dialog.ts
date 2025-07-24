import { Component, inject, Inject, OnInit, signal, WritableSignal } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select'
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Task } from '../model/task';
import { MatNativeDateModule } from '@angular/material/core';
import { Status } from '../model/status';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { CommentService } from '../service/comment.service';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Comment } from '../model/comment';
import { Auth } from '../service/auth.service';
import { CommonModule  } from '@angular/common';
import { map, Observable } from 'rxjs';
import { ConfirmationDialog } from '../comformation-dialog/comformation-dialog';
import { User } from '../model/user';

interface TaskDialogData extends Task {
  title: string;
  statuses: Status[];
  collaborators: User[];
}

@Component({
  selector: 'app-task-dialog',
  imports: [MatFormFieldModule, CommonModule, MatSelectModule, ReactiveFormsModule, MatInputModule, MatButtonModule, MatDatepickerModule, FormsModule, MatNativeDateModule, MatIconModule],
  templateUrl: './task-dialog.html',
  styleUrl: './task-dialog.css',
  providers: [provideNativeDateAdapter()],
})
export class TaskDialog  implements OnInit {
  taskForm = new FormGroup({
    taskId: new FormControl(''),
    taskTitle: new FormControl('', Validators.required),
    taskDate: new FormControl(),
    taskStatus: new FormControl('', Validators.required),
    taskPriority: new FormControl(),
    assignee: new FormControl(''),
    taskDesc: new FormControl(''),
    projectId: new FormControl(''),
  })
  priorities = ["Lowest", "Low", "Medium", "High", "Highest"];

  private dialog: MatDialog = inject(MatDialog);

  authService: Auth = inject(Auth);
  user : WritableSignal<User> = signal(new User());

  commentService: CommentService = inject(CommentService);
  comments : WritableSignal<Comment[]> = signal([]);
  currentComment = signal("");

  constructor(
    private dialogRef: MatDialogRef<TaskDialog>,
    @Inject(MAT_DIALOG_DATA) public data: TaskDialogData
  ) {
  }

  ngOnInit(): void {
    this.taskForm.patchValue({
      taskId: this.data.taskId,
      taskStatus: this.data.taskStatus,
      taskTitle: this.data.taskTitle,
      taskPriority: this.data.taskPriority,
      taskDate: this.data.taskDate,
      assignee: this.data.assignee ? this.data.assignee : '',
      taskDesc: this.data.taskDesc,
      projectId: this.data.projectId
    });
    console.log(this.data.taskStatus)
    this.getUser();
    this.getComments();
  }

  getUser() {
    let username = this.authService.getUsername();

    this.authService.getUserByName(username!).subscribe({
      next: (data) => {
        this.user.set(data);
      }
    })
  }

  createComment(event?: Event) {
    if(this.currentComment()) {
      let comment = new Comment();
      comment.comment = this.currentComment();
      comment.taskId = this.data.taskId;
      comment.createdAt = new Date();
      comment.userId = this.user().userId;

      this.commentService.createComment(comment).subscribe({
        next: (data) => {
          data.userName = this.user().username;
          this.comments.update(value => [data, ...value]);
          this.currentComment.set("");
          if (event) {
            (event.target as HTMLTextAreaElement).blur();
          }
        },
        error: (error: HttpErrorResponse) => {
          console.error(error);
          //TODO
        },
      })
    }
  }

  getComments() {
    if(this.data.taskId) {
      this.commentService.getAllComments(this.data.taskId)
      .pipe(map((commentArrray) => commentArrray.map((comment: Comment) =>{
        this.getCommentUsername(comment);
        return comment;
      })))
      .subscribe({
        next: (data: Comment[]) => {
          this.comments.set(data);
        },
        error: (error: HttpErrorResponse) => {
          console.error(error);
          //TODO
        },
      })
    }
  }

  setCommentToSuggestion(suggestion: string) {
    this.currentComment.update(c => c + suggestion)
  }

  getColorForUser(id: string): string {
    return this.authService.getColorForUser(id);
  }

  getCommentUsername(comment: Comment): void {
    this.authService.getUserNameById(comment.userId).subscribe({
      next: (data) => {
        comment.userName = data.message;
      },
      error: (error) => {
        console.error('Error fetching user:', error);
        comment.userName = 'Unknown User';
      }
    });
  }

  deleteComment(comment : Comment,  event : Event) {
    event.stopPropagation();
    const dialogRef = this.dialog.open(ConfirmationDialog, {
      disableClose: false
    });
    dialogRef.componentInstance.title = "Delete comment?"
    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.commentService.deleteComment(comment.commentId).subscribe({
          next: () => {
            this.comments.update(value => {
              let indexOfUpdatedComment = value.findIndex(item => item.commentId === comment.commentId);
              value.splice(indexOfUpdatedComment, 1);
              return [...value];
            });
          },
          error: (error: HttpErrorResponse) => {
            console.error(error.error.message);
          },
        })
      }
    });
  }

  assignToMe() {
    this.taskForm.patchValue({
      assignee: this.user().userId,
    });
  }


  onSubmit(): void {
    if (this.taskForm.valid) {
      this.dialogRef.close(this.taskForm.value);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
