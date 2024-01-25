import {Component, Input} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import Institut from "../../models/institut";
import Education from "../../models/education";
import Profile from "../../models/profile";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {InstitutService} from "../../services/institut.service";
import {StudentProfileService} from "../../services/student-profile.service";
import {Month, months, startYears, endYears} from "../../models/DateTypes";

@Component({
  selector: 'app-education-modal',
  standalone: true,
  imports: [
    DatePipe,
    ReactiveFormsModule,
    NgIf,
    NgForOf
  ],
  providers: [
    DatePipe,
    InstitutService
  ],
  templateUrl: './education-modal.component.html',
  styleUrl: './education-modal.component.css'
})
export class EducationModalComponent {

  @Input() studentProfile?: Profile;
  @Input() studentProfileService!: StudentProfileService;
  educModalCollapsed!: boolean;
  addInstitutCollapsed!: boolean;
  educationForm!: FormGroup;
  instituts: Institut[] = [];
  @Input() title?: string;
  @Input() operation?: string;
  @Input() education?: Education;
  myMonths: Month[] = months;
  myStartYears: number[] = startYears;
  myEndYears: number[] = endYears;


  constructor(
    private formBuilder: FormBuilder,
    private institutService: InstitutService
  ) {
    this.educModalCollapsed = true;
    this.addInstitutCollapsed = true;
  }

  ngOnInit():void {
    this.buildEducationForm();

  }

  public toggleAddEducModal():void {
    if (this.educModalCollapsed) {
      this.getInstituts();
      this.resetForm();
      this.addInstitutCollapsed = true;
      if (this.education !== undefined) {
        console.log('I am here');
        this.fillForm();
      }
    }
    this.educModalCollapsed = !this.educModalCollapsed;
  }

  public toggleAddInstitut():void {
    this.addInstitutCollapsed = !this.addInstitutCollapsed;
    if (this.addInstitutCollapsed) {
      this.educationForm.get('institutId')?.setValue(this.education?.institut?.id);
    } else {
      this.educationForm.patchValue({
        institutId: '',
        institutname: '',
        institutwebsite: ''
      });
    }
  }

  public onCreate(): void {
    console.warn('Your form has been submitted', this.educationForm);
    if(this.educationForm.invalid) {
      return;
    }
    let institut: {};
    // create institut object
    if (this.educationForm.get('institutId')?.value === '') {
      institut  = {
        name: this.educationForm.get('institutname')?.value,
        website: this.educationForm.get('institutwebsite')?.value,
        image: this.educationForm.get('institutimage')?.value
      };
    } else {
      institut  = {
        id: this.educationForm.get('institutId')?.value,
        name: this.educationForm.get('institutname')?.value,
        website: this.educationForm.get('institutwebsite')?.value
      };
    }
    // create education object
    const education :Education = {
      title: this.educationForm.get('title')?.value,
      studyField: this.educationForm.get('studyField')?.value,
      description: this.educationForm.get('description')?.value,
      startAt: this.formatDate(this.educationForm.get('startAtMonth')?.value,
        this.educationForm.get('startAtYear')?.value),
      endAt: this.formatDate(this.educationForm.get('endAtMonth')?.value,
        this.educationForm.get('endAtYear')?.value),
      institut: institut
    };
    console.log(education);
    this.studentProfileService.addEducation(this.studentProfile!.id ,education).subscribe((data: Profile) => {
      console.log(data);
      if (this.studentProfile?.educations !== undefined) {
        this.studentProfile.educations = data.educations;
      }
      this.toggleAddEducModal();
    });
  }

  public onUpdate(): void {
    console.warn('Your form has been submitted', this.educationForm);
    if(this.educationForm.invalid) {
      return;
    }
    let institut: {};
    // create institut object
    if (this.educationForm.get('institutId')?.value === '') {
      institut  = {
        name: this.educationForm.get('institutname')?.value,
        website: this.educationForm.get('institutwebsite')?.value,
        image: this.educationForm.get('institutimage')?.value
      };
    } else {
      institut  = {
        id: this.educationForm.get('institutId')?.value,
        name: this.educationForm.get('institutname')?.value,
        website: this.educationForm.get('institutwebsite')?.value
      };
    }
    // create education object
    const education :Education = {
      id: this.education?.id,
      title: this.educationForm.get('title')?.value,
      studyField: this.educationForm.get('studyField')?.value,
      description: this.educationForm.get('description')?.value,
      startAt: this.formatDate(this.educationForm.get('startAtMonth')?.value,
        this.educationForm.get('startAtYear')?.value),
      endAt: this.formatDate(this.educationForm.get('endAtMonth')?.value,
        this.educationForm.get('endAtYear')?.value),
      institut: institut
    };
    console.log(education);
    this.studentProfileService.updateEducation(this.studentProfile!.id ,education).subscribe((data: Profile) => {
      console.log(data);
      if (this.studentProfile?.educations !== undefined) {
        this.studentProfile.educations = data.educations;
      }
      this.toggleAddEducModal();
    });

  }

  private formatDate(month:number, year:number): string {
    if (month < 10)
      return (year + '-0' + month) as string;
    return (year + '-' + month) as string;
  }

  // get month from yyyy-MM date
  private getMonth(date: string): number {
    return +date.split('-')[1];
  }

  // get year from yyyy-MM date
  private getYear(date: string): number {
    return +date.split('-')[0];
  }

  private buildEducationForm():void {
    this.educationForm = this.formBuilder.group({
      title: ['', [Validators.required]],
      studyField: ['', [Validators.required]],
      startAtMonth: ['', Validators.required],
      startAtYear: ['', Validators.required],
      endAtMonth: ['', Validators.required],
      endAtYear: ['', Validators.required],
      institutId: [''],
      institutname: [''],
      institutwebsite: [''],
      institutimage: [null],
      description: ['', [Validators.required]],
    });
  }

  onImagePicked(event: Event): void {
    const file:File = (event.target as HTMLInputElement).files?.[0]!; // Here we use only the first file (single file)
    const maxFileSize = 1024 * 1024 * 5; // 5MB
    console.log(file);
    if (file.size > maxFileSize) {
      alert('File size exceeds 5MB');
      return;
    }
    this.educationForm.patchValue({ institutimage: file});
    console.log(this.educationForm.get('institutimage')?.value);
  }

  public getInstituts(): void {
    this.institutService.getInstituts().subscribe((data: Institut[]) => {
      console.log('Received Data:', data); // Log the received data
      this.instituts = data; // Assign the received data to the instituts property
    });
  }

  // empty the form
  public resetForm(): void {
    this.educationForm.reset();
  }

  // fill the form with the education data
  public fillForm(): void {
    this.educationForm.patchValue({
      title: this.education?.title,
      studyField: this.education?.studyField,
      startAtMonth: this.getMonth(this.education?.startAt as string),
      startAtYear: this.getYear(this.education?.startAt as string),
      endAtMonth: this.getMonth(this.education?.endAt as string),
      endAtYear: this.getYear(this.education?.endAt as string),
      institutId: this.education?.institut?.id,
      institutname: this.education?.institut?.name,
      institutwebsite: this.education?.institut?.website,
      institutimage: this.education?.institut?.image,
      description: this.education?.description
    });
  }
}
