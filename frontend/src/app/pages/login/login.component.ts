import { Component } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { NgIf } from "@angular/common";
import {AuthService} from "../../services/auth.service";
import {HttpResponse} from "@angular/common/http";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loginForm: FormGroup | any;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
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

      this.authService.login(this.loginForm.getRawValue())
      .subscribe((res: HttpResponse<any>) => {
        if(res.ok) {
          console.log(res.body);
          localStorage.setItem("accessToken", res.body.accessToken);
          localStorage.setItem("refreshToken", res.body.refreshToken);
          // TODO: redirect to home page

        }else{
          console.log("Login failed");
        }
      })
    }
  }

}
