import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment as env} from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class StudentProfileService {

  readonly baseUrl: string;

  constructor(
    private http: HttpClient
  ) {
    this.baseUrl = env.api;
  }

  getStudentProfile(): Observable<any> {
    return this.http.get(`${this.baseUrl}/profiles/me`);
  }

}
