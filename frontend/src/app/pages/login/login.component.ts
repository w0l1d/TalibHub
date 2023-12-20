import { Component } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { environment as env } from "../../../environments/environment.development";
import { HttpClient, HttpClientModule } from "@angular/common/http";
import { NgIf } from "@angular/common";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    HttpClientModule,
    NgIf
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loginForm: FormGroup | any;

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient,
  ) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(20)]]
    });
  }

  onSubmit(): void {
    console.warn('Your order has been submitted', this.loginForm);
    if(this.loginForm.valid) {

      this.http.post(`${env.api}/auth/login`, this.loginForm.getRawValue())
      .subscribe((res: any) => {
        console.log(res);
        localStorage.setItem("accessToken", res.accessToken);
        localStorage.setItem("refreshToken", res.refreshToken);

        // TODO: redirect to home page
      })
    }
  }

}
