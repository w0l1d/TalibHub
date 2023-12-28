import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment as env } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class StudentProfileService {

  readonly baseUrl;

  constructor(
    private httpClient: HttpClient
  ) { 
    this.baseUrl = env.api;
  }

  getStudentProfile(): Observable<any> {
    return this.httpClient.get(`${this.baseUrl}/profiles/me`);
  }

}
