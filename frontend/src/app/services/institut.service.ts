import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment as env} from "../../environments/environment.development";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class InstitutService {
  baseUrl: string = '';
  constructor(private http: HttpClient) {
    this.baseUrl = env.api;
  }

  getInstituts(): Observable<any> {
    return this.http.get(`${this.baseUrl}/instituts`);
  }

  getInstitutById(institutId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/instituts/${institutId}`);
  }
}
