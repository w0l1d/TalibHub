import {Component, Input} from '@angular/core';
import {DataTableComponent} from '../data-table/data-table.component';
import {ProfileComponent} from '../profile/profile.component';
import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {AuthService} from "../../services/auth.service";
import {RouterLink} from "@angular/router";
import User from "../../models/user";
import {environment as env} from "../../../environments/environment.development";

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    ProfileComponent,
    DataTableComponent,
    NgOptimizedImage,
    NgForOf,
    NgIf,
    RouterLink
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

  user?: User;
  baseUrl: string = '';

  constructor(
    private authService: AuthService
  ) {
    this.baseUrl = env.api;
  }

  ngOnInit(): void {
    this.user = this.authService.getLoggedUser();
    console.log("user", this.user);
  }
  logout(): void {
    this.authService.logout();
  }
}
