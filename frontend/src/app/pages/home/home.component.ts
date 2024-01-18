import { Component } from '@angular/core';
import { ProfileComponent } from '../../components/profile/profile.component';
import { LayoutComponent } from '../../components/layout/layout.component';
import NavbarData from './navbar-data';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {environment as env} from "../../../environments/environment.development";
import { StudentProfileService } from '../../services/student-profile.service';
import Profile from '../../models/profile';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    ProfileComponent,
    LayoutComponent,
    HttpClientModule,
    NgFor
  ],
  providers: [
    HttpClient,
    StudentProfileService
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  navbarData = NavbarData;
  searchTerm: string = '';
  studentProfiles!: Profile[];

  constructor(
    private studentProfileService: StudentProfileService
  ) {
  }

  onSearch(): void {
    console.log("Search term: " + this.searchTerm);
    this.studentProfileService.searchStudents(this.searchTerm).subscribe((data: any) => {
      console.log(data);
      this.studentProfiles = data;
    });
  }

  onKey(event: any): void {
    this.searchTerm = event.target.value;
  }
}
