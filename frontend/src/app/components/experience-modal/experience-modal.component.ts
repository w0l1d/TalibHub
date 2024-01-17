import {Component, Input} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import Profile from "../../models/profile";
import {StudentProfileService} from "../../services/student-profile.service";
import Institut from "../../models/institut";
import Experience from "../../models/experience";
import {InstitutService} from "../../services/institut.service";

@Component({
  selector: 'app-experience-modal',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule
  ],
  providers: [
    DatePipe,
    InstitutService
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
  constructor(
    private formBuilder: FormBuilder,
    private institutService: InstitutService,
    private datePipe: DatePipe
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
        website: this.experienceForm.get('institutwebsite')?.value
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
      startAt: this.formatDate(this.experienceForm.get('startAt')?.value),
      endAt: this.formatDate(this.experienceForm.get('endAt')?.value),
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
        website: this.experienceForm.get('institutwebsite')?.value
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
      startAt: this.formatDate(this.experienceForm.get('startAt')?.value),
      endAt: this.formatDate(this.experienceForm.get('endAt')?.value),
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

  private formatDate(date: Date): string {
    return this.datePipe.transform(date, 'yyyy-MM') || ''; // Format date to YYYY-MM
  }

  private buildExperienceForm():void {
    this.experienceForm = this.formBuilder.group({
      title: ['', Validators.required],
      startAt: ['', Validators.required],
      endAt: ['', Validators.required],
      institutId: [''],
      institutname: [''],
      institutwebsite: [''],
      description: ['', Validators.required]
    });
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
      startAt: this.datePipe.transform(this.experience?.startAt, 'yyyy-MM'),
      endAt: this.datePipe.transform(this.experience?.endAt, 'yyyy-MM'),
      institutId: this.experience?.institut?.id,
      institutname: this.experience?.institut?.name,
      institutwebsite: this.experience?.institut?.website,
    });
  }
}
