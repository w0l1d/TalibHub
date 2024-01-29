import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as env } from '../../environments/environment.development';
import Review from '../models/review';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  baseUrl: string = '';

  constructor(
    private http: HttpClient
  ) { 
    this.baseUrl = env.api;
  }

  public addReview(review: Review): Observable<any> {
    console.log("Adding review: " + JSON.stringify(review));
    return this.http.post(`${this.baseUrl}/reviews`, review);
  }

  public updateReview(review: Review): any {
    return this.http.put(`${this.baseUrl}/reviews/${review.id}`, review);
  }

  public deleteReview(reviewId: string): any {
    return this.http.delete(`${this.baseUrl}/reviews/${reviewId}`);
  }
}
