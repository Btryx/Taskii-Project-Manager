import { CommonModule  } from '@angular/common';
import { Project } from '../project';
import { ProjectService } from '../project.service';
import { ChangeDetectionStrategy, Component, effect, inject, OnInit, signal, ViewChild, WritableSignal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { ProjectDialog } from '../project-dialog/project-dialog';
import { MatDialog } from '@angular/material/dialog';
import { MatMenu, MatMenuTrigger, MatMenuItem } from '@angular/material/menu';
import { HttpErrorResponse } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import { Auth } from '../auth.service';
import { Observable } from 'rxjs';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import { ConfirmationDialog } from '../comformation-dialog/comformation-dialog';
import { InfoPopup } from '../info-popup/info-popup';
import { Router } from '@angular/router';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css'],
  imports: [MatIconModule, CommonModule, MatProgressSpinnerModule, MatButtonModule,
     MatPaginatorModule, MatTableModule, MatInputModule, MatFormFieldModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TaskListComponent implements OnInit {

  ngOnInit(): void {
  }

}
