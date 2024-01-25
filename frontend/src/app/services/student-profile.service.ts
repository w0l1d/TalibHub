import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment as env} from '../../environments/environment.development';
import Education from "../models/education";
import Experience from "../models/experience";
import {ProfileForm} from "../components/profile-form-modal/profile-form-modal.component";

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
    const formData: FormData = new FormData();

    // Append Education properties
    formData.append('title', education.title);
    formData.append('studyField', education.studyField);
    formData.append('description', education.description);
    formData.append('startAt', education.startAt);
    formData.append('endAt', education.endAt);

    // Append Institut properties
    formData.append('institut.name', education.institut.name);
    formData.append('institut.website', education.institut.website);

    // Append Image (if available)
    if (education.institut.image) {
      formData.append('institut.image', education.institut.image as Blob);
    }
    return this.http.post(`${this.baseUrl}/profiles/${profileId}/educations`, formData);
  }

  public updateEducation(profileId:string, education: Education): Observable<any> {
    const formData: FormData = new FormData();

    // Append Education properties
    formData.append('title', education.title);
    formData.append('studyField', education.studyField);
    formData.append('description', education.description);
    formData.append('startAt', education.startAt);
    formData.append('endAt', education.endAt);

    // Append Institut properties
    formData.append('institut.name', education.institut.name);
    formData.append('institut.website', education.institut.website);

    // Append Image (if available)
    if (education.institut.image) {
      formData.append('institut.image', education.institut.image as Blob);
    }
    return this.http.put(`${this.baseUrl}/profiles/${profileId}/educations/${education.id}`, formData);
  }

  public deleteEducation(profileId:string, educationId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/profiles/${profileId}/educations/${educationId}`);
  }

  public addExperience(profileId:string, experience: Experience): Observable<any> {
    const formData: FormData = new FormData();

    // Append Experience properties
    formData.append('title', experience.title);
    formData.append('description', experience.description);
    formData.append('startAt', experience.startAt);
    formData.append('endAt', experience.endAt);

    // Append Institut properties
    formData.append('institut.name', experience.institut.name);
    formData.append('institut.website', experience.institut.website);

    // Append Image (if available)
    if (experience.institut.image) {
      formData.append('institut.image', experience.institut.image as Blob);
    }


    return this.http.post(`${this.baseUrl}/profiles/${profileId}/experiences`, formData);
  }

  public updateExperience(profileId:string, experience: Experience): Observable<any> {
    const formData: FormData = new FormData();

    // Append Experience properties
    formData.append('title', experience.title);
    formData.append('description', experience.description);
    formData.append('startAt', experience.startAt);
    formData.append('endAt', experience.endAt);

    // Append Institut properties
    formData.append('institut.name', experience.institut.name);
    formData.append('institut.website', experience.institut.website);

    // Append Image (if available)
    if (experience.institut.image) {
      formData.append('institut.image', experience.institut.image as Blob);
    }
    return this.http.put(`${this.baseUrl}/profiles/${profileId}/experiences/${experience.id}`, formData);
  }

  public deleteExperience(profileId:string, experienceId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/profiles/${profileId}/experiences/${experienceId}`);
  }

  public updateProfile(profileId:string,profileForm: ProfileForm): Observable<any> {

    const formData:FormData = new FormData();
    formData.append('about', profileForm.about);
    formData.append('student.email', profileForm.student.email);
    formData.append('student.phone', profileForm.student.phone);
    if (profileForm.student.picture) {
      formData.append('student.picture', profileForm.student.picture as Blob);
    }

    return this.http.put(`${this.baseUrl}/profiles/${profileId}`, formData);
  }



}
