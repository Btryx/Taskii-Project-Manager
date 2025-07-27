import { Component } from '@angular/core';
import {MatDialogModule, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-info-popup',
  imports: [ MatDialogModule ],
  templateUrl: './info-popup.html',
  styleUrl: './info-popup.css'
})
export class InfoPopup {
  constructor(public dialogRef: MatDialogRef<InfoPopup>) {}

  public infoMessage : string = '';
  public title : string = '';
}
