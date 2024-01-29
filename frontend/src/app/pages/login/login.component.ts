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
  errorMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [
        Validators.required, Validators.minLength(3), Validators.maxLength(20), Validators.pattern('^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$')]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(20)]]
    });
  }

  onSubmit(): void {
    console.warn('Your order has been submitted', this.loginForm);
    if(this.loginForm.valid) {

      this.authService.login(this.loginForm.getRawValue())
        .then(() => {
          console.log("Login successful");
          if (this.authService.isStudent())
            this.router.navigate(['/home']).then(() => console.log("navigated to home"));
          else
            this.router.navigate(['/studentManagement']).then(() => console.log("navigated to studentManagement"));
      })
      .catch((error: any) => {
          console.error("Login failed", error);
        this.errorMessage = "Username or password incorrect";
      });
    }
  }

}
