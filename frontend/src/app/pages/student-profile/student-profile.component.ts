import {Component} from '@angular/core';
import {LayoutComponent} from '../../components/layout/layout.component';
import NavbarData from './navbar-data';
import {StudentProfileService} from '../../services/student-profile.service';
import {HttpClientModule} from '@angular/common/http';
import Profile from '../../models/profile';
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import Education from "../../models/education";
import Institut from "../../models/institut";

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

  ],
  providers: [
    StudentProfileService,
    DatePipe
  ],
  templateUrl: './student-profile.component.html',
  styleUrl: './student-profile.component.css'
})
export class StudentProfileComponent {
  navbarData = NavbarData;
  studentProfile?: Profile;
  educationForm!: FormGroup;
  addExpModalCollapsed : boolean = true;
  addEducModalCollapsed : boolean = true;

  constructor(
    private studentProfileService: StudentProfileService,
    private formBuilder: FormBuilder,
    private datePipe: DatePipe
  ) { }

  formatDate(date: Date): string {
    return this.datePipe.transform(date, 'yyyy-MM') || ''; // Format date to YYYY-MM
  }
  ngOnInit() {
    this.studentProfileService.getStudentProfile().subscribe(data => {
      this.studentProfile = data;
      console.log(this.studentProfile);
    });
    this.educationForm = this.formBuilder.group({
      title: ['', [Validators.required]],
      studyField: ['', [Validators.required]],
      startAt: ['', [Validators.required]],
      endAt: ['', [Validators.required]],
      institutname: ['', [Validators.required]],
      institutwebsite: ['', [Validators.required]],
      description: ['', [Validators.required]]
    });

  }

  onSubmit(): void {
    console.warn('Your form has been submitted', this.educationForm);
    if(this.educationForm.valid) {
      // create institut object
      const institut :Institut = {
        name: this.educationForm.get('institutname')?.value,
        website: this.educationForm.get('institutwebsite')?.value
      };
      // create education object
      const education :Education = {
        title: this.educationForm.get('title')?.value,
        studyField: this.educationForm.get('studyField')?.value,
        description: this.educationForm.get('description')?.value,
        startAt: this.formatDate(this.educationForm.get('startAt')?.value),
        endAt: this.formatDate(this.educationForm.get('endAt')?.value),
        institut: institut
      };
      console.log(education);
      this.studentProfileService.addEducation(this.studentProfile!.id ,education).subscribe((data: Profile) => {
        console.log(data);
        this.studentProfile?.educations.push(education);
        this.toggleAddEducModal();
      });
    }
  }

  toggleAddExpModal() {
    this.addExpModalCollapsed = !this.addExpModalCollapsed;
  }
  toggleAddEducModal() {
    this.addEducModalCollapsed = !this.addEducModalCollapsed;
  }

}
