import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";

export const managerGuard: CanActivateFn = (route, state) => {
  return inject(AuthService).isManager()
    ? true
    : inject(Router).createUrlTree(['/login'])
};
