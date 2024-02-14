import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment as env} from "../../environments/environment.development";


@Injectable({
  providedIn: 'root'
})
export class FileUploadService {
  baseUrl: string = '';

  constructor(
    private http: HttpClient
  ) {
    this.baseUrl = env.api;
  }

  public uploadImage(image: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', image);
    return this.http.post(`${this.baseUrl}/public/files/`, formData);
  }
}
