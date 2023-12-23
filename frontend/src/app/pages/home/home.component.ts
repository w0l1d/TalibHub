import { Component } from '@angular/core';
import { ProfileComponent } from '../../components/profile/profile.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    ProfileComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
