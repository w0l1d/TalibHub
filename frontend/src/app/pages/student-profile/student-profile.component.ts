import { Component } from '@angular/core';
import { LayoutComponent } from '../../components/layout/layout.component';
import NavbarData from './navbar-data';

@Component({
  selector: 'app-student-profile',
  standalone: true,
  imports: [
    LayoutComponent
  ],
  templateUrl: './student-profile.component.html',
  styleUrl: './student-profile.component.css'
})
export class StudentProfileComponent {
  navbarData = NavbarData;
   
}
