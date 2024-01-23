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
  @Input() operation: string = 'update';
  @Input() title: string = 'Edit Profile';
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
      picture: [null],
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
      picture: this.profile?.picture,
      about: this.profile?.about
    });
  }

  onCreate(): void {
    this.createProfile.emit(this.profileForm.value);
  }

  onUpdate(): void {
    this.updateProfile.emit(this.profileForm.value);
  }

  onImagePicked(event: Event): void {
    const file:File = (event.target as HTMLInputElement).files?.[0]!; // Here we use only the first file (single file)
    const maxFileSize = 1024 * 1024 * 5; // 5MB
    console.log(file);
    if (file.size > maxFileSize) {
      alert('File size exceeds 5MB');
      return;
    }
    this.profileForm.patchValue({ picture: file});
    console.log(this.profileForm.get('picture')?.value);
  }
}
