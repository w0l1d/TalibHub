import {Component} from '@angular/core';
import {LayoutComponent} from '../../components/layout/layout.component';
import NavbarData from './navbar-data';
import {StudentProfileService} from '../../services/student-profile.service';
import {HttpClientModule} from '@angular/common/http';
import Profile from '../../models/profile';

@Component({
  selector: 'app-student-profile',
  standalone: true,
  imports: [
    LayoutComponent,
    HttpClientModule
  ],
  providers: [StudentProfileService],
  templateUrl: './student-profile.component.html',
  styleUrl: './student-profile.component.css'
})
export class StudentProfileComponent {
  navbarData = NavbarData;
  studentProfile?: Profile;

  constructor(
    private studentProfileService: StudentProfileService,
  ) { }

  ngOnInit() {
    this.studentProfileService.getStudentProfile().subscribe(data => {
      this.studentProfile = data;
      console.log(this.studentProfile);
    });

  }

}
