import { Injectable } from '@angular/core';
import {environment as env} from "../../environments/environment.development";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private http: HttpClient
  )
  { }

  login(data: any) {
    return this.http.post(`${env.api}/auth/login`, data, {observe: 'response'});
  }
}
