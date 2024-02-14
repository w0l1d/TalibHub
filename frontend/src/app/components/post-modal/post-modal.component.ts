import {NgForOf, NgIf} from '@angular/common';
import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
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

  onCreate(): void {
    const posteForm: PostForm = {
      titre: this.posteFormGroup.get('titre')?.value,
      description: this.posteFormGroup.get('description')?.value,
      image: this.posteFormGroup.get('image')?.value
    }
    this.createPoste.emit(posteForm);
    this.posteModalCollapsed = true;
  }


  private resetForm():void {
    this.posteFormGroup.reset();
  }

  onUpdate(): void {
    const posteForm: PostForm = {
      titre: this.posteFormGroup.get('titre')?.value,
      description: this.posteFormGroup.get('description')?.value,
      image: this.posteFormGroup.get('image')?.value
    }
    this.updatePoste.emit(posteForm);
    this.posteModalCollapsed = true;
  }

  onImagePicked(event: Event): void {
    const file:File = (event.target as HTMLInputElement).files?.[0]!; // Here we use only the first file (single file)
    const maxFileSize = 1024 * 1024 * 5; // 5MB
    console.log(file);
    if (file.size > maxFileSize) {
      alert('File size exceeds 5MB');
      return;
    }
    // after merging with file management, should uncomment this line
    this.posteFormGroup.patchValue({ image: file});
    console.log(this.posteFormGroup.get('image')?.value);
  }

  private buildPosteForm(): void {
    this.posteFormGroup = this.formBuilder.group({
      titre: ['', Validators.required],
      description: ['', Validators.required],
      image: [null]
    });
  }

  private fillForm():void {
    this.posteFormGroup.patchValue({
      titre: this.poste?.titre,
      description: this.poste?.description,
      image: this.poste?.image
    });
  }

}
