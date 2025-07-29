
import { Component, computed, inject, Inject, Signal, signal, WritableSignal } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenu, MatMenuTrigger, MatMenuItem } from '@angular/material/menu';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule  } from '@angular/common';
import { Member } from '../../models/member';
import { CollaboratorService } from '../../services/collaborator.service';
import { ErrorPopup } from '../error-popup/error-popup';
import { InfoPopup } from '../info-popup/info-popup';
import { ConfirmationDialog } from '../comformation-dialog/comformation-dialog';
import { KeycloakService } from '../../services/keycloak.service.';

interface ManageMembersData {
  collaborators: Member[];
}

@Component({
  selector: 'app-manage-members-dialog',
  imports: [MatProgressSpinnerModule, MatIconModule, MatButtonModule, MatMenu, MatMenuTrigger, CommonModule, MatMenuItem],
  templateUrl: './manage-members-dialog.html',
  styleUrl: './manage-members-dialog.css'
})
export class ManageMembersDialog {

  keycloakService: KeycloakService = inject(KeycloakService);
  formValid = signal(false);
  message = signal("");
  successMessage = signal("");
  isLoading = signal(true);

  private dialog: MatDialog = inject(MatDialog);
  private collaborationService: CollaboratorService = inject(CollaboratorService);
  private collaborators : WritableSignal<Member[]>;
  members : Signal<Member[]>;;

  constructor(
    private dialogRef: MatDialogRef<ManageMembersDialog>,
    @Inject(MAT_DIALOG_DATA) public data: ManageMembersData
  ) {
    this.collaborators = signal(this.data.collaborators)
    this.members = computed(() => this.data.collaborators);
  }

  ngOnInit(): void {
    let count = 0;
    const length = this.data.collaborators.length - 1;
    this.data.collaborators.forEach(c => {
       this.keycloakService.getUserById(c.userId).subscribe({
        next: data => {
          c.userName = data.username;
        },
        error: error => {
          console.error(error.error.message);
          this.isLoading.set(false);
        },
        complete: () => {
          if(count++ == length) {
            this.isLoading.set(false);
            this.collaborators.set([...this.collaborators()]); // Trigger update
          }
        }
      })
    });
    console.log(this.data.collaborators);
  }

  deleteMember(member : Member,  event : Event) {
    event.stopPropagation();
    const dialogRef = this.dialog.open(ConfirmationDialog, {
      disableClose: false
    });
    dialogRef.componentInstance.title = "Remove member?"
    dialogRef.componentInstance.confirmMessage = `Are you sure you want to remove: ${member.userName} from this project?`

    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.collaborationService.deleteCollaborator(member.collaboratorId).subscribe({
          next: data => {
            let index = this.data.collaborators.findIndex(c => c.collaboratorId === member.collaboratorId);
            this.data.collaborators.splice(index, 1);

            this.collaborators.set([...this.collaborators()]); // Trigger update
            const dialogRef = this.dialog.open(InfoPopup, {
              disableClose: false
            });
            dialogRef.componentInstance.title = "Successfully removed member from project!"
          },
          error: (error: HttpErrorResponse) => {
            const dialogRef = this.dialog.open(ErrorPopup, {
              disableClose: false
            });
            dialogRef.componentInstance.title = "Error removing member from project!"
            dialogRef.componentInstance.errorMessage = error.error.message;
          },
        })
      }
    });
  }

  getColorForUser(id: string): string {
    return this.keycloakService.getColorForUser(id);
  }

  onCancel(): void {
    this.dialogRef.close(this.members());
  }
}
