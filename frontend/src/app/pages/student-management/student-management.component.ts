import { Component } from '@angular/core';
import { LayoutComponent } from '../../components/layout/layout.component';
import { DataTableComponent } from '../../components/data-table/data-table.component';

@Component({
  selector: 'app-student-management',
  standalone: true,
  imports: [
    LayoutComponent,
    DataTableComponent
  ],
  templateUrl: './student-management.component.html',
  styleUrl: './student-management.component.css'
})
export class StudentManagementComponent {

}
