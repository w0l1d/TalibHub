import {Component, Input} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import Institut from "../../models/institut";
import Education from "../../models/education";
import Profile from "../../models/profile";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {InstitutService} from "../../services/institut.service";
import {StudentProfileService} from "../../services/student-profile.service";

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
  @Input() title: string = 'Create New Education';
  @Input() operation: string = 'Create';


  constructor(
    private formBuilder: FormBuilder,
    private institutService: InstitutService,
    private datePipe: DatePipe
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
    }
    this.educModalCollapsed = !this.educModalCollapsed;
  }

  public toggleAddInstitut():void {
    this.addInstitutCollapsed = !this.addInstitutCollapsed;
  }

  public onCreate(): void {
    console.warn('Your form has been submitted', this.educationForm);
    if(this.educationForm.valid) {
      // create institut object
      const institut :Institut = {
        id: this.educationForm.get('institutId')?.value === '' ? null : this.educationForm.get('institutId')?.value,
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
        // search for institut by id in the instituts array
        education.institut = this.instituts.find((institut) => institut.id === data.educations[data.educations.length - 1].institut?.id);
        this.studentProfile?.educations.push(education);
        this.toggleAddEducModal();
      });
    }
  }

  public onUpdate(): void {

  }

  private formatDate(date: Date): string {
    return this.datePipe.transform(date, 'yyyy-MM') || ''; // Format date to YYYY-MM
  }

  public getInstituts(): void {
    this.institutService.getInstituts().subscribe((data: Institut[]) => {
      console.log('Received Data:', data); // Log the received data
      this.instituts = data; // Assign the received data to the instituts property
    });
  }

  private buildEducationForm():void {
    this.educationForm = this.formBuilder.group({
      title: ['', [Validators.required]],
      studyField: ['', [Validators.required]],
      startAt: ['', [Validators.required]],
      endAt: ['', [Validators.required]],
      institutId: [''],
      institutname: [''],
      institutwebsite: [''],
      description: ['', [Validators.required]]
    });
  }

  // empty the form
  public resetForm(): void {
    this.educationForm.reset();
  }


}
