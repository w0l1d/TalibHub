import {Component, Input} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {AuthService} from "../../services/auth.service";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-side-nav-bar',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    RouterLink
  ],
  templateUrl: './side-nav-bar.component.html',
  styleUrl: './side-nav-bar.component.css'
})
export class SideNavBarComponent {
  collapsed:boolean = false;
  @Input() navBarData!: any;
  toggleCollapsed(): void {
    this.collapsed = !this.collapsed;
  }

  constructor(
    private authService: AuthService
  ) {
  }

  logout(): void {
    this.authService.logout();
  }
}
