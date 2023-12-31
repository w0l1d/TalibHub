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
import {InstitutService} from "../../services/institut.service";

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
    InstitutService,
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
  instituts: Institut[] = [];

  constructor(
    private studentProfileService: StudentProfileService,
    private institutService: InstitutService,
    private formBuilder: FormBuilder,
    private datePipe: DatePipe
  ) { }


  ngOnInit() {
    this.getStudentProfile();
    this.buildEducationForm();

  }



  public onSubmit(): void {
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

  public toggleAddExpModal() {
    if (this.addEducModalCollapsed) {
      this.getInstituts();
    }
    this.addExpModalCollapsed = !this.addExpModalCollapsed;
  }
  public toggleAddEducModal() {
    if (this.addEducModalCollapsed) {
      this.getInstituts();
    }
    this.addEducModalCollapsed = !this.addEducModalCollapsed;
  }

  private getStudentProfile(): void {
    this.studentProfileService.getStudentProfile().subscribe(data => {
      this.studentProfile = data;
      console.log(this.studentProfile);
    });
  }

  private buildEducationForm() {
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

  private formatDate(date: Date): string {
    return this.datePipe.transform(date, 'yyyy-MM') || ''; // Format date to YYYY-MM
  }

  private getInstituts(): void {
    this.institutService.getInstituts().subscribe((data: any) => {
      console.log('Received Data:', data); // Log the received data
      if (data && data._embedded && data._embedded.instituts) {
        this.instituts = data._embedded.instituts;
        console.log('Institutes:', this.instituts); // Log the extracted institutes
      } else {
        console.error('Data structure mismatch or missing data');
      }
    });
  }

}
