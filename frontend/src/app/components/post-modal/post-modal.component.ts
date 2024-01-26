import { NgForOf, NgIf } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import Poste from '../../models/poste';

export interface PostForm{
  titre: string;
  description: string;
  image?: File;
}

@Component({
  selector: 'app-post-modal',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './post-modal.component.html',
  styleUrl: './post-modal.component.css'
})
export class PostModalComponent {
  @Input() operation: string = 'update';
  @Input() title: string = 'Edit Profile';
  @Input() poste?: Poste;
  @Output() createPoste: EventEmitter<PostForm> = new EventEmitter();
  @Output() updatePoste: EventEmitter<PostForm> = new EventEmitter();
  posteFormGroup!: FormGroup;
  posteModalCollapsed: boolean = true;

  constructor(
    private formBuilder: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.buildPosteForm();
  }


  public togglePosteModal(): void {
    if (this.posteModalCollapsed) {
      this.resetForm();
      if (this.poste !== undefined) {
        console.log('I am here');
        this.fillForm()
      }
    }
    this.posteModalCollapsed = !this.posteModalCollapsed;
  }

  private buildPosteForm(): void {
    this.posteFormGroup = this.formBuilder.group({
      email: ['', [Validators.email, Validators.required]],
      phone: ['', Validators.required],
      picture: [null],
      about: [''],
    });
  }


  private resetForm():void {
    this.posteFormGroup.reset();
  }

  private fillForm():void {
    this.posteFormGroup.patchValue({
      description: this.poste?.description,
      titre: this.poste?.titre,
      image: this.poste?.image
    });
  }

  onCreate(): void {
    const posteForm: PostForm = {
      description: this.posteFormGroup.get('description')?.value,
      titre: this.posteFormGroup.get('titre')?.value,
      image: this.posteFormGroup.get('image')?.value
    }
    this.createPoste.emit(posteForm);
  }

  onUpdate(): void {
    const posteForm: PostForm = {
      description: this.posteFormGroup.get('description')?.value,
      titre: this.posteFormGroup.get('titre')?.value,
      image: this.posteFormGroup.get('image')?.value
    }
    this.updatePoste.emit(posteForm);
  }

  onImagePicked(event: Event): void {
    const file:File = (event.target as HTMLInputElement).files?.[0]!; // Here we use only the first file (single file)
    const maxFileSize = 1024 * 1024 * 5; // 5MB
    console.log(file);
    if (file.size > maxFileSize) {
      alert('File size exceeds 5MB');
      return;
    }
    this.posteFormGroup.patchValue({ picture: file});
    console.log(this.posteFormGroup.get('image')?.value);
  }

}
