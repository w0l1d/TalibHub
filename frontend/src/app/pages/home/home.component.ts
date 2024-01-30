import {Component} from '@angular/core';
import {ProfileComponent} from '../../components/profile/profile.component';
import {LayoutComponent} from '../../components/layout/layout.component';
import NavbarData from './navbar-data';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {StudentProfileService} from '../../services/student-profile.service';
import Profile from '../../models/profile';
import {NgFor, NgOptimizedImage} from '@angular/common';
import {Router} from '@angular/router';
import {PostComponent} from '../../components/post/post.component';
import {environment as env} from "../../../environments/environment.development";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    ProfileComponent,
    LayoutComponent,
    HttpClientModule,
    NgFor,
    PostComponent,
    NgOptimizedImage
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
  baseUrl: string = '';

  constructor(
    private studentProfileService: StudentProfileService,
    private router: Router
  ) {
    this.baseUrl = env.api;
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

  navigateToProfile(profileId: string): void {
    console.log("Navigating to profile: " + profileId);
    this.router.navigate(['/talib', profileId]).then(() => console.log("navigated to profile"));
  }
}
