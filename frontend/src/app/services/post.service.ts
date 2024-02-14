import {Injectable} from '@angular/core';
import {environment as env} from "../../environments/environment.development";
import {HttpClient} from '@angular/common/http';
import {Observable, switchMap} from "rxjs";
import Poste from "../models/poste";
import {FileUploadService} from "./file-upload.service";

@Injectable({
  providedIn: 'root'
})
export class PostService {

  readonly baseUrl: string;

  constructor(
    private http: HttpClient,
    private fileUploadService: FileUploadService
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

  // add post
  addPoste(poste: Poste): Observable<any> {

    let fileUploadObservable: Observable<any> = new Observable<any>();
    if (poste.image)
      fileUploadObservable = this.fileUploadService.uploadImage(poste.image as File);
    const postData = {
      "titre": poste.titre,
      "description": poste.description,
      "imageUri": poste.imageUri
    };
    console.log("postData 000 ", postData);
    return fileUploadObservable.pipe(
      switchMap((data: any) => {
        if (data) {
          postData.imageUri = data.filename;
        }
        console.log("postData", postData);
        return this.http.post(`${this.baseUrl}/postes`, postData);
      }))
  }
  // add comment to post
  public addCommentToPost(id: string, comment: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/postes/${id}/comments`, {"content": comment});
  }


}
