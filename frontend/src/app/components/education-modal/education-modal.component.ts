import {Component, Input} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import Institut from "../../models/institut";
import Education from "../../models/education";
import Profile from "../../models/profile";
import {DatePipe, NgIf} from "@angular/common";
import {InstitutService} from "../../services/institut.service";
import {StudentProfileService} from "../../services/student-profile.service";

@Component({
  selector: 'app-education-modal',
  standalone: true,
  imports: [
    DatePipe,
    ReactiveFormsModule,
    NgIf
  ],
  providers: [
    DatePipe
  ],
  templateUrl: './education-modal.component.html',
  styleUrl: './education-modal.component.css'
})
export class EducationModalComponent {

  @Input() educModalCollapsed!: boolean;
  @Input() studentProfile?: Profile;
  @Input() studentProfileService!: StudentProfileService;
  educationForm!: FormGroup;
  instituts: Institut[] = [];
  constructor(
    private formBuilder: FormBuilder,
    private institutService: InstitutService,
    private datePipe: DatePipe
  ) {
    this.educModalCollapsed = true;
  }

  ngOnInit() {
    this.buildEducationForm();
  }

  public toggleAddEducModal() {
    if (this.educModalCollapsed) {
      this.getInstituts();
      this.resetForm();
    }
    this.educModalCollapsed = !this.educModalCollapsed;
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

  private formatDate(date: Date): string {
    return this.datePipe.transform(date, 'yyyy-MM') || ''; // Format date to YYYY-MM
  }

  public getInstituts(): void {
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

  // empty the form
  public resetForm(): void {
    this.educationForm.reset();
  }


}
