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

    const educationData: any = {
      title: education.title,
      studyField: education.studyField,
      description: education.description,
      startAt: education.startAt,
      endAt: education.endAt,
      institut: {}
    }

    if (education.institut.id) {
      educationData.institut.id = education.institut.id;
    } else {
      educationData.institut = {
        name: education.institut.name,
        website: education.institut.website,
        image: education.institut.image
      }
    }
    let fileUploadObservable: Observable<any> = new Observable<any>();
    if (education.institut.image) {
      fileUploadObservable = this.fileUploadService.uploadImage(education.institut.image as File);
      return fileUploadObservable.pipe(
        switchMap((data: any) => {
          if (data) {
            educationData.institut.imageUri = data.filename;
          }
          return this.http.post(`${this.baseUrl}/profiles/${profileId}/educations`, educationData);
        })
      );
    }
    return this.http.post(`${this.baseUrl}/profiles/${profileId}/educations`, educationData);
  }

  public updateEducation(profileId:string, education: Education): Observable<any> {

    const educationData: any = {
      title: education.title,
      studyField: education.studyField,
      description: education.description,
      startAt: education.startAt,
      endAt: education.endAt,
      institut: {}
    }

    if (education.institut.id) {
      educationData.institut.id = education.institut.id;
    } else {
      educationData.institut = {
        name: education.institut.name,
        website: education.institut.website,
        image: education.institut.image
      }
    }

    let fileUploadObservable: Observable<any> = new Observable<any>();
    if (education.institut.image) {
      fileUploadObservable = this.fileUploadService.uploadImage(education.institut.image as File);
      return fileUploadObservable.pipe(
        switchMap((data: any) => {
          if (data) {
            educationData.institut.imageUri = data.filename;
          }
          return this.http.put(`${this.baseUrl}/profiles/${profileId}/educations/${education.id}`, educationData);
        })
      );
    }
    return this.http.put(`${this.baseUrl}/profiles/${profileId}/educations/${education.id}`, educationData);
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

    let fileUploadObservable: Observable<any> = new Observable<any>();
    if (experience.institut.image) {
      fileUploadObservable = this.fileUploadService.uploadImage(experience.institut.image as File);
      return fileUploadObservable.pipe(
        switchMap((data: any) => {
          if (data) {
            experienceData.institut.imageUri = data.filename;
          }
          return this.http.post(`${this.baseUrl}/profiles/${profileId}/experiences`, experienceData);
        })
      );
    }
    return this.http.post(`${this.baseUrl}/profiles/${profileId}/experiences`, experienceData);
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
      return fileUploadObservable.pipe(
        switchMap((data: any) => {
          if (data) {
            experienceData.institut.imageUri = data.filename;
          }
          return this.http.put(`${this.baseUrl}/profiles/${profileId}/experiences/${experience.id}`, experienceData);
        })
      );
    }
    return this.http.put(`${this.baseUrl}/profiles/${profileId}/experiences/${experience.id}`, experienceData);
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
    return this.http.put(`${this.baseUrl}/profiles/${profileId}`, profileData);

  }

  public searchStudents(searchTerm: string): any {
    return this.http.get(`${this.baseUrl}/profiles/search`, {
      params: {
        query: searchTerm
      }
    });
  }


}
