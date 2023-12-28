import { Component } from '@angular/core';
import { ProfileComponent } from '../../components/profile/profile.component';
import { LayoutComponent } from '../../components/layout/layout.component';
import NavbarData from './navbar-data';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {environment as env} from "../../../environments/environment.development";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    ProfileComponent,
    LayoutComponent
    HttpClientModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  navbarData = NavbarData;

  constructor(
    private http: HttpClient
  ) {
  }

  ngOnInit() {
    this.http.get(`${env.api}/students`).subscribe(data => {
      console.log(data);
    });
  }
}
