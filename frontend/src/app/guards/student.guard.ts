import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";

export const studentGuard: CanActivateFn = (route, state) => {
  return inject(AuthService).isStudent()
    ? true
    : inject(Router).createUrlTree(['/login'])
};
