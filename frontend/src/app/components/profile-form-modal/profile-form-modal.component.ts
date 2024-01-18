import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import Profile from "../../models/profile";

export interface ProfileForm{
  email: string;
  phone: string;
  about: string;
}

@Component({
  selector: 'app-profile-form-modal',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './profile-form-modal.component.html',
  styleUrl: './profile-form-modal.component.css'
})
export class ProfileFormModalComponent {
  @Input() operation: string = 'Add';
  @Input() title: string = 'Add Profile';
  @Input() profile?: Profile;
  @Output() createProfile: EventEmitter<ProfileForm> = new EventEmitter();
  @Output() updateProfile: EventEmitter<ProfileForm> = new EventEmitter();
  profileForm!: FormGroup;
  profileModalCollapsed: boolean = true;


  constructor(
    private formBuilder: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.buildProfileForm();
  }


  public toggleProfileModal(): void {
    if (this.profileModalCollapsed) {
      this.resetForm();
      if (this.profile !== undefined) {
        console.log('I am here');
        this.fillForm()
      }
    }
    this.profileModalCollapsed = !this.profileModalCollapsed;
  }

  private buildProfileForm(): void {
    this.profileForm = this.formBuilder.group({
      email: ['', [Validators.email, Validators.required]],
      phone: ['', Validators.required],
      about: [''],
    });
  }


  private resetForm():void {
    this.profileForm.reset();
  }

  private fillForm():void {
    this.profileForm.patchValue({
      email: this.profile?.student.email,
      phone: this.profile?.student.phone,
      about: this.profile?.about
    });
  }

  onCreate(): void {
    this.createProfile.emit(this.profileForm.value);
  }

  onUpdate(): void {
    this.updateProfile.emit(this.profileForm.value);
  }
}
