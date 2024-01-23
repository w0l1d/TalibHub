import {Component, Input} from '@angular/core';
import {DataTableComponent} from '../data-table/data-table.component';
import {ProfileComponent} from '../profile/profile.component';
import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {AuthService} from "../../services/auth.service";
import { RouterLink } from '@angular/router';
import {SideNavBarComponent} from "../side-nav-bar/side-nav-bar.component";

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    ProfileComponent,
    DataTableComponent,
    NgOptimizedImage,
    NgForOf,
    NgIf,
    RouterLink,
    SideNavBarComponent
  ],
  providers: [AuthService],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent {
  collapsed:boolean = false;
  @Input() navBarData!: any;
  toggleCollapsed(): void {
    this.collapsed = !this.collapsed;
  }
}
