import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-error-popup',
  imports: [],
  templateUrl: './error-popup.html',
  styleUrl: './error-popup.css'
})
export class ErrorPopup {
  constructor(public dialogRef: MatDialogRef<ErrorPopup>) {}

  public errorMessage : string = '';
  public title : string = '';
}
