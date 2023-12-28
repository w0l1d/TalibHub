import { Routes } from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import { HomeComponent } from './pages/home/home.component';
import { StudentManagementComponent } from './pages/student-management/student-management.component';
import { StudentProfileComponent } from './pages/student-profile/student-profile.component';

export const routes: Routes = [
  {path: 'login',component: LoginComponent},
  {path: 'home', component: HomeComponent},
  {path: 'studentManagement', component: StudentManagementComponent},
  {path: 'studentProfile', component: StudentProfileComponent},
  {path: '', redirectTo: '/login', pathMatch: 'full'}
];
