import { Component } from '@angular/core';
import { ProfileComponent } from '../../components/profile/profile.component';
import { LayoutComponent } from '../../components/layout/layout.component';
import NavbarData from './navbar-data';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    ProfileComponent,
    LayoutComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  navbarData = NavbarData;
}