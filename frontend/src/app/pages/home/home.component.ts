import {Component, inject} from '@angular/core';
import {ProfileComponent} from '../../components/profile/profile.component';
import {LayoutComponent} from '../../components/layout/layout.component';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {StudentProfileService} from '../../services/student-profile.service';
import Profile from '../../models/profile';
import {NgFor, NgOptimizedImage} from '@angular/common';
import {Router} from '@angular/router';
import {PostComponent} from '../../components/post/post.component';
import {environment as env} from "../../../environments/environment.development";
import {AuthService} from "../../services/auth.service";
import NavbarLink from "../../models/NavbarLink";


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
  navbarData: NavbarLink[] | undefined;
  searchTerm: string = '';
  studentProfiles!: Profile[];
  baseUrl: string = '';

  constructor(
    private router: Router
  ) {
    this.baseUrl = env.api;
    this.setNavbarData();
  }

  private setNavbarData(): void {
    this.navbarData = [
      { route: '/home', icon: 'fa-solid fa-house', label: 'Home' , highlight: true},
      { route: '/search', icon: 'fa-solid fa-search', label: 'Search', highlight: false},
      { route: '/news', icon: 'fa-solid fa-newspaper', label: 'News', highlight: false },
      { route: '/logout', icon: 'fa-solid fa-right-from-bracket', label: 'Logout', highlight: false },
    ];
    console.log(inject(AuthService).getLoggedUser().email);
    console.log(inject(AuthService).isManager());
    console.log(inject(AuthService).isStudent());

    if (inject(AuthService).isManager()) {
      this.navbarData.splice(2, 0, { route: '/studentManagement', icon: 'fa-sharp fa-solid fa-shield-halved', label: 'Admin', highlight: false });
    } else if (inject(AuthService).isStudent()) {
      this.navbarData.splice(1, 0, { route: '/studentProfile', icon: 'fa-solid fa-user', label: 'Profile', highlight: false });
    }
  }


}
