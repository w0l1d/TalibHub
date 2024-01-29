import { Component } from '@angular/core';
import { LayoutComponent } from '../../components/layout/layout.component';
import NavbarData from './navbar-data';
import {DatePipe, NgFor, NgForOf, NgIf} from '@angular/common';
import { ReviewService } from '../../services/review.service';
import { ActivatedRoute } from '@angular/router';
import Review from '../../models/review';
import { HttpClientModule } from '@angular/common/http';
import { InstitutService } from '../../services/institut.service';
import Institut from '../../models/institut';
@Component({
  selector: 'app-company-reviews',
  standalone: true,
  imports: [
    LayoutComponent,
    HttpClientModule,
    NgIf,
    NgFor,
    NgForOf,
    DatePipe
  ],
  providers: [
    ReviewService,
    InstitutService
  ],
  templateUrl: './company-reviews.component.html',
  styleUrl: './company-reviews.component.css'
})
export class CompanyReviewsComponent {
  navbarData = NavbarData;
  institut?: Institut;
  addReviewClicked: boolean = false;
  currentReviewText: string = '';
  currentRate: number = 1;
  hoveredRate: number = 0;
  stars: number[] = [1, 0, 0, 0, 0];
  currentDate: Date = new Date();
  intitutId: string = '';
  reviews?: Review[];

  constructor(
    private reviewService: ReviewService,
    private institutService: InstitutService,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      this.intitutId = params['id'] || '';
      this.institutService.getInstitutById(this.intitutId).subscribe((data: any) => {
        console.log(data);
        this.institut = data;
        this.reviews = this.institut?.reviews;
      });
    });
    console.log("Institut id: " + this.intitutId);
  }

  addReview() {
    this.addReviewClicked = true;
  }

  cancelAddReview() {
    this.addReviewClicked = false;
  }

  submitReview() {
    this.addReviewClicked = false;

    console.log("Review text: " + this.currentReviewText);
    console.log("Review rating: " + this.currentRate);

    let review: Review = {
      review: this.currentReviewText,
      rating: this.currentRate,
      institut: {
        id: this.intitutId
      }
    };

      this.reviewService.addReview(review).subscribe((data: any) => {
        console.log(data);
        this.reviews?.unshift(data);
      });

      // reset values
      this.currentReviewText = '';
      this.currentRate = 1;
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

  onReviewTextChanged(event: any): void {
    this.currentReviewText = event.target.value;
  }

  getStarClassByIndexAndRate(rate: number, index: number): string {
    return index <= rate ? 'fas fa-star text-yellow-400 p-1' : 'fas fa-star text-gray-300 p-1';
  }

  protected readonly DatePipe = DatePipe;
}
