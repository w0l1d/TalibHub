import { Component } from '@angular/core';
import { DataTableComponent } from '../data-table/data-table.component';
import { ProfileComponent } from '../profile/profile.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    ProfileComponent,
    DataTableComponent
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent {

}
