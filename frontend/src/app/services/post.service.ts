import {Injectable} from '@angular/core';
import {environment as env} from "../../environments/environment.development";
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";

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
  public getAllPosts(): Observable<any> {
    return this.http.get(`${this.baseUrl}/postes`);
  }

  // get post by id
  public getPostById(id: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/postes/${id}`);
  }

  // add comment to post
  public addCommentToPost(id: string, comment: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/postes/${id}/comments`, {"content": comment});
  }


}
