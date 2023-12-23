import { Component } from '@angular/core';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {environment as env} from "../../../environments/environment.development";
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    HttpClientModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  constructor(
    private http: HttpClient
  ) {
  }

  ngOnInit() {
    this.http.get(`${env.api}/students`).subscribe(data => {
      console.log(data);
    });
  }
}
