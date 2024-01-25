import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import Profile from "../../models/profile";

export interface ProfileForm{
  about: string;
  student: {
    email: string;
    phone: string;
    picture: File;
  }
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
  @Input() operation: string = 'update';
  @Input() title: string = 'Edit Profile';
  @Input() profile?: Profile;
  @Output() createProfile: EventEmitter<ProfileForm> = new EventEmitter();
  @Output() updateProfile: EventEmitter<ProfileForm> = new EventEmitter();
  profileFormGroup!: FormGroup;
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
    this.profileFormGroup = this.formBuilder.group({
      email: ['', [Validators.email, Validators.required]],
      phone: ['', Validators.required],
      picture: [null],
      about: [''],
    });
  }


  private resetForm():void {
    this.profileFormGroup.reset();
  }

  private fillForm():void {
    this.profileFormGroup.patchValue({
      about: this.profile?.about,
      email: this.profile?.student.email,
      phone: this.profile?.student.phone,
      picture: this.profile?.student.picture
    });
  }

  onCreate(): void {
    const profileForm: ProfileForm = {
      about: this.profileFormGroup.get('about')?.value,
      student: {
        email: this.profileFormGroup.get('email')?.value,
        phone: this.profileFormGroup.get('phone')?.value,
        picture: this.profileFormGroup.get('picture')?.value
      }
    }
    this.createProfile.emit(profileForm);
  }

  onUpdate(): void {
    const profileForm: ProfileForm = {
      about: this.profileFormGroup.get('about')?.value,
      student: {
        email: this.profileFormGroup.get('email')?.value,
        phone: this.profileFormGroup.get('phone')?.value,
        picture: this.profileFormGroup.get('picture')?.value
      }
    }
    this.updateProfile.emit(profileForm);
  }

  onImagePicked(event: Event): void {
    const file:File = (event.target as HTMLInputElement).files?.[0]!; // Here we use only the first file (single file)
    const maxFileSize = 1024 * 1024 * 5; // 5MB
    console.log(file);
    if (file.size > maxFileSize) {
      alert('File size exceeds 5MB');
      return;
    }
    this.profileFormGroup.patchValue({ picture: file});
    console.log(this.profileFormGroup.get('picture')?.value);
  }
}
