import { Component } from '@angular/core';
import NavbarData from './navbar-data';
import { LayoutComponent } from '../../components/layout/layout.component';
import {ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-post-forum',
  standalone: true,
  imports: [
    LayoutComponent,
    ReactiveFormsModule
  ],
  templateUrl: './post-forum.component.html',
  styleUrl: './post-forum.component.css'
})
export class PostForumComponent {

  navbarData = NavbarData;

  constructor() {

  }



}
