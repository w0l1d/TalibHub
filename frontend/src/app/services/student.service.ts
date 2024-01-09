import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment as env} from "../../environments/environment.development";
import Student from "../models/student";

@Injectable({
  providedIn: "root"
})
export class StudentService {

  readonly baseUrl: string;

  constructor(
    private http: HttpClient
  ) {
    this.baseUrl = env.api;
  }

  // save all students
  public saveAllStudents(students: Student[]): any {
    return this.http.post(`${this.baseUrl}/students/saveAll`, students);
  }

}
