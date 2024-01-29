import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, switchMap} from 'rxjs';
import {environment as env} from '../../environments/environment.development';
import Education from "../models/education";
import Experience from "../models/experience";
import {ProfileForm} from "../components/profile-form-modal/profile-form-modal.component";
import {FileUploadService} from "./file-upload.service";

@Injectable({
  providedIn: 'root'
})
export class StudentProfileService {

  readonly baseUrl: string;

  constructor(
    private http: HttpClient,
    private fileUploadService: FileUploadService
  ) {
    this.baseUrl = env.api;
  }

  public getStudentProfile(): Observable<any> {
    return this.http.get(`${this.baseUrl}/profiles/me`);
  }

  public getStudentProfileById(profileId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/profiles/${profileId}`);
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

    const experienceData: any = {
      title: experience.title,
      description: experience.description,
      startAt: experience.startAt,
      endAt: experience.endAt,
    }

    if (experience.institut.id) {
      experienceData.institut.id = experience.institut.id;
    } else {
      experienceData.institut = {
        name: experience.institut.name,
        website: experience.institut.website,
        image: experience.institut.image
      }
    }

    let fileUploadObservable: Observable<any> = new Observable<any>();
    if (experience.institut.image) {
      fileUploadObservable = this.fileUploadService.uploadImage(experience.institut.image as File);
    }

    return fileUploadObservable.pipe(
      switchMap((data: any) => {
        if (data) {
          experienceData.institut.imageUri = data.filename;
        }
        return this.http.post(`${this.baseUrl}/profiles/${profileId}/experiences`, experienceData);
      })
    );
  }

  public updateExperience(profileId:string, experience: Experience): Observable<any> {

    const experienceData: any = {
      title: experience.title,
      description: experience.description,
      startAt: experience.startAt,
      endAt: experience.endAt,
      institut: {}
    }

    if (experience.institut.id) {
      experienceData.institut.id = experience.institut.id;
    } else {
      experienceData.institut = {
        name: experience.institut.name,
        website: experience.institut.website,
        image: experience.institut.image
      }
    }
    console.log("Experience data: " + JSON.stringify(experienceData));
    let fileUploadObservable: Observable<any> = new Observable<any>();
    if (experience.institut.image) {
      fileUploadObservable = this.fileUploadService.uploadImage(experience.institut.image as File);
    }

    return fileUploadObservable.pipe(
      switchMap((data: any) => {
        if (data) {
          experienceData.institut.imageUri = data.filename;
        }
        return this.http.put(`${this.baseUrl}/profiles/${profileId}/experiences/${experience.id}`, experienceData);
      })
    );
  }

  public deleteExperience(profileId:string, experienceId: string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/profiles/${profileId}/experiences/${experienceId}`);
  }

  public updateProfile(profileId:string,profileForm: ProfileForm): Observable<any> {

    const profileData: any = {
      about: profileForm.about,
      student: {
        email: profileForm.student.email,
        phone: profileForm.student.phone
      }
    }

    let fileUploadObservable: Observable<any> = new Observable<any>();
    if (profileForm.student.picture) {
      fileUploadObservable = this.fileUploadService.uploadImage(profileForm.student.picture as File);
    }

    return fileUploadObservable.pipe(
      switchMap((data: any) => {
        if (data) {
          profileData.student.imageUri = data.filename;
          console.log("Image uploaded: " + profileData.student.imageUri);
        }
        return this.http.put(`${this.baseUrl}/profiles/${profileId}`, profileData);
      })
    );
  }

  public searchStudents(searchTerm: string): any {
    return this.http.get(`${this.baseUrl}/profiles/search`, {
      params: {
        query: searchTerm
      }
    });
  }


}
