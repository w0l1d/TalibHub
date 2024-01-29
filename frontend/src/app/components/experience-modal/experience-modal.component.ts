import {Component, Input} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import Profile from "../../models/profile";
import {StudentProfileService} from "../../services/student-profile.service";
import Institut from "../../models/institut";
import Experience from "../../models/experience";
import {InstitutService} from "../../services/institut.service";
import {endYears, Month, months, startYears} from "../../models/DateTypes";
import {HttpClientModule} from "@angular/common/http";
import {FileUploadService} from "../../services/file-upload.service";

@Component({
  selector: 'app-experience-modal',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [
    DatePipe,
    InstitutService,
    FileUploadService
  ],
  templateUrl: './experience-modal.component.html',
  styleUrl: './experience-modal.component.css'
})
export class ExperienceModalComponent {

  @Input() studentProfile?: Profile;
  @Input() studentProfileService!: StudentProfileService;
  expModalCollapsed!: boolean;
  addInstitutCollapsed!: boolean;
  experienceForm!: FormGroup;
  instituts: Institut[] = [];
  @Input() title?: string;
  @Input() operation?: string;
  @Input() experience?: Experience;
  myMonths: Month[] = months;
  myStartYears: number[] = startYears;
  myEndYears: number[] = endYears;

  constructor(
    private formBuilder: FormBuilder,
    private institutService: InstitutService
  ) {
    this.expModalCollapsed = true;
    this.addInstitutCollapsed = true;
  }

  ngOnInit(): void {
    this.buildExperienceForm();
  }

  public toggleAddExpModal(): void {
    if (this.expModalCollapsed) {
      this.getInstituts();
      this.resetForm();
      this.addInstitutCollapsed = true;
      if (this.experience !== undefined) {
        console.log('I am here');
        this.fillForm()
      }
    }
    this.expModalCollapsed = !this.expModalCollapsed;
  }

  public toggleAddInstitut(): void {
    this.addInstitutCollapsed = !this.addInstitutCollapsed;
    if (this.addInstitutCollapsed) {
      this.experienceForm.get('institutId')?.setValue(this.experience?.institut?.id);
    } else {
      this.experienceForm.patchValue({
        institutId: '',
        institutname: '',
        institutwebsite: ''
      });
    }
  }

  onCreate():void{
    if (this.experienceForm.invalid) {
      console.log('form invalid ' + this.experienceForm);
      return;
    }
    console.log('form ' + this.experienceForm);
    let institut: Institut;
    // create institut object
    if (this.experienceForm.get('institutId')?.value === '') {
      institut  = {
        name: this.experienceForm.get('institutname')?.value,
        website: this.experienceForm.get('institutwebsite')?.value,
        image: this.experienceForm.get('institutimage')?.value
      };
    } else {
      institut  = {
        id: this.experienceForm.get('institutId')?.value,
        name: this.experienceForm.get('institutname')?.value,
        website: this.experienceForm.get('institutwebsite')?.value
      };
    }
    // create experience object
    const experience: Experience = {
      title: this.experienceForm.get('title')?.value,
      description: this.experienceForm.get('description')?.value,
      startAt: this.formatDate(this.experienceForm.get('startAtMonth')?.value, this.experienceForm.get('startAtYear')?.value),
      endAt: this.formatDate(this.experienceForm.get('endAtMonth')?.value, this.experienceForm.get('endAtYear')?.value),
      institut: institut
    };
    console.log(experience);
    this.studentProfileService.addExperience(this.studentProfile!.id ,experience).subscribe((data: Profile) => {
      console.log(data);
      if (this.studentProfile?.experiences !== undefined) {
        this.studentProfile.experiences = data.experiences;
      }
      this.toggleAddExpModal();
    });

  }

  public onUpdate():void {
    if (this.experienceForm.invalid) {
      return;
    }
    let institut: Institut;
    // create institut object
    if (this.experienceForm.get('institutId')?.value === '') {
      institut = {
        name: this.experienceForm.get('institutname')?.value,
        website: this.experienceForm.get('institutwebsite')?.value,
        image: this.experienceForm.get('institutimage')?.value
      };
    } else {
      institut  = {
        id: this.experienceForm.get('institutId')?.value,
        name: this.experienceForm.get('institutname')?.value,
        website: this.experienceForm.get('institutwebsite')?.value
      };
    }
    // create experience object
    const experience: Experience = {
      id: this.experience?.id,
      title: this.experienceForm.get('title')?.value,
      description: this.experienceForm.get('description')?.value,
      startAt: this.formatDate(this.experienceForm.get('startAtMonth')?.value,
        this.experienceForm.get('startAtYear')?.value),
      endAt: this.formatDate(this.experienceForm.get('endAtMonth')?.value,
        this.experienceForm.get('endAtYear')?.value),
      institut: institut
    };
    console.log(experience);
    this.studentProfileService.updateExperience(this.studentProfile!.id,experience).subscribe((data: Profile) => {
      console.log(data);
      if (this.studentProfile?.experiences !== undefined) {
        this.studentProfile.experiences = data.experiences;
      }
      this.toggleAddExpModal();
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

  private buildExperienceForm():void {
    this.experienceForm = this.formBuilder.group({
      title: ['', Validators.required],
      startAtMonth: ['', Validators.required],
      startAtYear: ['', Validators.required],
      endAtMonth: ['', Validators.required],
      endAtYear: ['', Validators.required],
      institutId: [''],
      institutname: [''],
      institutwebsite: [''],
      institutimage: [null],
      description: ['', Validators.required]
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
    this.experienceForm.patchValue({ institutimage: file});
    console.log(this.experienceForm.get('institutimage')?.value);
  }

  private getInstituts():void {
    this.institutService.getInstituts().subscribe(data => {
      this.instituts = data;
    });
  }

  private resetForm() {
    this.experienceForm.reset();
  }

  private fillForm() {
    this.experienceForm.patchValue({
      title: this.experience?.title,
      description: this.experience?.description,
      startAtMonth: this.getMonth(this.experience?.startAt as string),
      startAtYear: this.getYear(this.experience?.startAt as string),
      endAtMonth: this.getMonth(this.experience?.endAt as string),
      endAtYear: this.getYear(this.experience?.endAt as string),
      institutId: this.experience?.institut?.id,
      institutname: this.experience?.institut?.name,
      institutwebsite: this.experience?.institut?.website,
      institutimage: this.experience?.institut?.image
    });
  }
}
