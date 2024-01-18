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
  ],
  templateUrl: './student-profile.component.html',
  styleUrl: './student-profile.component.css'
})
export class StudentProfileComponent {
  navbarData = NavbarData;
  studentProfile?: Profile;



  constructor(
    protected studentProfileService: StudentProfileService,
  ) { }


  ngOnInit() {
    this.getStudentProfile();

  }

  private getStudentProfile(): void {
    this.studentProfileService.getStudentProfile().subscribe(data => {
      this.studentProfile = data;
      console.log(this.studentProfile);
    });
  }


  handleProfileCreation(profileForm: ProfileForm) {
    console.log(profileForm);
  }

  handleProfileUpdate(profileForm: ProfileForm) {
    console.log(profileForm);
  }
}
