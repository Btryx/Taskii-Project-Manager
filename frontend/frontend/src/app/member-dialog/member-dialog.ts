import { Component, inject, Inject, signal, WritableSignal } from '@angular/core';
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

interface MemberDialogData {
  userId: string;
  role: string;
  collaborators: User[];
}

@Component({
  selector: 'app-member-dialog',
  imports: [MatFormFieldModule, CommonModule, MatSelectModule, ReactiveFormsModule, MatInputModule, MatButtonModule, MatDatepickerModule, FormsModule, MatNativeDateModule, MatIconModule],
  templateUrl: './member-dialog.html',
  styleUrl: './member-dialog.css',
  providers: [provideNativeDateAdapter()],
})
export class MemberDialog {
  memberForm = new FormGroup({
    member: new FormControl('', Validators.required),
    selectedRole: new FormControl('Contributor', Validators.required),
  })

  authService: Auth = inject(Auth);
  formValid = signal(false);
  message = signal("");
  successMessage = signal("");

  constructor(
    private dialogRef: MatDialogRef<MemberDialog>,
    @Inject(MAT_DIALOG_DATA) public data: MemberDialogData
  ) {
  }

  ngOnInit(): void {
    this.memberForm.patchValue({
      member: this.data.userId,
      selectedRole: this.data.role
    });
  }

  checkIfUserExists(event: Event) {
    event.preventDefault();
    if (this.memberForm.value.member) {
      let name = this.memberForm.value.member;
      if(this.isAlreadyMember(name)) {
        this.successMessage.set( ``);
        this.message.set( `This user is already a collaborator on the project.`)
      } else {
        this.authService.getUserIdByName(name).subscribe({
          next: (data) => {
            this.successMessage.set( `User found! Click save to add ${name} to the project`);
            this.message.set( ``)
            this.memberForm.value.member = data.message;
            if(this.memberForm.value.selectedRole) {
              this.formValid.set(true)
            } else {
              this.formValid.set(false)
            }
          },
          error: () => {
            this.formValid.set(false)
            this.successMessage.set( ``);
            this.message.set( `User not found! `)
          }
        })
      }

    }
  }

  private isAlreadyMember(username: string) {
    return this.data.collaborators.some(col => col.username === username);
  }

  onSubmit(): void {
    if (this.memberForm.valid) {
      this.dialogRef.close(this.memberForm.value);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
