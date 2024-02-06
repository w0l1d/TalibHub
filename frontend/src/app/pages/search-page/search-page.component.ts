import {Component, inject} from '@angular/core';
import {LayoutComponent} from "../../components/layout/layout.component";
import {NgForOf, NgIf} from "@angular/common";
import Profile from "../../models/profile";
import {StudentProfileService} from "../../services/student-profile.service";
import {Router} from "@angular/router";
import {environment as env} from "../../../environments/environment.development";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {AuthService} from "../../services/auth.service";
import NavbarLink from "../../models/NavbarLink";

@Component({
  selector: 'app-search-page',
  standalone: true,
  imports: [
    LayoutComponent,
    NgForOf,
    HttpClientModule,
    NgIf
  ],
  providers: [
    StudentProfileService,
    HttpClient
  ],
  templateUrl: './search-page.component.html',
  styleUrl: './search-page.component.css'
})
export class SearchPageComponent {
  navbarData: NavbarLink[] | undefined;
  searchTerm: string = '';
  studentProfiles!: Profile[];
  baseUrl: string = '';

  constructor(
    private studentProfileService: StudentProfileService,
    private router: Router
  ) {
    this.baseUrl = env.api;
    this.setNavbarData();
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

  private setNavbarData(): void {
    this.navbarData = [
      { route: '/home', icon: 'fa-solid fa-house', label: 'Home' , highlight: false},
      { route: '/search', icon: 'fa-solid fa-search', label: 'Search', highlight: true},
      { route: '/news', icon: 'fa-solid fa-newspaper', label: 'News', highlight: false },
      { route: '/logout', icon: 'fa-solid fa-right-from-bracket', label: 'Logout', highlight: false },
    ];

    if (inject(AuthService).isManager()) {
      this.navbarData.splice(2, 0, { route: '/studentManagement', icon: 'fa-sharp fa-solid fa-shield-halved', label: 'Admin', highlight: false });
    } else if (inject(AuthService).isStudent()) {
      this.navbarData.splice(1, 0, { route: '/studentProfile', icon: 'fa-solid fa-user', label: 'Profile', highlight: false });
    }
  }
}
