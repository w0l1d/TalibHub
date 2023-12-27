import { Component, Input } from '@angular/core';
import { DataTableComponent } from '../data-table/data-table.component';
import { ProfileComponent } from '../profile/profile.component';
import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    ProfileComponent,
    DataTableComponent,
    NgOptimizedImage,
    NgForOf,
    NgIf
  ],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent {
  @Input() navBarData!: any;
}
