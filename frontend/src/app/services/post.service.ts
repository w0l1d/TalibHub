import { Injectable } from '@angular/core';
import {environment as env} from "../../environments/environment.development";
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  readonly baseUrl: string;

  constructor(
    private http: HttpClient
  ) {
    this.baseUrl = env.api;
  }

  // get all posts
  public getAllPosts(): any {
    return this.http.get(`${this.baseUrl}/posts`);
  }
}
