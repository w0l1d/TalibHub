import { Routes } from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import { HomeComponent } from './pages/home/home.component';
import { StudentManagementComponent } from './pages/student-management/student-management.component';
import { StudentProfileComponent } from './pages/student-profile/student-profile.component';
import { PostForumComponent } from './pages/post-forum/post-forum.component';

export const routes: Routes = [
  {path: 'login',component: LoginComponent},
  {path: 'home', component: HomeComponent},
  {path: 'studentManagement', component: StudentManagementComponent},
  {path: 'studentProfile', component: StudentProfileComponent},
  {path: 'talib/:id', component: StudentProfileComponent},
  {path: 'post/:id', component: PostForumComponent},
  {path: '', redirectTo: '/login', pathMatch: 'full'},
];
