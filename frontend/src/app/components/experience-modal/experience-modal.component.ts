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
      if (this.experience !== undefined) {
        console.log('I am here');
        this.fillForm();
      }
    }
    this.expModalCollapsed = !this.expModalCollapsed;
  }

  public toggleAddInstitut(): void {
    this.addInstitutCollapsed = !this.addInstitutCollapsed;
  }

  onCreate():void{
    if (this.experienceForm.invalid) {
      console.log('form invalid ' + this.experienceForm);
      return;
    }
    console.log('form ' + this.experienceForm);
    var institut: Institut;
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
      // search for institut by id in the instituts array
      experience.institut = this.instituts.find((institut) => institut.id === data.experiences[data.experiences.length - 1].institut?.id);
      this.studentProfile?.experiences.push(experience);
      this.toggleAddExpModal();
    });

  }

  public onUpdate():void {
    if (this.experienceForm.invalid) {
      return;
    }
    var institut: Institut;
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
      // search for institut by id in the instituts array
      experience.institut = this.instituts.find((institut) => institut.id === data.experiences[data.experiences.length - 1].institut?.id);
      // update the experience in the studentProfile
      const index: number | undefined = this.studentProfile?.experiences.findIndex((experience) => experience.id === this.experience?.id);
      if (index !== undefined && index !== -1) {

        this.studentProfile!.experiences[index] = experience;
        // Assuming 'experience' is the object you want to assign to the array
      } else {
        // Handle the case where the index was not found
        console.error('Experience not found or index is undefined');
      }
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
      institut: this.experience?.institut
    });
  }
}
