import {Component} from "@angular/core";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

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

    loginForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
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
      .then((success: boolean) => {
        if (success) {
            console.log("Login successful");
            this.router.navigate(['/home']).then(() => console.log("navigated to home"));
        }else {
            console.log("Login failed");
        }
      })
      .catch((error: any) => {
          console.error("Login failed", error);
      });
    }
  }

}
