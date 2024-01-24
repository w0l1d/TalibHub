import {ApplicationConfig} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideHttpClient} from "@angular/common/http";
import {tokenInterceptor} from "./interceptors/token.interceptor";
import {studentGuard} from "./guards/student.guard";
import {managerGuard} from "./guards/manager.guard";

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(),
    tokenInterceptor,
    {provide: 'studentGuard', useValue: studentGuard},
    {provide: 'managerGuard', useValue: managerGuard}
  ]
};
