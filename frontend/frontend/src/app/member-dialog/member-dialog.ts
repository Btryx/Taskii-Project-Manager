import { Component, inject, Inject, signal, WritableSignal } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select'
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule } from '@angular/material/core';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { Auth } from '../service/auth.service';
import { CommonModule  } from '@angular/common';
import { User } from '../model/user';
import { Member } from '../model/member';

interface MemberDialogData extends Member {
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
    collaboratorId: new FormControl(''),
    userId: new FormControl(''),
    userName: new FormControl(''),
    projectId: new FormControl(''),
    role: new FormControl('', Validators.required),
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
      collaboratorId: this.data.collaboratorId,
      projectId: this.data.projectId,
      userId: this.data.userId,
      role: this.data.role
    });
  }

  checkIfUserExists(event: Event) {
    event.preventDefault();

    if (this.memberForm.value.userName) {
      let name = this.memberForm.value.userName;
      this.authService.getUserIdByName(name).subscribe({
        next: (data) => {
          this.userIsFound(name, data);
        },
        error: () => {
          this.userNotFound();
        }
      })
    }
  }

  private userIsFound(name: string, data: any) {
    if (this.isAlreadyMember(name)) {
      this.successMessage.set(``);
      this.message.set(`This user is already a collaborator on the project.`);
    } else {
      this.successMessage.set(`User found! Click save to add ${name} to the project`);
      this.message.set(``);
      this.memberForm.patchValue({
        userId: data.message
      });
    }

    if (this.memberForm.value.role) {
      this.formValid.set(true);
    } else {
      this.formValid.set(false);
    }
  }

  private userNotFound() {
    this.formValid.set(false);
    this.successMessage.set(``);
    this.message.set(`User not found! `);
  }

  private isAlreadyMember(userName: string) {
    return this.data.collaborators.some(col => col.username === userName);
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
