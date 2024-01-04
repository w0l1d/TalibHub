import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment as env} from '../../environments/environment.development';
import Education from "../models/education";
import Experience from "../models/experience";

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

  public getStudentProfile(): Observable<any> {
    return this.http.get(`${this.baseUrl}/profiles/me`);
  }

  public addEducation(profileId:string, education: Education): Observable<any> {
    return this.http.post(`${this.baseUrl}/profiles/${profileId}/educations`, education);
  }

  public updateEducation(profileId:string, education: Education): Observable<any> {
    return this.http.put(`${this.baseUrl}/profiles/${profileId}/educations/${education.id}`, education);
  }

  public deleteEducation(profileId:string, educationId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/profiles/${profileId}/educations/${educationId}`);
  }

  public addExperience(profileId:string, experience: Experience): Observable<any> {
    return this.http.post(`${this.baseUrl}/profiles/${profileId}/experiences`, experience);
  }

  public updateExperience(profileId:string, experience: Experience): Observable<any> {
    return this.http.put(`${this.baseUrl}/profiles/${profileId}/experiences/${experience.id}`, experience);
  }

  public deleteExperience(profileId:string, experienceId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/profiles/${profileId}/experiences/${experienceId}`);
  }



}
