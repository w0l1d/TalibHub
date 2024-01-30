import {Component} from '@angular/core';
import {LayoutComponent} from '../../components/layout/layout.component';
import NavbarData from './navbar-data';
import {StudentProfileService} from '../../services/student-profile.service';
import {HttpClientModule} from '@angular/common/http';
import Profile from '../../models/profile';
import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {EducationModalComponent} from "../../components/education-modal/education-modal.component";
import {ExperienceModalComponent} from "../../components/experience-modal/experience-modal.component";
import {ProfileForm, ProfileFormModalComponent} from "../../components/profile-form-modal/profile-form-modal.component";
import {environment as env} from "../../../environments/environment.development";
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-student-profile',
  standalone: true,
  imports: [
    LayoutComponent,
    HttpClientModule,
    NgForOf,
    NgIf,
    FormsModule,
    ReactiveFormsModule,
    EducationModalComponent,
    ExperienceModalComponent,
    NgOptimizedImage,
    ProfileFormModalComponent

  ],
  providers: [
    StudentProfileService,
    AuthService
  ],
  templateUrl: './student-profile.component.html',
  styleUrl: './student-profile.component.css'
})
export class StudentProfileComponent {
  navbarData = NavbarData;
  studentProfile?: Profile;
  profileId: string = '';
  baseUrl: string = '';



  constructor(
    protected studentProfileService: StudentProfileService,
    private authService: AuthService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  )
  {
    this.baseUrl = env.api;
  }


  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.profileId = params['id'] || '';
    });
    console.log("Profile id: " + this.profileId);
    this.profileId == '' ? this.getStudentProfile() : this.getStudentProfileById(this.profileId);
  }

  private getStudentProfile(): void {
    this.studentProfileService.getStudentProfile().subscribe(data => {
      this.studentProfile = data;
      console.log(this.studentProfile);
    });
  }

  private getStudentProfileById(profileId: string): void {
    this.studentProfileService.getStudentProfileById(profileId).subscribe(data => {
      this.studentProfile = data;
      console.log(this.studentProfile);
    });
  }

  isMyProfile(): boolean {
    return this.profileId == '';
  }

  handleProfileCreation(profileForm: ProfileForm) {
    console.log(profileForm);
  }

  handleProfileUpdate(profileForm: ProfileForm) {
    console.log(profileForm);
    this.studentProfileService.updateProfile(this.studentProfile?.id!,profileForm).subscribe(data => {
      console.log(data)
      this.studentProfile = data;
      this.authService.setLoggedUser(this.studentProfile?.student!);
    });
  }

  navigateToReviews(intitutId: string): void {
    //console.log("Navigating to reviews: " + intitutId);
    this.router.navigate(['/companyReviews', intitutId]).then(() => console.log("navigated to reviews"));
  }
}
