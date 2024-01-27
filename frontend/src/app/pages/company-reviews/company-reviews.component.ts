import { Component } from '@angular/core';
import { LayoutComponent } from '../../components/layout/layout.component';
import NavbarData from './navbar-data';
import { NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-company-reviews',
  standalone: true,
  imports: [
    LayoutComponent,
    NgIf,
    NgFor
  ],
  templateUrl: './company-reviews.component.html',
  styleUrl: './company-reviews.component.css'
})
export class CompanyReviewsComponent {
  navbarData = NavbarData;
  addReviewClicked: boolean = false;
  currentRate: number = 1;
  hoveredRate: number = 0;
  stars: number[] = [1, 0, 0, 0, 0];

  constructor() { }

  ngOnInit() {
  }

  addReview() {
    this.addReviewClicked = true;
  }

  cancelAddReview() {
    this.addReviewClicked = false;
  }

  submitReview() {
    this.addReviewClicked = false;
  }

  handleRateChange(rate: number) {
    this.currentRate = rate;
    this.stars = this.stars.map((_, i) => {
      return i < rate ? 1 : 0;
    });
  }

  starClass(index: number) {
    return index < this.hoveredRate ? 'fas fa-star text-yellow-400 p-1 hover:cursor-pointer' : this.stars[index] ? 'fas fa-star text-yellow-400 p-1 hover:cursor-pointer' : 'fas fa-star text-gray-300 p-1 hover:cursor-pointer';
  }

  handleHover(rate: number): void {
    this.hoveredRate = rate;
  }

}
