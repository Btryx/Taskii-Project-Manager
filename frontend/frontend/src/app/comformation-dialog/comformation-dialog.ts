import { Component } from '@angular/core';
import {MatDialogModule, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-comformation-dialog',
  imports: [ MatDialogModule ],
  templateUrl: './comformation-dialog.html',
  styleUrl: './comformation-dialog.css'
})
export class ConfirmationDialog {
  constructor(public dialogRef: MatDialogRef<ConfirmationDialog>) {}

  public confirmMessage : string = '';
  public title : string = '';
}
